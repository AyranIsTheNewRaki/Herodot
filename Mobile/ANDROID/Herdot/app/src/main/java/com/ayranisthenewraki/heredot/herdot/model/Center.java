package com.ayranisthenewraki.heredot.herdot.model;

import java.io.Serializable;

/**
 * Created by idilgun on 06/05/17.
 */

public class Center implements Serializable {

    private Double lat;
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
