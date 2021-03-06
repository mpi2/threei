package org.immunophenotype.web.controllers;


import org.apache.solr.client.solrj.SolrServerException;
import org.immunophenotype.web.services.ConstructService;
import org.immunophenotype.web.services.GeneDTO;
import org.immunophenotype.web.services.GeneService;
import org.immunophenotype.web.services.HeatmapRow;
import org.immunophenotype.web.services.HeatmapService;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class HeatmapController {
	
	JSONArray columnHeadersJson=null;
	Set constructList=null;
	
	List<String> headerOrder=null;
	List<String> columnHeaders=null;
	List<HeatmapRow> rows = null;
	JSONArray heatmap = new JSONArray();
	
	 
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private HeatmapService heatmapService;
    private ConstructService constructService;
	private GeneService geneService;


    public HeatmapController(HeatmapService heatmapService, ConstructService constructService, GeneService geneService) {
        Assert.notNull(heatmapService, "Heatmapservice cannot be null");
        this.heatmapService = heatmapService;
        this.constructService=constructService;
        this.geneService=geneService;
    }


    @RequestMapping("/data")
	public String dataController(Model model, @RequestParam(value = "keywords", required = false) String keyword,
			@RequestParam(value = "construct", required = false) String construct) {
		System.out.println("keywords=" + keyword + " construct=" + construct);
		Map<String, GeneDTO> symbolToGene = new HashMap<>();
		if (keyword != null && !keyword.equals("")) {
			symbolToGene = getGenesForKeywords(keyword, symbolToGene);
		}

		JSONArray filteredHeatmap = new JSONArray();
		List<HeatmapRow> filteredRows = new ArrayList<>();

		// need to get the data from a service so we can replace a data source behind it
		// easily
		if (rows == null || columnHeadersJson == null || constructList == null || headerOrder == null
				|| columnHeaders == null) {

			generateCachedObjectsForAllHeatmap();
			model.addAttribute("heatmap_rows", heatmap);
		} else {

			if (symbolToGene != null && symbolToGene.size() > 0) {

				for (HeatmapRow filteredHeatmapRow : rows) {
					JSONArray heatmapRow = new JSONArray();
					if (symbolToGene.containsKey(filteredHeatmapRow.getGene())) {
						System.out.println("adding row with gene=" + filteredHeatmapRow.getGene());
						// heatmapRow=createJsonArrayforRow(headerOrder, filteredHeatmapRow,
						// heatmapRow);
						filteredRows.add(filteredHeatmapRow);
						// heatmap.put(heatmapRow);
					}
				}
				// heatmap=filteredHeatmap;
			}

			if (construct != null && !construct.equalsIgnoreCase("Any")) {
				List<HeatmapRow> filteredRows2 = new ArrayList<>();
				if (filteredRows.isEmpty()) {
					filteredRows.addAll(rows);
				}
				for (HeatmapRow row : filteredRows) {
					if (construct.equalsIgnoreCase(row.getConstruct())) {
						System.out.println("adding row after construct filter with gene=" + row.getGene());
						filteredRows2.add(row);
						// heatmapRow=createJsonArrayforRow(headerOrder, filteredHeatmapRow,
						// heatmapRow);
						// heatmap.put(heatmapRow);
					}
				}
				// heatmap=filteredHeatmap;
				filteredRows = filteredRows2;
			}

			// if(!filteredRows.size()>0) {
			//
			//
			// }else {
			//
			// }

			for (HeatmapRow filteredHeatmapRow : filteredRows) {
				JSONArray heatmapRow = new JSONArray();
				heatmapRow = createJsonArrayforRow(headerOrder, filteredHeatmapRow, heatmapRow);
				filteredHeatmap.put(heatmapRow);
			}
			model.addAttribute("heatmap_rows", filteredHeatmap);
		}
		
		if((keyword==null || keyword.equals("")) && (construct==null ||construct.equals("Any"))) {
			model.addAttribute("heatmap_rows", heatmap);
		}

		model.addAttribute("columnHeadersJson", columnHeadersJson);

		model.addAttribute("constructlist", constructList);
		return "data";
	}


	private void generateCachedObjectsForAllHeatmap() {
		try {
        	System.out.println("generating cache objects");
            rows = heatmapService.getHeatmapRows();
            columnHeaders = heatmapService.getDistinctProcedureNames(rows);
            headerOrder = HeatmapService.getHeaderorder();
            columnHeadersJson=new JSONArray(headerOrder);

            if(headerOrder.size()==columnHeaders.size()){
            	columnHeaders=headerOrder;//if size the same then lets just convert the ordered column headers to the ones we use
            }else{
                	System.err.println("!!!!!! column headers or order has changed as the number of headers is not the same as the number of ordered headers specified headerOrder.size="+headerOrder.size()+" columnHeaders size="+columnHeaders.size());
                }
            // JSON object representing the rows in the heatmap
            

				for (HeatmapRow row : rows) {

					JSONArray heatmapRow = new JSONArray();

					heatmapRow=createJsonArrayforRow(headerOrder, row, heatmapRow);
					heatmap.put(heatmapRow);

				}

               
                
              constructList = constructService.getConstructs();





        } catch (SQLException e) {
            logger.warn("SQL error occurred when retrieving entries for heatmap", e);
            e.printStackTrace();
        }
	}


	private JSONArray createJsonArrayforRow(List<String> headerOrder, HeatmapRow row, JSONArray heatmapRow) {
		heatmapRow.put(row.getGene()).put(row.getConstruct());
		// put the numbers in for each column header to insure column numbers same and
		// same order

		for (String key : headerOrder) {// use the hard coded header order instead of keys

			// 4 is the constant for "No data"
			Integer call = row.getProcedureSignificance().getOrDefault(key, 4);
			heatmapRow.put(call);
		}
		return heatmapRow;
	}


	private Map<String, GeneDTO> getGenesForKeywords(String keyword, Map<String, GeneDTO> genes) {
		System.out.println("keyword is "+keyword);
    	//now use our gene autosuggest field to get the appropriate gene back
    	//auto_suggest:Adal
    	//http://localhost:8080/data?keyword=4930578F03Rik returns Adal - also need to handle spaces with quotes....!!!
      try {
			genes=geneService.getGeneByKeywords(keyword);
			//use the symbol from the gene returned to request the page with the gene
			//maybe use a redirect?
			
			System.out.println(genes);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return genes;
	}


}
