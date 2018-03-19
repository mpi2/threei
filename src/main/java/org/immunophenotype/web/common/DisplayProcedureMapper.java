package org.immunophenotype.web.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DisplayProcedureMapper {
	
	private static final Map<String, String> realProcedureTodDisplayName= DisplayProcedureMapper.createMap();//for use internally as a couple of procedure display names map to more than one procedure so use getDisplayName method externally 
	private static final String[] displayHeaderOrder={ 
			"Homozygous viability at P14",
	         "Homozygous Fertility",
	        "Haematology",
	         "Peripheral Blood Leukocytes",
	       "Spleen",
	         "Mesenteric Lymph Node",
	       "Bone Marrow",
	        "Ear Epidermis",
	        "Anti-nuclear Antibodies",
	        "Salmonella Challenge",
	        "Cytotoxic T Cell Function",
	        "DSS Challenge",
	         "Influenza",
	        "Trichuris Challenge",
	        "Salmonella Challenge"};
	
			
			
			
			private static Map<String,String> createMap() {
	
        Map<String, String> aMap = new HashMap<>();
        aMap.put("Viability Primary Screen", "Homozygous viability at P14");
        aMap.put("Fertility of Homozygous Knock-out Mice", "Homozygous Fertility");
        aMap.put("Haematology", "Haematology");
        aMap.put("Buffy coat peripheral blood leukocyte immunophenotyping", "Peripheral Blood Leukocytes");
        aMap.put("Whole blood peripheral blood leukocyte immunophenotyping", "Peripheral Blood Leukocytes");
        aMap.put("Spleen Immuno Phenotyping","Spleen");
        aMap.put("Mesenteric Lymph Node Immunophenotyping", "Mesenteric Lymph Node");
        aMap.put("Bone marrow immunophenotyping", "Bone Marrow");
        aMap.put("Ear epidermis immunophenotyping", "Ear Epidermis");
        aMap.put("Anti-nuclear antibody assay","Anti-nuclear Antibodies");
        aMap.put("Antigen Specific Immunoglobulin Assay", "Salmonella Challenge");
        aMap.put("Salmonella Challenge", "Salmonella Challenge");
        aMap.put("CTL assay","Cytotoxic T Cell Function");
        aMap.put("3i DSS Challenge","DSS Challenge");
        aMap.put("Infection Challenge Weights", "Influenza");
        aMap.put("3i Trichurus Challenge","Trichuris Challenge");
        return Collections.unmodifiableMap(aMap);
    }
		public static String getDisplayNameForProcedure(String procedureName){
			if(realProcedureTodDisplayName.containsKey(procedureName)){
			realProcedureTodDisplayName.get(procedureName);
			}else{
				System.err.println("procedureName not found in Real Procedure to display Map "+procedureName);
			}
			return realProcedureTodDisplayName.get(procedureName);
		}
	
	
	public static String[] getDisplayHeaderOrder(){
		return displayHeaderOrder;
	}
//	private class ProcedureBean{
//		
//		
//		
//	}
}
