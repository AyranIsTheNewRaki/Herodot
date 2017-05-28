package com.ayranisthenewraki.heredot.herdot.model;

import android.bluetooth.BluetoothGattServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.text.Normalizer;

/**
 * Created by idilgun on 06/05/17.
 */

public class TimeLocationCouple implements Serializable {

    private String name;
    private String time;
    private String timeType;
    private ShapeIdentifier shape;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public ShapeIdentifier getShape() {
        return shape;
    }

    public void setShape(ShapeIdentifier shape) {
        this.shape = shape;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(this).replace("\"","'");
        json = Normalizer.normalize(json, Normalizer.Form.NFD);
        return json;
    }
}
