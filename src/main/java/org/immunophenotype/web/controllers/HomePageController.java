package org.immunophenotype.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Calendar;

@Controller
public class HomePageController {

    @GetMapping("/")
    public String homeController(Model model) {

        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);
        return "index";
    }

    @RequestMapping("/methods")
    public String methodController(Model model) {

        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);return "methods";
    }

    @RequestMapping("/analysis")
    public String analysisController(Model model) {

        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);return "analysis";
    }

    @RequestMapping("/contact")
    public String contactController(Model model) {

        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);return "contact";
    }

    @RequestMapping("/learnmore")
    public String learnmoreController(Model model) {

        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);return "learnmore";
    }

    @RequestMapping("/faq")
    public String faqController(Model model) {

        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);return "faq";
    }

    @RequestMapping("/glossary")
    public String glossaryController(Model model) {
        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);
        return "glossary";
    }

    @RequestMapping("/publications")
    public String publicationsController(Model model) {
        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);
        return "publications";
    }

    @RequestMapping("/contactform")
    public String contactformController(Model model) {

        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);return "contactform";
    }

    @RequestMapping("/3i-consortium-meeting")
    public String consortiummeetingController(Model model) {

        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);return "3i-consortium-meeting";
    }

    @RequestMapping("/wgc-advanced-course-immunophenotyping")
    public String wgcController(Model model) {

        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);return "wgc-advanced-course-immunophenotyping";
    }

    @RequestMapping("/image-acknowledgements")
    public String imageackController(Model model) {

        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);return "image-acknowledgements";
    }

    @RequestMapping(value="/phenotypeofthemonth")
    public String PhenotypeController(Model model) {

        String phenotype = getPheno();
        return phenotype;

    }

    @RequestMapping("/privacypolicy")
    public String PrivacypolicyController(Model model) {

        String phenotype= getPheno();

        model.addAttribute("phenotypeofthemonth", phenotype);
        return "privacypolicy";
    }



    public String getPheno(){

        String phenotype = "";
        int month;
        month = Calendar.getInstance().get(Calendar.MONTH)+1;

        switch (month){
            case 1: phenotype = "Adgrd1"; break;
            case 2: phenotype = "Bivm";break;
            case 3: phenotype =  "Chd9";break;
            case 4: phenotype =  "Clpp";break;
            case 5: phenotype =  "Cxcr2";break;
            case 6: phenotype = "Gimap6";break;
            case 7: phenotype = "Gmds";break;
            case 8: phenotype = "Paf";break;
            case 9: phenotype = "Peptidase";break;
            case 10: phenotype = "Setd5"; break;
            case 11: phenotype = "Wdtc1";break;
            case 12: phenotype = "Zfp408"; break;
            default: phenotype = "phenotypeofthemonth";

        }

        return phenotype;
    }



}
