package org.immunophenotype.web.services;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.immunophenotype.web.common.DisplayProcedureMapper;
import org.immunophenotype.web.common.SignificanceType;
import org.immunophenotype.web.common.WebStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class HeatmapService implements WebStatus{

	//public static final String [] headerArray={	"Viability Primary Screen", "Fertility of Homozygous Knock-out Mice",  "Haematology",	"Buffy coat peripheral blood leukocyte immunophenotyping", "Whole blood peripheral blood leukocyte immunophenotyping", "Spleen Immuno Phenotyping", 	"Mesenteric Lymph Node Immunophenotyping", 	"Bone marrow immunophenotyping", "Ear epidermis immunophenotyping",	"Anti-nuclear antibody assay", "Antigen Specific Immunoglobulin Assay", "CTL assay", "3i DSS Challenge", "Infection Challenge Weights",	"3i Trichurus Challenge",	"Salmonella Challenge"	};
			
	
	public static final List<String> headerOrder =  new ArrayList<>(Arrays.asList(DisplayProcedureMapper.getDisplayHeaderOrder())); 
	
    public static List<String> getHeaderorder() {
		return headerOrder;
	}

	private DataSource dataSource;

    public HeatmapService(DataSource dataSource) {
        Assert.notNull(dataSource, "Datasource cannot be null");
        this.dataSource = dataSource;
    }

	public List<HeatmapRow> getHeatmapRows() throws SQLException {
		//key should be gene_construct now as we want a row per this combination
		Map<String, HeatmapRow> rows = new HashMap<>();
		//"select Gene, Construct, ProcedureName, CallType from threei.threei_data_for_heat_map where Gene='herc1'";
		String query = "select Gene, Construct, ProcedureName, CallType from threei_data_for_heat_map order by Gene";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement p = connection.prepareStatement(query)) {
			ResultSet results = p.executeQuery();

			while (results.next()) {

				String gene = results.getString("Gene");
				String construct = results.getString("Construct");
				String key=gene +"_"+construct;
				if (construct.contains("(")) {
					String[] constructs = construct.split("\\(");
					construct = constructs[0];
				}

				HeatmapRow row = null;
				if (!rows.containsKey(key)) {
					row = new HeatmapRow(gene, construct);
					rows.put(key, row);
				} else {
					row = rows.get(key);
				}
				//System.out.println("|"+results.getString("ProcedureName")+"|");
				String procedureName = DisplayProcedureMapper.getDisplayNameForProcedure(results.getString("ProcedureName"));
				String callType = results.getString("CallType");

				// System.out.println("adding
				// result="+results.getString("Gene")+"|"+results.getString("Construct")+"|"+results.getString("ProcedureName")+"|"+results.getString("CallType")+"|"+results.getString("Gender"));
				if (!row.getProcedureSignificance().containsKey(procedureName)) {
					row.getProcedureSignificance().put(procedureName, getRankFromSignificanceName(callType));// set
																								// new
																												// procedure
				} else {
					// assign the highest rank for a the procedure
					row = this.setHighestRankForProcedure(row, procedureName, callType);
				}

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

       return  SignificanceType.getRankFromSignificanceName(significanceString);

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

	@Override
	public long getWebStatus() throws SQLException {
		String query = "select count(Gene) from threei_data_for_heat_map";

		try (Connection connection = dataSource.getConnection();
				PreparedStatement p = connection.prepareStatement(query)) {

			ResultSet results = p.executeQuery();
			long numberOfGenes = 0;
			while (results.next()) {
				numberOfGenes = results.getLong(1);
			}

			return numberOfGenes;
		}
	}

	@Override
	public String getServiceName() {
		
		return "HeatmapService";
	}

}
