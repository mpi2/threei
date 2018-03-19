package org.immunophenotype.web.services;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.immunophenotype.web.common.DisplayProcedureMapper;
import org.immunophenotype.web.common.Result;
import org.immunophenotype.web.common.SexType;
import org.immunophenotype.web.common.SignificanceType;
import org.immunophenotype.web.common.ZygosityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String query = "SELECT ParameterName, ParameterId, Gender, Genotype, CallType FROM threei_data_for_heat_map WHERE Gene = ? AND ProcedureName = ?";
    private DataSource dataSource;

	private GeneService geneService;

    public DetailsService(DataSource dataSource, GeneService geneService) {
        Assert.notNull(dataSource, "Data source cannot be null");
        this.dataSource = dataSource;
        this.geneService=geneService;
    }

    /*
     * procedureName is now the blessed ones from Lucie and so we need to map these back to the real procedure names before giving the page
     */
    public Set<ParameterDetails> getParametersForGeneAndProcedure(String gene, String procedureName) {

        Map<String, ParameterDetails> parameters = new HashMap<>();
        //strip quotes off procedureName
        procedureName=procedureName.replaceAll("\"", "");
        List<String> procedureList=DisplayProcedureMapper.getProceduresFromDisplayName(procedureName);
        
        System.out.println("blah gene"+gene+" procedureName="+procedureList);
        

        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, gene);
            statement.setString(2, procedureName);

            System.out.println(statement);

            ResultSet r = statement.executeQuery();

            while (r.next()) {

                String parameter = r.getString("ParameterName");
                if(parameter==null || parameter.equals("")){
                	parameter="Viability Primary Screen";//seems like all the ones with empty parameter name ar viabiility??
                }

                // If we have not seen this parameter yet, add a new ParameterDetails object to the map
                if ( ! parameters.containsKey(parameter)) {
                    ParameterDetails p = new ParameterDetails();
                    p.setName(parameter);
                    parameters.put(parameter, p);
                }

                ParameterDetails p = parameters.get(parameter);
                String parameterId=r.getString("ParameterId");
                p.setParameterId(parameterId);
                String callType=r.getString("CallType");
                SignificanceType sig=SignificanceType.fromString(callType);
                String gender=r.getString("Gender").toLowerCase();
                System.out.println(callType+" "+gender);
                String zygosity=r.getString("genotype");
         
                Result result=new Result();
                result.setZygosityType(ZygosityType.valueOf(zygosity));
                result.setSignificant(sig);
                if(gender.equals("male")) {
                	result.setSexType(SexType.male);
                    p.addMaleResult(result);
                }

                if(gender.toLowerCase().equals("female")) {
                	result.setSexType(SexType.female);
                    p.addFemaleResult(result);
                   // p.setMaleSignificant(SignificanceType.not_significant);
                }
                if(gender.toLowerCase().equals("both")) {
                	result.setSexType(SexType.both);
                    p.addBothSexResult(result);
                }
                
            }
        } catch (Exception e) {
            logger.info("Sql exception when getting details for gene %s, procedure %s", gene, procedureName, e);
        }

        return new HashSet<>(parameters.values());

    }

	public String getAccessionForGene(String gene) {
		String accession="";
        try {
			accession=this.getMgiAccessionFromGeneSymbol(gene);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SolrServerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("accession in detail service="+accession);
		return accession;
	}
    
    private String getMgiAccessionFromGeneSymbol(String geneSymbol) throws IOException, SolrServerException{
    	return geneService.getMgiAccessionFromGeneSymbol(geneSymbol);
    }





}
