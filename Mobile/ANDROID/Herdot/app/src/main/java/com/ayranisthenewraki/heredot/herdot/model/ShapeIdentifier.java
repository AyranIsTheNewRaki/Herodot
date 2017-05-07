package com.ayranisthenewraki.heredot.herdot.model;

import java.io.Serializable;

/**
 * Created by idilgun on 06/05/17.
 */

public class ShapeIdentifier implements Serializable {

    private int id;
    private String identifier;
    private int type;
    private ShapeDetail shape;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ShapeDetail getShape() {
        return shape;
    }

    public void setShape(ShapeDetail shape) {
        this.shape = shape;
    }
}
