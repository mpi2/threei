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

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.List;


@Controller
public class HeatmapController {
	
	JSONArray heatmap = null;
	JSONArray columnHeadersJson=null;
	Set constructList=null;
	 
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private HeatmapService heatmapService;
    private ConstructService constructService;
	private GeneService geneService;
	private List<GeneDTO> genes;

    public HeatmapController(HeatmapService heatmapService, ConstructService constructService, GeneService geneService) {
        Assert.notNull(heatmapService, "Heatmapservice cannot be null");
        this.heatmapService = heatmapService;
        this.constructService=constructService;
        this.geneService=geneService;
    }


    @RequestMapping("/data")
    public String dataController(Model model) {

        //need to get the data from a service so we can replace a data source behind it easily
    	if(heatmap==null || columnHeadersJson == null || constructList == null || genes==null){
    		
        try {
        	heatmap = new JSONArray();
            List<HeatmapRow> rows = heatmapService.getHeatmapRows();
            List<String> columnHeaders = heatmapService.getDistinctProcedureNames(rows);
            List<String> headerOrder = HeatmapService.getHeaderorder();
            columnHeadersJson=new JSONArray(headerOrder);
//            try {
//				this.genes=geneService.getGeneDtosForWtsi();
//			} catch (SolrServerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            if(headerOrder.size()==columnHeaders.size()){
            	columnHeaders=headerOrder;//if size the same then lets just convert the ordered column headers to the ones we use
            }else{
                	System.err.println("!!!!!! column headers or order has changed as the number of headers is not the same as the number of ordered headers specified headerOrder.size="+headerOrder.size()+" columnHeaders size="+columnHeaders.size());
                }
            // JSON object representing the rows in the heatmap
            

            for (HeatmapRow row : rows) {

                JSONArray heatmapRow = new JSONArray();
                heatmapRow
                        .put(row.getGene())
                        .put(row.getConstruct());
                
               

                // put the numbers in for each column header to insure column numbers same and same order
                
                for (String key : headerOrder) {//use the hard coded header order instead of keys

                    // 4 is the constant for "No data"
                    Integer call = row.getProcedureSignificance().getOrDefault(key, 4);
                    heatmapRow.put(call);
                }
                heatmap.put(heatmapRow);
                }
                
              constructList = constructService.getConstructs();





        } catch (SQLException e) {
            logger.warn("SQL error occurred when retrieving entries for heatmap", e);
            e.printStackTrace();
        }
    }
    	model.addAttribute("columnHeadersJson", columnHeadersJson);
        model.addAttribute("heatmap_rows", heatmap);
        model.addAttribute("constructlist",constructList);
        return "data";
    }


}
