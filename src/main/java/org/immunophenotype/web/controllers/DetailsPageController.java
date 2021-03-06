package org.immunophenotype.web.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.immunophenotype.web.common.Result;
import org.immunophenotype.web.common.SexType;
import org.immunophenotype.web.common.SignificanceType;
import org.immunophenotype.web.common.ZygosityType;
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
                           @RequestParam("construct") String construct,
                           @RequestParam("procedure") String procedure) {

        Set<ParameterDetails> parameters = detailsService.getParametersForGeneAndDisplayName(gene,construct, procedure);
        String accession=detailsService.getAccessionForGene(gene);
//        System.out.println("parameters.size="+parameters.size());
//        System.out.println("parameters="+parameters);

		Set<String> headers = generateUniqueColumnHeaders(parameters);
		// now we need to just keep the most signficant hits for each column header and
		// store these in a row object with blanks where non exist for table display and
		// column alignment

		List<ParameterRow> rows=new ArrayList<>();
		for (ParameterDetails param : parameters) {
			ParameterRow row=new ParameterRow(param.getName(), param.getParameterId());
			for (String header : headers) {
				boolean headerFound = false;
				List<Result> resultsForParam = param.getMostSignificantResults();
				for (Result result : resultsForParam) {
					if (header.equalsIgnoreCase(result.getHeaderKey())) {
						//System.out.println("headerkey found");
						headerFound = true;
						row.addResult(result);

					}
				}
				if(!headerFound) {
					Result blankResult=new Result();
					blankResult.setSignificant(SignificanceType.no_data);
					String[] sexNZyg=header.split(" ");
					blankResult.setSexType(SexType.getByDisplayName(sexNZyg[0]));
					blankResult.setZygosityType(ZygosityType.valueOf(sexNZyg[1]));
					row.addResult(blankResult);
				}
			}
			rows.add(row);
		}
        
        
        model.addAttribute("rows", rows);
        //System.out.println("headers="+headers);
        model.addAttribute("accession", accession);
        model.addAttribute("headers", headers);
        model.addAttribute("parameters", parameters);
        model.addAttribute("gene", gene);
        model.addAttribute("construct", construct);
        model.addAttribute("procedure", procedure);
        return "procedure";

    }

	private Set<String> generateUniqueColumnHeaders(Set<ParameterDetails> parameters) {
		Set<String> headers=new TreeSet<String>();
        for(ParameterDetails details:parameters){
        	//System.out.println("parameterDetails="+details);
        	Set<String> headerKeys=details.getHeaderKeysForParameter();
        	headers.addAll(headerKeys);
        }
		return headers;
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
