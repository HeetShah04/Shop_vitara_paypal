package com.android.shop_vitara.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shop_vitara.Activities.LoginActivity;
import com.android.shop_vitara.Adapter.OrderHistoryAdapter;
import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.Model.OrderHistory;
import com.android.shop_vitara.Model.PreferenceHelper;
import com.android.shop_vitara.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {


    PreferenceHelper preferenceHelper;
    RecyclerView rv;
    String cstid;
    List<OrderHistory> lst = new ArrayList<>();
    LinearLayout linearnoorders;
    private SoapObject response;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        linearnoorders = view.findViewById(R.id.linearNoOrders);
        rv = view.findViewById(R.id.recyclerMyOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        preferenceHelper = new PreferenceHelper(getActivity());
        Boolean a = preferenceHelper.getisLogin();
        if (a) {
            cstid = preferenceHelper.getcustomerid(getActivity());
            orderhistsync histsync = new orderhistsync();
            histsync.execute();
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        return view;
    }

    public void orderhist(String cstid) {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_ORDER_HISTORY);

        PropertyInfo pf = new PropertyInfo();
        pf.setName("CSTid");
        pf.setType(android.R.string.class);
        pf.setValue(cstid);
        request.addProperty(pf);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        try {
            httpTransport.debug = true;
            httpTransport.call(AppConfig.ACTION_ORDER_HISTORY, envelope);
            response = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }

    private void bindData() {
        for (int i = 0; i < response.getPropertyCount(); i++) {

            SoapObject obj = (SoapObject) response.getProperty(i);
            OrderHistory bd = new OrderHistory();
            bd.setOrderDate(obj.getProperty("OrderDate").toString());
            bd.setOrderNo(obj.getProperty("OrderNo").toString());
            bd.setOrderStatus(obj.getProperty("OrderStatus").toString());
            bd.setPaymentStatus(obj.getProperty("PaymentStatus").toString());
            bd.setPaymentType(obj.getProperty("PaymentType").toString());
            lst.add(bd);
        }
        OrderHistoryAdapter adapter = new OrderHistoryAdapter(getActivity(), lst);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class orderhistsync extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Loading!!!!");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            orderhist(cstid);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (response.getPropertyCount() > 0) {
                linearnoorders.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                bindData();
            } else {
                linearnoorders.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
            dialog.dismiss();
        }
    }
}