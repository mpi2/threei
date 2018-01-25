package org.immunophenotype.web.common;

public enum ZygosityType {
	
	Het,
	Hom,
	Hemi,
	Mutant;
	
	public String getName(){
		return this.toString();
	}

	
}
