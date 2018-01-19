package org.immunophenotype.web.services;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class HeatmapService {

    private DataSource dataSource;

    public HeatmapService(DataSource dataSource) {
        Assert.notNull(dataSource, "Datasource cannot be null");
        this.dataSource = dataSource;
    }

    public List<HeatmapRow> getHeatmapRows() throws SQLException {
        Map<String, HeatmapRow> rows = new HashMap<>();

        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select Gene, Construct, ProcedureName, CallType from threei_data_for_heat_map order by Gene");// where Gene='Elac2' order by Gene");
        ResultSet results = statement.executeQuery();

        while (results.next()) {


            String gene = results.getString("Gene");
            String construct = results.getString("Construct");
            if(construct.contains("(")){
            	String [] constructs =construct.split("\\(");
            	construct=constructs[0];
            }

            HeatmapRow row = null;
            if (!rows.containsKey(gene)) {
                row = new HeatmapRow(gene, construct);
                rows.put(gene, row);
            } else {
                row = rows.get(gene);
            }

            String procedureName = results.getString("ProcedureName");
            String callType = results.getString("CallType");

            //System.out.println("adding result="+results.getString("Gene")+"|"+results.getString("Construct")+"|"+results.getString("ProcedureName")+"|"+results.getString("CallType")+"|"+results.getString("Gender"));
            if (!row.getProcedureSignificance().containsKey(procedureName)) {
                row.getProcedureSignificance().put(procedureName, getRankFromSignificanceName(callType));//set rank for a new procedure
            } else {
                //assign the highest rank for a the procedure
                row = this.setHighestRankForProcedure(row, procedureName, callType);
            }

        }


        List<HeatmapRow> heatmapRows = new ArrayList<>();
        for (String key : rows.keySet()) {
            heatmapRows.add(rows.get(key));
        }



        return heatmapRows;
    }

    private HeatmapRow setHighestRankForProcedure(HeatmapRow row, String procedureName, String callType) {
        Integer oldCall = row.getProcedureSignificance().get(procedureName);
        Integer newCall = this.getRankFromSignificanceName(callType);
        if (newCall > oldCall) {
            row.getProcedureSignificance().put(procedureName, newCall);
        }
        return row;

    }

    private Integer getRankFromSignificanceName(String significanceString) {

        //1 not enough data yet
        //2 not significantly different from WT calls
        //3 high sig
        //4 or 0 no data, call it 0 before display as ranks senisbly then
        int rank = 0;

        if (significanceString == null) {
            return 0;
        }

        switch (significanceString.toLowerCase()) {
            case "not significant":
                rank = 2;
                break;
                //if 4 then call it 0 and change all 0s to 4s at the end when creating rows.
            case "pending":
                rank = 1;
                break;
//            case "not performed or applicable":
//                significance = 4;
//                break;
            case "significant":
                rank = 3;
                break;
            case "early indication of possible phenotype":
                rank = 1;
                break;
            default:
                rank = 0;
                break;
        }

        return rank;

    }

    /**
     * Get distinct set of procedure names from the rows to get column headers
     *
     * @param rows
     * @return
     */
    public List<String> getDistinctProcedureNames(List<HeatmapRow> rows) {
        Set<String> uniqueProcedures = new HashSet<>();
        for (HeatmapRow row : rows) {
            Set<String> procedures = row.getProcedureSignificance().keySet();
            uniqueProcedures.addAll(procedures);
        }
        return new ArrayList<>(uniqueProcedures);
    }

}
