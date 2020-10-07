package com.android.shop_vitara.Model;

public class Country {
    String Cid;
    String ContryName;

    public Country() {
    }

    public Country(String contryName) {
        ContryName = contryName;
    }

    public Country(String cid, String contryName) {
        Cid = cid;
        ContryName = contryName;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getContryName() {
        return ContryName;
    }

    public void setContryName(String contryName) {
        ContryName = contryName;
    }


}
