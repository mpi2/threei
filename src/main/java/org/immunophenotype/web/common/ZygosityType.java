package org.immunophenotype.web.common;

public enum ZygosityType {
	homozygote,
	heterozygote,
	hemizygote,
	not_applicable;
	
	public String getName(){
		return this.toString();
	}

	public String getShortName(){
		
		if (this.getName().equals(not_applicable.toString())){
			return "N/A";
		} else {
			return this.toString().substring(0, 3).toUpperCase();
		}
	}
}
