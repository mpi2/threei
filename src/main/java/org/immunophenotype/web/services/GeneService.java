package org.immunophenotype.web.services;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.immunophenotype.web.common.WebStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GeneService implements WebStatus{

	
	@Autowired
	@Qualifier("geneCore")
	private SolrClient solr;
	public String getMgiAccessionFromGeneSymbol(String geneSymbol) throws IOException, SolrServerException {

		if (geneSymbol == null) {
			System.err.println("null entered for gene symbol to get accession");
			return null;
		}

		SolrQuery query = new SolrQuery();
		query.setQuery(GeneDTO.MARKER_SYMBOL + ":" + geneSymbol+"  OR "+ GeneDTO.MARKER_SYNONYM+":"+geneSymbol);
		query.setRows(Integer.MAX_VALUE);
		query.setFields(GeneDTO.MARKER_SYMBOL, GeneDTO.MGI_ACCESSION_ID);
		
		QueryResponse rsp = solr.query(query);

		List<GeneDTO> genes = rsp.getBeans(GeneDTO.class);
		if(genes.size()==1){
			return genes.get(0).getAccession();
		}else{
			System.err.println("blah too many or too few genes returned from 3i solr service");
			return "";
		}

	}





	@Override
	public long getWebStatus() throws Exception {
		SolrQuery query = new SolrQuery();

		query.setQuery("*:*").setRows(0);

		//System.out.println("SOLR URL WAS " + SolrUtils.getBaseURL(solr) + "/select?" + query);

		QueryResponse response = solr.query(query);
		return response.getResults().getNumFound();
	}





	@Override
	public String getServiceName() {
		return "GeneService";
	}





	public Map<String, GeneDTO> getGeneByKeywords(String keyword) throws SolrServerException, IOException {
		//all 3i data is from WTSI so filter genes on latest_phenotyping_centre:WTSI

		SolrQuery query = new SolrQuery();
		query.setQuery("auto_suggest"+":"+"\""+keyword+"\"");
		query.addFilterQuery("latest_phenotyping_centre" + ":" + "WTSI");//all 3i are WTSI phenotyping center??
		query.setRows(Integer.MAX_VALUE);
		
		QueryResponse rsp = solr.query(query);

		List<GeneDTO> genes = rsp.getBeans(GeneDTO.class);
		if(genes.size()>0){
			Map<String, GeneDTO> geneSymbolToGene=new HashMap<>();
			for(GeneDTO gene: genes) {
				geneSymbolToGene.put(gene.getMarkerSymbol(), gene);
			}
			
			return geneSymbolToGene;
		}else{
			System.err.println("too few genes returned from 3i solr service for keywords");
			return null;
		}
		
	}
	
	
	//geneQf use this field for gene, synonym and human gene info. Seems to work with lower and upper case mixes
	public Map<String, GeneDTO> getGeneSymbolOrSynonymOrNameOrHuman(String keyword) throws SolrServerException, IOException {
		//all 3i data is from WTSI so filter genes on latest_phenotyping_centre:WTSI

		SolrQuery query = new SolrQuery();
		query.setQuery("geneQf"+":"+"\""+keyword+"\"");
		query.addFilterQuery("latest_phenotyping_centre" + ":" + "WTSI");//all 3i are WTSI phenotyping center??
		query.setRows(Integer.MAX_VALUE);
		
		QueryResponse rsp = solr.query(query);

		List<GeneDTO> genes = rsp.getBeans(GeneDTO.class);
		if(genes.size()>0){
			Map<String, GeneDTO> geneSymbolToGene=new HashMap<>();
			for(GeneDTO gene: genes) {
				geneSymbolToGene.put(gene.getMarkerSymbol(), gene);
			}
			
			return geneSymbolToGene;
		}else{
			System.err.println("too few genes returned from 3i gene solr service for keywords");
			return null;
		}
		
	}
    
}
