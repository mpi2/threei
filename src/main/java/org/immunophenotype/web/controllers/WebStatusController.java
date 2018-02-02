package org.immunophenotype.web.controllers;


import org.immunophenotype.web.common.WebStatus;
import org.immunophenotype.web.services.GeneService;
import org.immunophenotype.web.services.HeatmapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Class to handle the nagios web status monitoring pages
 *
 * @author jwarren
 *
 */
@Controller
public class WebStatusController {

	 List<WebStatus> webStatusObjects;
	 
    private final Logger logger = LoggerFactory.getLogger(WebStatusController.class);

    public static final Integer TIMEOUT_INTERVAL = 2;
    
    @Autowired
    HeatmapService heatmapService;
    
    private Model savedModel = null;
    private Integer cacheHit = 0;
    private Integer cacheMiss = 0;
    
    @Autowired
    private GeneService geneService;



    @PostConstruct
    public void initialise() {

        // cores we need to test are at least this set:
        // experiment,genotype-phenotype,statistical-result,preqc,allele,images,impc_images,mp,ma,pipeline,gene,disease,autosuggest
        // System.out.println("calling webStatus initialisation method");
    	webStatusObjects = new ArrayList<>();
    	webStatusObjects.add(heatmapService);
        webStatusObjects.add(geneService);
       
    }
    
    @RequestMapping("/clearCache")
    public synchronized ModelAndView clearCache() {
        savedModel = null;
        return new ModelAndView("redirect:webstatus");


    }
    
    @RequestMapping("/webstatus")
    public synchronized String webStatus(Model model, HttpServletResponse response) {

        if (savedModel != null && savedModel.containsAttribute("webStatusModels") && Math.random() < 0.95) {
            cacheHit += 1;

            model.addAllAttributes(savedModel.asMap());

            model.addAttribute("hits", cacheHit);
            model.addAttribute("misses", cacheMiss);
            model.addAttribute("ratio", cacheHit+cacheMiss>0 ? ((float)cacheHit / (cacheHit+cacheMiss)): "Not available yet");

            return "webStatus";
        }

        logger.info("Updating webstatus model values");
        cacheMiss += 1;

        ExecutorService executor = Executors.newCachedThreadPool();

        boolean ok = true;

        // check our core solr instances are returning via the services
        List<WebStatusModel> webStatusModels = new ArrayList<>();
        for (WebStatus status : webStatusObjects) {

            Future<Long> future = null;

            String name = status.getServiceName();
            long number = 0;
            try {

                // This block causes the method reference getWebStatus to be submitted to the executor
                // And then "get"ted from the future.  If the request is not complete in 2 seconds (more than enough time)
                // then timeout and throw an exception
                Callable<Long> task = status::getWebStatus;
                future = executor.submit(task);
                number = future.get(TIMEOUT_INTERVAL, TimeUnit.SECONDS);

            } catch (Exception e) {

                // Cancel the ongoing call
                if (future!=null) {future.cancel(true);}

                ok = false;

                // Do not cache the model if it is not ok.  This will force the status check
                // to occur on subsequent page loads.
                savedModel = null;

                logger.error("Essential service {} is not available", name);
                e.printStackTrace();
                break;
            }

            WebStatusModel wModel = new WebStatusModel(name, number);
            webStatusModels.add(wModel);
        }

        model.addAttribute("webStatusModels", webStatusModels);

 
       if (ok) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        model.addAttribute("ok", ok);
        
        model.addAttribute("hits", cacheHit);
        model.addAttribute("misses", cacheMiss);
        model.addAttribute("ratio", cacheHit+cacheMiss>0 ? ((float)cacheHit / (cacheHit+cacheMiss)): "Not available yet");

        // If everything is ok, cache the model to be used later
        if (ok) {
            savedModel = model;
        }


        return "webStatus";
    }

    public class WebStatusModel {

        public String name;
        public long number;

        public WebStatusModel(String name, long number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getNumber() {
            return number;
        }

        public void setNumber(long number) {
            this.number = number;
        }

    }

}
