package org.immunophenotype.web.services;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.immunophenotype.web.common.DisplayProcedureMapper;
import org.immunophenotype.web.common.WebStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.SortedSet;

import java.util.TreeSet;

@Service
public class ConstructService {

    private DataSource dataSource;

    public ConstructService(DataSource dataSource) {
        Assert.notNull(dataSource, "Datasource cannot be null");
        this.dataSource = dataSource;
    }



    public SortedSet<String> getConstructs() throws SQLException {

        SortedSet<String> constructlist = new TreeSet<>();

        String query = "select DISTINCT Construct from threei_data_for_heat_map  ";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement p = connection.prepareStatement(query)) {
            ResultSet results = p.executeQuery();

            while (results.next()) {

                String construct = results.getString("Construct");
                if (construct.contains("(")) {
                    String[] constructs = construct.split("\\(");
                    construct = constructs[0];
                }
                constructlist.add(construct.trim());

            }
        }
        return constructlist;

    }

}

