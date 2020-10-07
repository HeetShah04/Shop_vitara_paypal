package com.android.shop_vitara.Model;

public class State {
    String Sid;
    String Cid;
    String StateName;

    public State() {
    }

    public State(String sid, String cid, String stateName) {
        Sid = sid;
        Cid = cid;
        StateName = stateName;
    }

    public String getSid() {
        return Sid;
    }

    public void setSid(String sid) {
        Sid = sid;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }
}

