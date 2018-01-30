package org.immunophenotype.web;


import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.immunophenotype.web.services.GeneService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

@SpringBootApplication
public class Application {

	
	@NotNull
	//@Value("${solr.host}")
	@Value("http://ves-ebi-d0.ebi.ac.uk:8090/mi/impc/dev/solr")
	private String solrBaseUrl;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource.komp2")
    public DataSource komp2DataSource() {
        return DataSourceBuilder.create().driverClassName("com.mysql.jdbc.Driver").build();
    }
	
	@Bean
	public GeneService geneService() {
        return new GeneService();
    }
	
	//Gene
		@Bean(name = "geneCore")
		HttpSolrClient getGeneCore() {
			return new HttpSolrClient(solrBaseUrl + "/gene");
		}

}
