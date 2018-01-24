package org.immunophenotype.web.services;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.immunophenotype.web.common.SignificanceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class DetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String query = "SELECT ParameterName, Gender, Genotype, CallType FROM threei_data_for_heat_map WHERE Gene = ? AND ProcedureName = ?";
    private DataSource dataSource;

    public DetailsService(DataSource dataSource) {
        Assert.notNull(dataSource, "Data source cannot be null");
        this.dataSource = dataSource;
    }

    public Set<ParameterDetails> getParametersForGeneAndProcedure(String gene, String procedureName) {

        Map<String, ParameterDetails> parameters = new HashMap<>();
        //strip quotes off procedureName
        procedureName=procedureName.replaceAll("\"", "");
        System.out.println("gene"+gene+" procedureName="+procedureName);
        

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

                String callType=r.getString("CallType");
                SignificanceType sig=SignificanceType.fromString(callType);
                String gender=r.getString("Gender").toLowerCase();
                System.out.println(callType+" "+gender);
               
                if(gender.equals("male")) {
                    p.setMaleSignificant(sig);
                    p.setFemaleSignificant(SignificanceType.not_significant);
                }

                if(gender.toLowerCase().equals("female")) {
                    p.setFemaleSignificant(sig);
                    p.setMaleSignificant(SignificanceType.not_significant);
                }
                if(gender.toLowerCase().equals("both")) {
                    p.setFemaleSignificant(sig);
                    p.setMaleSignificant(sig);
                }

            }
        } catch (Exception e) {
            logger.info("Sql exception when getting details for gene %s, procedure %s", gene, procedureName, e);
        }

        return new HashSet<>(parameters.values());

    }





}
