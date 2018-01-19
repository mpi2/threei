package org.immunophenotype.web.controllers;

import org.immunophenotype.web.services.DetailsService;
import org.immunophenotype.web.services.ParameterDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        model.addAttribute("parameters", parameters);
        model.addAttribute("gene", gene);
        model.addAttribute("procedure", procedure);
        return "procedure";

    }

}
