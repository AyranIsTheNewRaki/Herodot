package com.ayranisthenewraki.heredot.herdot.model;

import java.util.List;

/**
 * Created by idilgun on 26/05/17.
 */

public class myTarget {

    private String source;
    private List<SelectorH> selector;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<SelectorH> getSelector() {
        return selector;
    }

    public void setSelector(List<SelectorH> selector) {
        this.selector = selector;
    }
}
