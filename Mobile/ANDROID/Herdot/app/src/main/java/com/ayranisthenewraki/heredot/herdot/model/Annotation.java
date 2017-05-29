package com.ayranisthenewraki.heredot.herdot.model;


import java.util.List;

/**
 * Created by idilgun on 26/05/17.
 */

public class Annotation {

    private String group = "__world__";

    private Permissions permissions = new Permissions();

    private List<myTarget> target;

    private String text;

    private String uri;

    public List<myTarget> getTarget() {
        return target;
    }

    public void setTarget(List<myTarget> target) {
        this.target = target;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
