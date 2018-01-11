package org.immunophenotype.web.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.immunophenotype.web.services.HeatmapRow;
import org.immunophenotype.web.services.HeatmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {

	@Autowired
	HeatmapService heatmapService;
	
    @GetMapping("/")
    public String homeController(Model model){ return  "index"; }
    @RequestMapping("/methods")
    public String methodController(Model model){
        return  "methods";
    }

    @RequestMapping("/analysis")
    public String analysisController(Model model){
        return  "analysis";
    }

    @RequestMapping("/contact")
    public String contactController(Model model){
        return  "contact";
    }

    @RequestMapping("/data")
    public String dataController(Model model){
    	System.out.println("calling data page");
    	//need to get the data from a service so we can replace a data source behind it easily
    	
    	try {
			 List<HeatmapRow> rows = heatmapService.getHeatmapRows();
			 String heatmapRows="[";//string to hold the javascript variable that is going to be passed to the data page for the data
			 //having got the rows we need distinct list of procedures so we know what column headers we will have and what order to display the calls
			 Set<String>columnHeaders=heatmapService.getDistinctProcedureNames(rows);
			 for(HeatmapRow row:rows){
				//System.out.println(row);
				heatmapRows+="[\""+row.getGene()+"\",\""+row.getConstruct()+"\""+",";
						for(String key:columnHeaders){//put the numbers in for each column header to insure column numbers same and same order
							Integer call=4;//default no data
							if(row.getProcedureSignificance().containsKey(key)){
								call=row.getProcedureSignificance().get(key);
							}
							
							heatmapRows+=call.toString()+",";
						}
						
				heatmapRows+= "]";
				
			}
			 heatmapRows+= ",]";
			 
			 model.addAttribute("columnHeaders",columnHeaders);
			 System.out.println("columnHeaders.size="+columnHeaders.size());
			 model.addAttribute("rows",rows);
			 
			 System.out.println("rows.size="+rows.size());
			 
			 model.addAttribute("heatmap_rows", heatmapRows);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
        return  "data";
    }

    @RequestMapping("/learnmore")
    public String learnmoreController(Model model){ return  "learnmore"; }

    @RequestMapping("/faq")
    public String faqController(Model model){ return  "faq"; }

    @RequestMapping("/glossary")
    public String glossaryController(Model model){ return  "glossary"; }

    @RequestMapping("/publications")
    public String publicationsController(Model model){ return  "publications"; }

    @RequestMapping("/contactform")
    public String contactformController(Model model){ return  "contactform"; }

    @RequestMapping("/3i-consortium-meeting")
    public String consortiummeetingController(Model model){ return  "3i-consortium-meeting"; }

    @RequestMapping("/wgc-advanced-course-immunophenotyping")
    public String wgcController(Model model){return "wgc-advanced-course-immunophenotyping";}

    @RequestMapping("/image-acknowledgements")
    public String imageackController(Model model){return "image-acknowledgements";}

    @RequestMapping("/phenotypeofthemonth")
    public String PhenotypeController(Model model){return "phenotypeofthemonth";}


}
