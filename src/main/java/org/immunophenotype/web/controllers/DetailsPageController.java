package org.immunophenotype.web.controllers;

import java.util.HashSet;
import java.util.Set;

import org.immunophenotype.web.services.DetailsService;
import org.immunophenotype.web.services.ParameterDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        String accession=detailsService.getAccessionForGene(gene);
        System.out.println("parameters.size="+parameters.size());
        
        Set<String> headers=new HashSet<String>();
        for(ParameterDetails details:parameters){
        	System.out.println("parameterDetails="+details);
        	Set headerKeys=details.getHeaderKeysForParameter();
        	headers.addAll(headerKeys);
        }
        model.addAttribute("accession", accession);
        model.addAttribute("headers", headers);
        model.addAttribute("parameters", parameters);
        model.addAttribute("gene", gene);
        model.addAttribute("procedure", procedure);
        return "procedure";

    }
    
    
//    @RequestMapping(value = "/chart", produces = "text/html;charset=UTF-8")
//    @ResponseBody
//    public void chartPage(HttpServletRequest request,  HttpServletResponse response) {
//
//
////	String paramValue = "param\\with\\backslash";
////	String yourURLStr = "http://host.com?param=" + java.net.URLEncoder.encode(paramValue, "UTF-8");
////	java.net.URL url = new java.net.URL(yourURLStr);
//	try {
//		String queryString=request.getQueryString();
//		String urlString = "https://www.mousephenotype.org/data/charts?phenotyping_center=WTSI&bare=true&accession=MGI:894679&parameter_stable_id=SLM_SLM_016_001";//+queryString;//request.getParameter("url");
//		//response.setHeader("Content-Type", "text/plain");
//		// response.setContentType(“text/xml”);
//		urlString = urlString.replace(" ", "" + "");
//		System.out.println("urlString="+urlString);
//		URLConnection connection = new URL(urlString).openConnection();
//		
//		//String contentEncoding = connection.getHeaderField("Content-Encoding");// should
//																				// be”Content-Encoding”,”ISO-8859-1″
//		// if (contentType.startsWith(“text”)) {
//		// String charset = “ISO-8859-1”;//this encoding is what the registry
//		// uses so must be set here to override default;
//		// System.out.println(“charset=”+charset);
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		BufferedReader reader = null;
//		response.setContentType("text/html;charset=utf-8");
//		PrintWriter writer = response.getWriter();
//		
//		try {
//			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//			for (String line; (line = reader.readLine()) != null;) {
//				
//				writer.println(line);
//				System.out.println(line);
//			}
//		} finally {
//			if (reader != null)
//				try {
//					reader.close();
//				} catch (IOException logOrIgnore) {
//				}
//		}
//
//		reader.close();
//		writer.flush();
//		writer.close();
//	} catch (MalformedURLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	
//
//	}



}
