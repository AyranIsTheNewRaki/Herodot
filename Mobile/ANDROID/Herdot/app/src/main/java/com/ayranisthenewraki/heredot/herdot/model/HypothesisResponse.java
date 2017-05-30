package com.ayranisthenewraki.heredot.herdot.model;

import java.lang.annotation.Target;
import java.util.List;

/**
 * Created by idilgun on 27/05/17.
 */

public class HypothesisResponse {

    private String id;

    private String text;

    private myTarget[] target;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public myTarget[] getTarget() {
        return target;
    }

    public void setTarget(myTarget[] target) {
        this.target = target;
    }
}
