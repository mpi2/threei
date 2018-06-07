package org.immunophenotype.web.services;

import static org.junit.Assert.*;

import java.util.HashSet;
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
		//uses real procedureName
		Set<ParameterDetails> details = detailsService.getParametersForGeneAndProcedure("Nxn", "tm1b","CTL assay");
		System.out.println("details size="+details.size());
		assertTrue(details.size()>0);
		for(ParameterDetails parameter :details){
		assertTrue(parameter.getMaleResults().get(0).getZygosityType().equals(ZygosityType.Het));
		assertTrue(parameter.getMaleResults().get(0).getSignificant().equals(SignificanceType.not_significant));
		}
		
		
	}
	
	@Test
	public final void testGetCTLDisplayNameResults(){
		//uses display name
				Set<ParameterDetails> details2 = detailsService.getParametersForGeneAndDisplayName("Nxn", "tm1b","Cytotoxic T cell function");	
				Set<String> paramNames=new HashSet<>();
				for(ParameterDetails detail:details2){
					System.out.println("detail for ctl="+detail.getName());
					paramNames.add(detail.getName());
				}
				//we had a bug where ctl would be showing parameters for fluffy coat so lets make sure it doesn't happen again
				assertTrue(paramNames.contains("% CD4+"));//this isn't a ctl param
	}
	
	@Test
	public final void testGetAccessionForGene() {
		String accession = detailsService.getAccessionForGene("Nxn");
		System.out.println("accession="+accession);
		assertTrue(accession.equals("MGI:109331"));
		
	}
	
	@Test
	public final void testGetProceduresFromDisplayNameSalmonella(){
		Set<ParameterDetails> parameterDetails=detailsService.getParametersForGeneAndDisplayName("Trmt2a","tm2b","Salmonella Challenge");
		for(ParameterDetails paramDtail: parameterDetails){	
			System.out.println("paramdetail="+paramDtail);
		}
		assertTrue(parameterDetails.size()>=1);
	}
	
	@Test
	public final void testGetProceduresFromDisplayNameTrmt2a() {
		// for all the procedures for this gene Trmt2a there is at least one
		// result apart from influenza and Trichuris Challenge and should always
		// be?
		for (String displayHeader : DisplayProcedureMapper.getDisplayHeaderOrder()) {
			Set<ParameterDetails> parameterDetails = detailsService.getParametersForGeneAndDisplayName("Trmt2a", "tm2b",
					displayHeader);
			System.out.println(displayHeader + " details size is " + parameterDetails.size());
			if (displayHeader.equalsIgnoreCase("influenza")) {
				assertTrue(parameterDetails.size() == 1);
			} else if (displayHeader.equalsIgnoreCase("Trichuris Challenge")) {
				assertFalse(parameterDetails.size() > 0);
			} else {
				assertTrue(parameterDetails.size() > 0);
			}
		}
	}
	
	@Test
	public void testGetParametersForGeneAndDisplayNamePeripheralBloodLeukocytes(){
		//This procedure has special rules in that if there is data for "whole blood.." use that only when not use "Buffy coat..." parameters.
		Set<ParameterDetails> parameterDetails = detailsService.getParametersForGeneAndDisplayName("Trmt2a","tm2b",
				"Peripheral Blood Leukocytes");
		System.out.println("parameters size="+parameterDetails.size());
		assertTrue(parameterDetails.size()>0);
		
	}
	
	@Test
	public final void testGetDataForAllColumns(){
		
		//http://localhost:8080/link?gene=Cxcr2&procedure=%22Mesenteric%20Lymph%20Node&construct=%22tm1a%22
		Set<ParameterDetails> parameterDetails=detailsService.getParametersForGeneAndDisplayName("Cxcr2","tm1a","Mesenteric Lymph Node");
		for(ParameterDetails paramDetail: parameterDetails){	
			System.out.println("paramdetail="+paramDetail);
			//System.out.println("all results       ="+paramDetail.getResults());
			if(paramDetail.getResults().size()>0) {
				System.out.println("size is "+paramDetail.getResults().size());
			}
			//System.out.println("significant sorted="+paramDetail.getMostSignificantResults());
		}
		assertTrue(parameterDetails.size()>=1);
	}
	
	

}
