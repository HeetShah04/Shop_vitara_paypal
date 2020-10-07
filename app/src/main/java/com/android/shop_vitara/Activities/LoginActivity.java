package com.android.shop_vitara.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.Model.CustomerDetail;
import com.android.shop_vitara.Model.PreferenceHelper;
import com.android.shop_vitara.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    List<CustomerDetail> lst = new ArrayList<>();
    EditText log_mno, log_pwd;
    Button btn_log;
    Button btn_signup;
    String mno, pwd;
    PreferenceHelper preferenceHelper;
    private SoapObject response;
    TextView txtforgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_login);
        log_mno = findViewById(R.id.log_mno);
        log_pwd = findViewById(R.id.log_pwd);
        btn_log = findViewById(R.id.btn_log);
        preferenceHelper = new PreferenceHelper(LoginActivity.this);
        txtforgotpassword=findViewById(R.id.txtforgotpassword);
        txtforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));

            }
        });
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mno = log_mno.getText().toString();
                pwd = log_pwd.getText().toString();
                if (mno.equals("")) {

                }
                if (pwd.equals("")) {

                } else {
                    LoginSync loginSync = new LoginSync();
                    loginSync.execute();
                }
            }
        });
        btn_signup = findViewById(R.id.btn_sign_up);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                LoginActivity.this.finish();
            }
        });
    }

    private void loginMaster(String mno, String pwd) {

        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_LOGIN);

        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setName("mno");
        propertyInfo.setValue(mno);
        propertyInfo.setType(android.R.string.class);
        request.addProperty(propertyInfo);

        PropertyInfo pf = new PropertyInfo();
        pf.setName("pwd");
        pf.setValue(pwd);
        pf.setType(android.R.string.class);
        request.addProperty(pf);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        try {
            httpTransport.debug = true;
            httpTransport.call(AppConfig.ACTION_LOGIN, envelope);
            response = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void binddata() {
        for (int i = 0; i < response.getPropertyCount(); i++) {

            SoapObject obj = (SoapObject) response.getProperty(i);
            CustomerDetail cd = new CustomerDetail();
            cd.setCSTid(obj.getProperty("CSTid").toString());
            cd.setCid(obj.getProperty("Cid").toString());
            cd.setSid(obj.getProperty("Sid").toString());
            cd.setCity(obj.getProperty("City").toString());
            cd.setName(obj.getProperty("Name").toString());
            cd.setEmail(obj.getProperty("Email").toString());
            cd.setMobileNo(obj.getProperty("MobileNo").toString());
            cd.setAltMobileNo(obj.getProperty("AltMobileNo").toString());
            cd.setAddress1(obj.getProperty("Address1").toString());
            cd.setAddress2(obj.getProperty("Address2").toString());
            cd.setAdditionalAddress1(obj.getProperty("AdditionalAddress1").toString());
            cd.setAdditionalAddress2(obj.getProperty("AdditionalAddress2").toString());
            cd.setLandmark(obj.getProperty("Landmark").toString());
            cd.setPincode(obj.getProperty("Pincode").toString());
            cd.setPasword(obj.getProperty("Password").toString());
            lst.add(cd);
        }
        preferenceHelper.setcustname(lst.get(0).getName());
        preferenceHelper.setcustomerid(lst.get(0).getCSTid());
        preferenceHelper.setcustemail(lst.get(0).getEmail());
        preferenceHelper.setcustadd1(lst.get(0).getAddress1());
        preferenceHelper.setcustadd2(lst.get(0).getAddress2());
        preferenceHelper.setcustlandmark(lst.get(0).getLandmark());
        preferenceHelper.setcustpincode(lst.get(0).getPincode());
        preferenceHelper.setcustmobile(lst.get(0).getMobileNo());
        preferenceHelper.setcustcity(lst.get(0).getCity());

    }

    public class LoginSync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            loginMaster(mno, pwd);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            int Count = response.getPropertyCount();
            if (Count > 0) {
                binddata();
                startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
                LoginActivity.this.finish();
                preferenceHelper.putisLogin(true);
            }
        }
    }
}