package org.immunophenotype.web.common;

public enum SignificanceType {

    // 1 not enough data yet
    // 2 not significantly different from WT calls
    // 3 high sig
    // 4 no data call it 0 before display as ranks sensibly then


    not_enough_data(1),
    not_significant(2),
    significant(3),
    no_data(4);

    private final Integer manualCall;
    SignificanceType(Integer manualCall) {
        this.manualCall = manualCall;
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
