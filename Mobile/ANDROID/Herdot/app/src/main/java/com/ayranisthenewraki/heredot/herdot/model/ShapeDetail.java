package com.ayranisthenewraki.heredot.herdot.model;

import java.io.Serializable;

/**
 * Created by idilgun on 06/05/17.
 */

public class ShapeDetail implements Serializable {

    private Center center;
    private Double radius;

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
