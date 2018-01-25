package org.immunophenotype.web.controllers;

import org.immunophenotype.web.services.DetailsService;
import org.immunophenotype.web.services.ParameterDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class DetailsPageController {

    private DetailsService detailsService;

    public DetailsPageController(DetailsService detailsService) {
        this.detailsService = detailsService;
    }

    @RequestMapping("/link")
    public String linkPage(Model model,
                           @RequestParam("gene") String gene,
                           @RequestParam("procedure") String procedure) {

        Set<ParameterDetails> parameters = detailsService.getParametersForGeneAndProcedure(gene, procedure);
        System.out.println("parameters.size="+parameters.size());
        
        Set<String> headers=new HashSet<String>();
        for(ParameterDetails details:parameters){
        	System.out.println("parameterDetails="+details);
        	Set headerKeys=details.getHeaderKeysForParameter();
        	headers.addAll(headerKeys);
        }
        model.addAttribute("headers", headers);
        model.addAttribute("parameters", parameters);
        model.addAttribute("gene", gene);
        model.addAttribute("procedure", procedure);
        return "procedure";

    }

}
