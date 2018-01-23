package org.immunophenotype.web.common;

public enum SignificanceType {

    // 1 not enough data yet
    // 2 not significantly different from WT calls
    // 3 high sig
    // 4 no data call it 0 before display as ranks sensibly then


    not_enough_data(1, "Not Enough Data"),//grey
    not_significant(2, "Not Significant"),//blue
    significant(3, "Significant"),//red
    no_data(4, "No Data");//white

    private final Integer manualCall;
    private final String label;
    
    public String getLabel() {
		return label;
	}


	SignificanceType(Integer manualCall, String label) {
        this.manualCall = manualCall;
        this.label=label;
    }
    

    public Integer getManualCall() {
        return this.manualCall;
    }
    
    public String getManualCallText() {
        return Integer.toString(this.manualCall);
    }



    public static SignificanceType fromInt(Integer manualCall) {
        if (manualCall != null) {
            for (SignificanceType b : SignificanceType.values()) {
                if (manualCall==b.manualCall) {
                    return b;
                }
            }
        }
        return null;
    }


}
