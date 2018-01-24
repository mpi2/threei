package org.immunophenotype.web.services;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.sql.DataSource;

import org.immunophenotype.web.common.SexType;
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
	DataSource komp2DataSource;
	
	DetailsService detailsService;
	@Before
	public void setUp() throws Exception {
       this.detailsService=new DetailsService((org.apache.tomcat.jdbc.pool.DataSource) komp2DataSource);
	}

	@Test
	public final void testGetParametersForGeneAndProcedure() {
		Set<ParameterDetails> details = detailsService.getParametersForGeneAndProcedure("Nxn", "CTL Assay");
		assertTrue(details.size()==3);//3 rows currently
		for(ParameterDetails parameter :details){
		//asserTrue(parameter.getResults().getBySex(SexType.male).equals(ZygosityType.heterozygote));
		}
	}
	

}
