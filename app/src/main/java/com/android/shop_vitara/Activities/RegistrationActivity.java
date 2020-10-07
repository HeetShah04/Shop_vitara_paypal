package com.android.shop_vitara.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.Model.Country;
import com.android.shop_vitara.Model.PreferenceHelper;
import com.android.shop_vitara.Model.State;
import com.android.shop_vitara.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {
    List<Country> countryList = new ArrayList<>();
    Button register;
    List<State> stateList = new ArrayList<>();
    EditText regi_name, regi_mobileno, regi_email, regi_add1, regi_add2, regi_city, regi_pincode, regi_pwd;
    Spinner regi_spincon, regi_spinstat;
    String contr, stae;
    String name, mno, email, add1, add2, city, pincode, pwd;
    PreferenceHelper preferenceHelper;
    private SoapObject respo, resp;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        register = findViewById(R.id.Register);
        CountrySync sync = new CountrySync();
        sync.execute();
        preferenceHelper = new PreferenceHelper(this);

        regi_name = findViewById(R.id.regi_name);
        regi_mobileno = findViewById(R.id.regi_mobileno);
        regi_email = findViewById(R.id.regi_email);
        regi_add1 = findViewById(R.id.regi_add1);
        regi_add2 = findViewById(R.id.regi_add2);
        regi_city = findViewById(R.id.regi_city);
        regi_pincode = findViewById(R.id.regi_pincode);
        regi_pwd = findViewById(R.id.regi_pwd);
        regi_spincon = findViewById(R.id.regi_spincon);
        regi_spinstat = findViewById(R.id.regi_spinstat);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = regi_name.getText().toString();
                mno = regi_mobileno.getText().toString();
                email = regi_email.getText().toString();
                add1 = regi_add1.getText().toString();
                add2 = regi_add2.getText().toString();
                city = regi_city.getText().toString();
                pincode = regi_pincode.getText().toString();
                pwd = regi_pwd.getText().toString();
                if (name.equals("")) {
                }
                if (mno.equals("")) {

                }
                if (email.equals("")) {

                }
                if (add1.equals("")) {

                }
                if (add2.equals("")) {

                }
                if (city.equals("")) {

                }
                if (pincode.equals("")) {

                }
                if (pwd.equals("")) {

                } else {
                    RegiSync regiSync = new RegiSync();
                    regiSync.execute();
                }
            }
        });
    }

    private void country() {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_COUNTRY);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        try {
            httpTransport.call(AppConfig.ACTION_COUNTRY, envelope);
            respo = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void bindcountry() {
        List<String> cont = new ArrayList<>();
        for (int i = 0; i < respo.getPropertyCount(); i++) {
            Country con = new Country();
            SoapObject obj = (SoapObject) respo.getProperty(i);
            con.setCid(obj.getProperty("Cid").toString());
            con.setContryName(obj.getProperty("CountryName").toString());
            countryList.add(con);
            cont.add(countryList.get(i).getContryName());
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cont);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regi_spincon.setAdapter(adapter);
        regi_spincon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contr = countryList.get(position).getCid();
                StateSync stateSync = new StateSync();
                stateSync.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void state(String qw) {

        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_STATES);

        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setName("cid");
        propertyInfo.setType(android.R.string.class);
        propertyInfo.setValue(qw);
        request.addProperty(propertyInfo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        try {
            httpTransport.call(AppConfig.ACTION_STATE, envelope);
            resp = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindstate() {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < resp.getPropertyCount(); i++) {
            State stat = new State();
            SoapObject obj = (SoapObject) resp.getProperty(i);
            stat.setCid(obj.getProperty("Cid").toString());
            stat.setSid(obj.getProperty("Sid").toString());
            stat.setStateName(obj.getProperty("StateName").toString());
            stateList.add(stat);
            stringList.add(stateList.get(i).getStateName());
        }
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regi_spinstat.setAdapter(adapter1);

        regi_spinstat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stae = stateList.get(position).getSid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String regimaster(String contr, String stae, String city, String name, String mno, String s, String email, String pwd, String add1, String add2, String s1, String pincode, String s2, String s3) {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_REGISTER);

        PropertyInfo pf = new PropertyInfo();
        pf.setName("cid");
        pf.setValue(contr);
        pf.setType(android.R.string.class);
        request.addProperty(pf);

        PropertyInfo p = new PropertyInfo();
        p.setName("sid");
        p.setValue(stae);
        p.setType(android.R.string.class);
        request.addProperty(p);

        PropertyInfo f = new PropertyInfo();
        f.setName("city");
        f.setValue(city);
        f.setType(android.R.string.class);
        request.addProperty(f);

        PropertyInfo r = new PropertyInfo();
        r.setName("name");
        r.setValue(name);
        r.setType(android.R.string.class);
        request.addProperty(r);

        PropertyInfo o = new PropertyInfo();
        o.setName("mobileno");
        o.setValue(mno);
        o.setType(android.R.string.class);
        request.addProperty(o);

        PropertyInfo e = new PropertyInfo();
        e.setName("altmobileno");
        e.setValue(s);
        e.setType(android.R.string.class);
        request.addProperty(e);

        PropertyInfo q = new PropertyInfo();
        q.setName("email");
        q.setValue(email);
        q.setType(android.R.string.class);
        request.addProperty(q);

        PropertyInfo w = new PropertyInfo();
        w.setName("password");
        w.setValue(pwd);
        w.setType(android.R.string.class);
        request.addProperty(w);

        PropertyInfo t = new PropertyInfo();
        t.setName("Address1");
        t.setValue(add1);
        t.setType(android.R.string.class);
        request.addProperty(t);

        PropertyInfo y = new PropertyInfo();
        y.setName("Address2");
        y.setValue(add2);
        y.setType(android.R.string.class);
        request.addProperty(y);

        PropertyInfo u = new PropertyInfo();
        u.setName("Landmark");
        u.setValue(s1);
        u.setType(android.R.string.class);
        request.addProperty(u);

        PropertyInfo i = new PropertyInfo();
        i.setName("Pincode");
        i.setValue(pincode);
        i.setType(android.R.string.class);
        request.addProperty(i);

        PropertyInfo z = new PropertyInfo();
        z.setName("AdditionalAddress1");
        z.setValue(s2);
        z.setType(android.R.string.class);
        request.addProperty(z);

        PropertyInfo x = new PropertyInfo();
        x.setName("AdditionalAddress2");
        x.setValue(s3);
        x.setType(android.R.string.class);
        request.addProperty(x);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        SoapPrimitive response = null;
        try {
            httpTransport.debug = true;
            httpTransport.call(AppConfig.ACTION_REGISTER, envelope);
            response = (SoapPrimitive) envelope.getResponse();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response.toString();
    }

    public class CountrySync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            country();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            bindcountry();
            super.onPostExecute(s);
        }
    }

    public class StateSync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            state(contr);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            bindstate();
            super.onPostExecute(s);
        }
    }

    public class RegiSync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            result = regimaster(contr, stae, city, name, mno, "", email, pwd, add1, add2, "", pincode, "", "");
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result.equals("1")) {
                Toast.makeText(RegistrationActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistrationActivity.this, DrawerActivity.class));
                preferenceHelper.putisLogin(true);
                RegistrationActivity.this.finish();
            }
            super.onPostExecute(s);
        }
    }
}