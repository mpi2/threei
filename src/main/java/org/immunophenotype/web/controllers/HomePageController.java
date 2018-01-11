package org.immunophenotype.web.controllers;

import java.sql.SQLException;

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
			heatmapService.getHeatmapRows();
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
