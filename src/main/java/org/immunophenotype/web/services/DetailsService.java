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

    private String query = "SELECT ParameterName, Gender, Genotype, ManualCallId FROM threei_data_for_heat_map WHERE Gene = ? AND ProcedureId = ?";
    private DataSource dataSource;

    public DetailsService(DataSource dataSource) {
        Assert.notNull(dataSource, "Data source cannot be null");
        this.dataSource = dataSource;
    }

    public Set<ParameterDetails> getParametersForGeneAndProcedure(String gene, String procedureId) {

        Map<String, ParameterDetails> parameters = new HashMap<>();


        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, gene);
            statement.setString(2, procedureId);

            System.out.println(statement);

            ResultSet r = statement.executeQuery();

            while (r.next()) {

                String parameter = r.getString("ParameterName");

                // If we have not seen this parameter yet, add a new ParameterDetails object to the map
                if ( ! parameters.containsKey(parameter)) {
                    ParameterDetails p = new ParameterDetails();
                    p.setName(parameter);
                    parameters.put(parameter, p);
                }

                ParameterDetails p = parameters.get(parameter);

                if(r.getString("Gender").toLowerCase().equals("male")) {
                    p.setMaleSignificant(SignificanceType.fromString(r.getString("ManualCallId")));
                }

                if(r.getString("Gender").toLowerCase().equals("female")) {
                    p.setFemaleSignificant(SignificanceType.fromString(r.getString("ManualCallId")));
                }

            }
        } catch (Exception e) {
            logger.info("Sql exception when getting details for gene %s, procedure %s", gene, procedureId, e);
        }

        return new HashSet<>(parameters.values());

    }





}
