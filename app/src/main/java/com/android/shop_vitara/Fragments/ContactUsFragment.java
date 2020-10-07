package com.android.shop_vitara.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class ContactUsFragment extends Fragment {

    TextView edtFirstName,edtLastName,edtEmail,edtMobileNumber,edtSubject,edtMessage;
    Button btnSubmit;
    String fname,lname,msg,mno,sub,email,result;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        edtFirstName=view.findViewById(R.id.edtFirstName);
        edtLastName=view.findViewById(R.id.edtLastName);
        edtEmail=view.findViewById(R.id.edtEmail);
        edtMobileNumber=view.findViewById(R.id.edtMobileNumber);
        edtSubject=view.findViewById(R.id.edtSubject);
        edtMessage=view.findViewById(R.id.edtMessage);
        btnSubmit=view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             fname=edtFirstName.getText().toString();
             lname=edtLastName.getText().toString();
             msg=edtMessage.getText().toString();
             sub=edtSubject.getText().toString();
             email=edtEmail.getText().toString();
             mno=edtMobileNumber.getText().toString();
             contactussync sync=new contactussync();
             sync.execute();
            }
        });
        return view;
    }

    public class contactussync extends AsyncTask<String,Void,String>{
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog=new ProgressDialog(getActivity());
            dialog.setTitle("Sending!!");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            result=Contactus(fname,lname,mno,email,sub,msg);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result.equals("1")){
                Toast.makeText(getActivity(), "Message Sent Successfully...!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "Something Went Wrong..!!!", Toast.LENGTH_SHORT).show();
            }
            dialog.cancel();
        }
    }

    private String Contactus(String fname, String lname, String mno, String email, String sub, String msg) {
        SoapObject request=new SoapObject(AppConfig.SOAP_URL,AppConfig.SOAP_CONTACTUS);
        PropertyInfo pf=new PropertyInfo();
        pf.setName("fname");
        pf.setType(android.R.string.class);
        pf.setValue(fname);
        request.addProperty(pf);

      PropertyInfo p=new PropertyInfo();
        p.setName("lname");
        p.setType(android.R.string.class);
        p.setValue(lname);
        request.addProperty(p);

      PropertyInfo pf1=new PropertyInfo();
        pf1.setName("mno");
        pf1.setType(android.R.string.class);
        pf1.setValue(mno);
        request.addProperty(pf1);

      PropertyInfo pf2=new PropertyInfo();
        pf2.setName("email");
        pf2.setType(android.R.string.class);
        pf2.setValue(email);
        request.addProperty(pf2);

      PropertyInfo pf3=new PropertyInfo();
        pf3.setName("sub");
        pf3.setType(android.R.string.class);
        pf3.setValue(sub);
        request.addProperty(pf3);

      PropertyInfo pf4=new PropertyInfo();
        pf4.setName("msg");
        pf4.setType(android.R.string.class);
        pf4.setValue(msg);
        request.addProperty(pf4);

        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        SoapPrimitive respo = null;
        try {
            httpTransport.call(AppConfig.ACTION_CONTACTUS, envelope);
            respo = (SoapPrimitive) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respo.toString();
    }
}