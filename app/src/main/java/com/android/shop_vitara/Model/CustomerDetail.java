package com.android.shop_vitara.Model;

public class CustomerDetail {
    String CSTid;
    String Cid;
    String Sid;
    String City;
    String Name;
    String MobileNo;
    String AltMobileNo;
    String Email;
    String Pasword;
    String Address1;
    String Address2;
    String Landmark;
    String Pincode;
    String AdditionalAddress1;
    String AdditionalAddress2;

    public CustomerDetail(String CSTid, String cid, String sid, String city, String name, String mobileNo, String altMobileNo, String email, String pasword, String address1, String address2, String landmark, String pincode, String additionalAddress1, String additionalAddress2) {
        this.CSTid = CSTid;
        Cid = cid;
        Sid = sid;
        City = city;
        Name = name;
        MobileNo = mobileNo;
        AltMobileNo = altMobileNo;
        Email = email;
        Pasword = pasword;
        Address1 = address1;
        Address2 = address2;
        Landmark = landmark;
        Pincode = pincode;
        AdditionalAddress1 = additionalAddress1;
        AdditionalAddress2 = additionalAddress2;
    }

    public CustomerDetail() {
    }

    public String getCSTid() {
        return CSTid;
    }

    public void setCSTid(String CSTid) {
        this.CSTid = CSTid;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getSid() {
        return Sid;
    }

    public void setSid(String sid) {
        Sid = sid;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getAltMobileNo() {
        return AltMobileNo;
    }

    public void setAltMobileNo(String altMobileNo) {
        AltMobileNo = altMobileNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPasword() {
        return Pasword;
    }

    public void setPasword(String pasword) {
        Pasword = pasword;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getAdditionalAddress1() {
        return AdditionalAddress1;
    }

    public void setAdditionalAddress1(String additionalAddress1) {
        AdditionalAddress1 = additionalAddress1;
    }

    public String getAdditionalAddress2() {
        return AdditionalAddress2;
    }

    public void setAdditionalAddress2(String additionalAddress2) {
        AdditionalAddress2 = additionalAddress2;
    }
}
