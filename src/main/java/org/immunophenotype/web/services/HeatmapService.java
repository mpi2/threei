package org.immunophenotype.web.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeatmapService {
	
	@Autowired
	DataSource komp2DataSource;
	
	public Map<String,HeatmapRow> getHeatmapRows() throws SQLException{
		Map<String,HeatmapRow> rows=new HashMap<>();
		
		Connection connection = komp2DataSource.getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from threei_data_for_heat_map where Gene='Elac2' order by Gene");
		ResultSet results = statement.executeQuery();
		HeatmapRow row=null;
		while(results.next()){
			
			String gene=results.getString("Gene");
			String construct=results.getString("Construct");
			String procedureName=results.getString("ProcedureName");
			String callType=results.getString("CallType");
			if(!rows.containsKey(gene)){
				row=new HeatmapRow(gene,construct);
			}
			System.out.println("adding result="+results.getString("Gene")+"|"+results.getString("Construct")+"|"+results.getString("ProcedureName")+"|"+results.getString("CallType")+"|"+results.getString("Gender"));
			if(!row.getProcedureSignificance().containsKey(procedureName)){
				row.getProcedureSignificance().put(procedureName, getRankFromSignificanceName(callType));
			}
			//genes.add(results.getString("Gene"));
			//genes.add(results.getString("Construct"));
			
		}
		return rows;
	}
	
	private Integer getRankFromSignificanceName(String significanceString){

		//1 not enough data yet
		//2 not significantly different from WT calls
		//3 high sig
		//4 no data, call it 0 before display as ranks senisbly then
		int monthNumber = 0;

        if (significanceString == null) {
            return monthNumber;
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
                monthNumber = 5;
                break;
            default: 
                monthNumber = 4;
                break;
        }

        return monthNumber;
	    
	}
	
}
