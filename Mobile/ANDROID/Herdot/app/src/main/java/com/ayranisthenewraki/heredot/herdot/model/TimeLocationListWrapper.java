package com.ayranisthenewraki.heredot.herdot.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;

/**
 * Created by idilgun on 07/05/17.
 */

public class TimeLocationListWrapper implements Serializable {

    private List<TimeLocationCouple> tlcList;

    public List<TimeLocationCouple> getTlcList() {
        return tlcList;
    }

    public void setTlcList(List<TimeLocationCouple> tlcList) {
        this.tlcList = tlcList;
    }
}
