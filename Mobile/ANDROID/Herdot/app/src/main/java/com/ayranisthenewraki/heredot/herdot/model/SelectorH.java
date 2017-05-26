package com.ayranisthenewraki.heredot.herdot.model;

/**
 * Created by idilgun on 26/05/17.
 */

public class SelectorH {
    private String exact;
    private String prefix = "";
    private String type = "TextQuoteSelector";
    private String suffix = "";

    public String getExact() {
        return exact;
    }

    public void setExact(String exact) {
        this.exact = exact;
    }
}
