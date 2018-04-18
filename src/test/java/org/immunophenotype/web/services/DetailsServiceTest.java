package org.immunophenotype.web.services;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.immunophenotype.web.common.DisplayProcedureMapper;
import org.immunophenotype.web.common.SexType;
import org.immunophenotype.web.common.SignificanceType;
import org.immunophenotype.web.common.ZygosityType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppServiceTestConfig.class})
public class DetailsServiceTest {
	
	@Autowired
	DataSource komp2DataSource;//make sure this points to the threei db not the komp2 one as not in there anymore. datasource.komp2 property can be set in the env tab in eclipse
	
	@Autowired
	GeneService geneService;
	
	DetailsService detailsService;
	@Before
	public void setUp() throws Exception {
       this.detailsService=new DetailsService((org.apache.tomcat.jdbc.pool.DataSource) komp2DataSource, geneService);
	}

	@Test
	public final void testGetParametersForGeneAndProcedure() {
		Set<ParameterDetails> details = detailsService.getParametersForGeneAndProcedure("Nxn", "CTL assay");
		System.out.println("details size="+details.size());
		assertTrue(details.size()>0);
		for(ParameterDetails parameter :details){
		assertTrue(parameter.getMaleResults().get(0).getZygosityType().equals(ZygosityType.Het));
		assertTrue(parameter.getMaleResults().get(0).getSignificant().equals(SignificanceType.not_significant));
		}
		//one 
		Set<ParameterDetails> details2 = detailsService.getParametersForGeneAndDisplayName("Nxn", "Cytotoxic T cell function");
		System.out.println("details size="+details.size());
		assertTrue(details.size()>0);
		assertTrue(details.size()==details2.size());
		
	}
	
	@Test
	public final void testGetParametersForGeneAndDisplayName() {
		Set<ParameterDetails> details = detailsService.getParametersForGeneAndDisplayName("Nxn", "CTL assay");
		System.out.println("details size="+details.size());
		assertTrue(details.size()>0);
		for(ParameterDetails parameter :details){
		assertTrue(parameter.getMaleResults().get(0).getZygosityType().equals(ZygosityType.Het));
		assertTrue(parameter.getMaleResults().get(0).getSignificant().equals(SignificanceType.not_significant));
		}
	}
	
	@Test
	public final void testGetAccessionForGene() {
		String accession = detailsService.getAccessionForGene("Nxn");
		System.out.println("accession="+accession);
		assertTrue(accession.equals("MGI:109331"));
		
	}
	
	@Test
	public final void testGetProceduresFromDisplayNameSalmonella(){
		Set<ParameterDetails> parameterDetails=detailsService.getParametersForGeneAndDisplayName("Trmt2a","Salmonella Challenge");
		for(ParameterDetails paramDtail: parameterDetails){	
			System.out.println(paramDtail);
		}
		assertTrue(parameterDetails.size()==2);
	}

}
