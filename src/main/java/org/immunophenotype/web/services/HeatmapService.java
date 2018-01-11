package org.immunophenotype.web.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeatmapService {
	
	@Autowired
	DataSource komp2DataSource;
	
	public List<HeatmapRow> getHeatmapRows() throws SQLException{
		Map<String,HeatmapRow> rows=new HashMap<>();
		
		Connection connection = komp2DataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from threei_data_for_heat_map");// where Gene='Elac2' order by Gene");
		ResultSet results = statement.executeQuery();
		HeatmapRow row=null;
		while(results.next()){
			
			String gene=results.getString("Gene");
			String construct=results.getString("Construct");
			String procedureName=results.getString("ProcedureName");
			String callType=results.getString("CallType");
			if(!rows.containsKey(gene)){
				if(row !=null){
				rows.put(row.getGene(), row);
				}
				row=new HeatmapRow(gene,construct);
			}
			//System.out.println("adding result="+results.getString("Gene")+"|"+results.getString("Construct")+"|"+results.getString("ProcedureName")+"|"+results.getString("CallType")+"|"+results.getString("Gender"));
			if(!row.getProcedureSignificance().containsKey(procedureName)){
				row.getProcedureSignificance().put(procedureName, getRankFromSignificanceName(callType));//set rank for a new procedure
			}else
			{
				//assign the highest rank for a the procedure
				row=this.setHighestRankForProcedure(row,procedureName, callType);
			}
			//genes.add(results.getString("Gene"));
			//genes.add(results.getString("Construct"));
			
		}
		
		List<HeatmapRow> heatmapRows=new ArrayList<>();
		for(String key:rows.keySet()){
			//System.out.println(rows.get(key));
			heatmapRows.add(rows.get(key));
		}
		return heatmapRows;
	}
	
	private HeatmapRow setHighestRankForProcedure(HeatmapRow row, String procedureName, String callType) {
		Integer oldCall=row.getProcedureSignificance().get(procedureName);
		Integer newCall=this.getRankFromSignificanceName(callType);
		if(newCall>oldCall){
			row.getProcedureSignificance().put(procedureName, newCall);
		}
		return row;
		
	}

	private Integer getRankFromSignificanceName(String significanceString){

		//1 not enough data yet
		//2 not significantly different from WT calls
		//3 high sig
		//4 no data, call it 0 before display as ranks senisbly then
		int monthNumber = 0;

        if (significanceString == null) {
            return 0;
        }

        switch (significanceString.toLowerCase()) {
            case "not significant":
                monthNumber = 2;
                break;
//            case "pending":
//                monthNumber = 4;
//                break;
//            case "not performed or applicable":
//                monthNumber = 4;
//                break;
            case "significant":
                monthNumber = 3;
                break;
            case "early indication of possible phenotype":
                monthNumber = 1;
                break;
            default: 
                monthNumber = 0;
                break;
        }

        return monthNumber;
	    
	}

	/**
	 * Get distinct set of procedure names from the rows to get column headers
	 * @param rows
	 * @return
	 */
	public Set<String> getDistinctProcedureNames(List<HeatmapRow> rows) {
		Set<String> uniqueProcedures = new HashSet<>();
		for (HeatmapRow row : rows) {
			Set<String> procedures = row.getProcedureSignificance().keySet();
			for (String procedure : procedures) {
				if (!uniqueProcedures.contains(procedure)) {
					uniqueProcedures.add(procedure);
				}

			}
		}
		return uniqueProcedures;
	}
	
}
