package org.immunophenotype.web;


import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.h2.server.web.WebServlet;
import org.immunophenotype.web.services.GeneService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

@SpringBootApplication
public class Application {

	private final static Integer INITIAL_POOL_CONNECTIONS = 1; 
	@NotNull
	@Value("${solr.host}")
	//@Value("http://ves-ebi-d0.ebi.ac.uk:8090/mi/impc/dev/solr")
	private String solrBaseUrl;
	
	@Value("${datasource.komp2.url}")
	String komp2Url;
	@Value("${datasource.komp2.username}")
	String komp2Username;
	@Value("${datasource.komp2.password}")
	String komp2Password;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	
	@Bean
	@Primary
	//@ConfigurationProperties(prefix = "datasource.komp2")
	public DataSource komp2DataSource() {
		return Application.getConfiguredDatasource(komp2Url, komp2Username, komp2Password);
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
		
		public static DataSource getConfiguredDatasource(String url, String username, String password) {
	        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
	        ds.setUrl(url);
	        ds.setUsername(username);
	        ds.setPassword(password);
	        ds.setDriverClassName("com.mysql.jdbc.Driver");
	        ds.setInitialSize(INITIAL_POOL_CONNECTIONS);
	        ds.setMaxActive(100);
	        ds.setMinIdle(INITIAL_POOL_CONNECTIONS);
	        ds.setMaxIdle(INITIAL_POOL_CONNECTIONS);
	        ds.setTestOnBorrow(true);
	        ds.setValidationQuery("SELECT 1");
	        ds.setValidationInterval(5000);
	        ds.setMaxAge(30000);
	        ds.setMaxWait(35000);
	        ds.setTestWhileIdle(true);
	        ds.setTimeBetweenEvictionRunsMillis(5000);
	        ds.setMinEvictableIdleTimeMillis(5000);
	        ds.setValidationInterval(30000);
	        ds.setRemoveAbandoned(true);
	        ds.setRemoveAbandonedTimeout(10000); // 10 seconds before abandoning a query

	        try {
	            System.out.println("Using database {} with initial pool size {}. URL: {}"+ ds.getConnection().getCatalog()+ ds.getInitialSize()+url);

	        } catch (Exception e) {

	            System.err.println(e.getLocalizedMessage());
	            e.printStackTrace();
	        }

	        return ds;
	    }

}
