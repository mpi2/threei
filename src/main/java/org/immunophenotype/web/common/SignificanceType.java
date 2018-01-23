package org.immunophenotype.web.common;

public enum SignificanceType {

    // 1 not enough data yet
    // 2 not significantly different from WT calls
    // 3 high sig
    // 4 no data call it 0 before display as ranks sensibly then


    not_significant("Not Significant"),//grey
    pending("Pending"),//blue
    significant("Significant"),//red
    no_data("Not performed or applicable"),
    early_indication("Early indication of possible phenotype");//white
	
    private final String label;
    
    public String getLabel() {
		return label;
	}


	SignificanceType(String label) {
        this.label=label;
    }



    public static SignificanceType fromString(String manualCall) {
        if (manualCall != null) {
            for (SignificanceType b : SignificanceType.values()) {
                if (manualCall.equalsIgnoreCase(b.label)) {
                    return b;
                }
            }
        }
        return no_data;
    }


}
