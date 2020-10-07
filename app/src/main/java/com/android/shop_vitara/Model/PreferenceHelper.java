package com.android.shop_vitara.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private static SharedPreferences app_prefs;
    Context context;

    public PreferenceHelper(Context context) {
        this.context = context;
        app_prefs = context.getSharedPreferences("shared", Context.MODE_PRIVATE);
    }

    public static void putisLogin(Boolean loginout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean("Login", loginout);
        edit.apply();
        edit.commit();
    }

    public static void setcustname(String name) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("Name", name);
        edit.apply();
        edit.commit();
    }

    public static void setcustemail(String email) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("Email", email);
        edit.apply();
        edit.commit();
    }

    public static void setcustomerid(String cstid) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("Customer_id", cstid);
        edit.apply();
        edit.commit();
    }

    public static void setcustmobile(String mob) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("Mobile", mob);
        edit.apply();
        edit.commit();
    }

    public static void setcustadd1(String add1) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("Address1", add1);
        edit.apply();
        edit.commit();
    }

    public static void setcustadd2(String add2) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("Address2", add2);
        edit.apply();
        edit.commit();
    }

    public static void setcustlandmark(String landmark) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("Landmark", landmark);
        edit.apply();
        edit.commit();
    }

    public static void setcustpincode(String pincode) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("Pincode", pincode);
        edit.apply();
        edit.commit();
    }

    public static void setcustcity(String city) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString("City", city);
        edit.apply();
        edit.commit();
    }

    public boolean getisLogin() {
        return app_prefs.getBoolean("Login", false);
    }

    public String getcustname(Context ctx) {
        return app_prefs.getString("Name", "");
    }

    public String getcustemail(Context ctx) {
        return app_prefs.getString("Email", "");
    }

    public String getcustomerid(Context ctx) {
        return app_prefs.getString("Customer_id", "");
    }

    public String getcustmobile(Context ctx) {
        return app_prefs.getString("Mobile", "");
    }

    public String getcustadd1(Context ctx) {
        return app_prefs.getString("Address1", "");
    }

    public String getcustadd2(Context ctx) {
        return app_prefs.getString("Address2", "");
    }

    public String getcustlandmark(Context ctx) {
        return app_prefs.getString("Landmark", "");
    }

    public String getcustpincode(Context ctx) {
        return app_prefs.getString("Pincode", "");
    }

    public String getcustcity(Context ctx) {
        return app_prefs.getString("City", "");
    }
}
