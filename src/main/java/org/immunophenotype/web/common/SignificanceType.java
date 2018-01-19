package org.immunophenotype.web.common;

public enum SignificanceType {

    // 1 not enough data yet
    // 2 not significantly different from WT calls
    // 3 high sig
    // 4 no data call it 0 before display as ranks sensibly then


    not_enough_data("1"),
    not_significant("2"),
    significant("3"),
    no_data("4");

    private final String text;
    SignificanceType(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }

    /**
     * convenience method for bean access from jsp
     * @return
     */
    public String getText(){
        return this.text;
    }

    public static SignificanceType fromString(String text) {
        if (text != null) {
            for (SignificanceType b : SignificanceType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }


}
