package org.immunophenotype.web.services;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class HeatmapService {

    @Autowired
    DataSource komp2DataSource;

    public List<HeatmapRow> getHeatmapRows() throws SQLException {
        Map<String, HeatmapRow> rows = new HashMap<>();

        Connection connection = komp2DataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from threei_data_for_heat_map");// where Gene='Elac2' order by Gene");
        ResultSet results = statement.executeQuery();
        HeatmapRow row = null;
        while (results.next()) {

            String gene = results.getString("Gene");
            String construct = results.getString("Construct");
            if(construct.contains("(")){
            	String [] constructs =construct.split("\\(");
            	construct=constructs[0];
            }
            String procedureName = results.getString("ProcedureName");
            String callType = results.getString("CallType");
            if (!rows.containsKey(gene)) {
                if (row != null) {
                    rows.put(row.getGene(), row);
                }
                row = new HeatmapRow(gene, construct);
            }
            //System.out.println("adding result="+results.getString("Gene")+"|"+results.getString("Construct")+"|"+results.getString("ProcedureName")+"|"+results.getString("CallType")+"|"+results.getString("Gender"));
            if (!row.getProcedureSignificance().containsKey(procedureName)) {
                row.getProcedureSignificance().put(procedureName, getRankFromSignificanceName(callType));//set rank for a new procedure
            } else {
                //assign the highest rank for a the procedure
                row = this.setHighestRankForProcedure(row, procedureName, callType);
            }
            //genes.add(results.getString("Gene"));
            //genes.add(results.getString("Construct"));

        }

        List<HeatmapRow> heatmapRows = new ArrayList<>();
        for (String key : rows.keySet()) {
            //System.out.println(rows.get(key));
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
            for (String procedure : procedures) {
                if (!uniqueProcedures.contains(procedure)) {
                    uniqueProcedures.add(procedure);
                }

            }
        }
        return new ArrayList<>(uniqueProcedures);
    }

}
