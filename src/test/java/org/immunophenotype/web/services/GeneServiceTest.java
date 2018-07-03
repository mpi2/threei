package org.immunophenotype.web.services;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppServiceTestConfig.class})
public class GeneServiceTest {
	@Autowired
	GeneService geneService;
	
	@Test
	public void testGetGeneByKeywords() {
		String keywords="interleukin";
		Map<String, GeneDTO> genes=null;
		try {
			genes = geneService.getGeneByKeywords(keywords);
			
			System.out.println("genes="+genes);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(genes.containsKey("Il4i1b"));
		assertTrue(genes.containsKey("Il27"));
	}

}
