package org.immunophenotype.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {

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
        return  "data";
    }

    @RequestMapping("/learnmore")
    public String learnmoreController(Model model){ return  "learnmore"; }


}
