package org.immunophenotype.web.services;


import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Configuration
@Component
public class AppServiceTestConfig {

	
	 	@Value("${datasource.komp2.url}")
	    private String datasourceKomp2Url;

	    @Value("${datasource.komp2.username}")
	    private String username;

	    @Value("${datasource.komp2.password}")
	    private String password;
	    
	    @NotNull
		//@Value("${solr.host}")
		@Value("http://ves-ebi-d0.ebi.ac.uk:8090/mi/impc/dev/solr")
		private String solrBaseUrl;

	    
	@Bean
	@Primary
    @ConfigurationProperties(prefix = "datasource.komp2")
    public  DataSource komp2DataSource() {
        //return DataSourceBuilder.create().driverClassName("com.mysql.jdbc.Driver").build();
		System.out.println("initialising with dataSource.komp2.url: " + datasourceKomp2Url+" username="+username +" password="+password);
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create().driverClassName("com.mysql.jdbc.Driver");
        dataSourceBuilder.url(datasourceKomp2Url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();   
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
