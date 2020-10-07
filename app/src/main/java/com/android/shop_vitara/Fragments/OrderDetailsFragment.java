package com.android.shop_vitara.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shop_vitara.Adapter.OrderDetailAdapter;
import com.android.shop_vitara.Adapter.OrderHistoryAdapter;
import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.Model.OrderHistory;
import com.android.shop_vitara.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsFragment extends Fragment {
    String orderno;
    RecyclerView orderdetailrecycler;
    private SoapObject response;
List<OrderHistory> orderHistoryList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        orderdetailrecycler=view.findViewById(R.id.orderdetailrecycler);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        orderdetailrecycler.setLayoutManager(layoutManager);
        Bundle args=getArguments();
        if(args!=null && args.containsKey("Orderno")){
           orderno= args.getString("Orderno");
           Orderdetailsync orderdetailsync=new Orderdetailsync();
           orderdetailsync.execute();
        }
        return view;
    }

    public class Orderdetailsync extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            orderdetail(orderno);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
           binddata();
        }
    }

    public void orderdetail(String orderno) {
        SoapObject request=new SoapObject(AppConfig.SOAP_URL,AppConfig.SOAP_ORDER_HISTORY_DETAIL);
        PropertyInfo pf=new PropertyInfo();
        pf.setName("OrderNo");
        pf.setType(android.R.string.class);
        pf.setValue(orderno);
        request.addProperty(pf);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        try {
            httpTransport.debug = true;
            httpTransport.call(AppConfig.ACTION_ORDER_HISTORY_DETAIL, envelope);
            response = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }
    public void binddata(){
        for (int i = 0; i < response.getPropertyCount(); i++) {

            SoapObject obj = (SoapObject) response.getProperty(i);
            OrderHistory bd = new OrderHistory();
            bd.setOdid(obj.getProperty("Odid").toString());
            bd.setOrderNo(obj.getProperty("OrderNo").toString());
            bd.setPid(obj.getProperty("Pid").toString());
            bd.setPrice(obj.getProperty("Price").toString());
            bd.setShippingCharge(obj.getProperty("ShippingCharge").toString());
            bd.setFinalamt(obj.getProperty("Finalamt").toString());
            bd.setMRP(obj.getProperty("MRP").toString());
            bd.setDiscount(obj.getProperty("Discount").toString());
            bd.setDiscPercent(obj.getProperty("DiscPercent").toString());
            bd.setTax(obj.getProperty("Tax").toString());
            bd.setQty(obj.getProperty("Qty").toString());
            bd.setProductName(obj.getProperty("ProductName").toString());
            orderHistoryList.add(bd);
        }
        OrderDetailAdapter adapter=new OrderDetailAdapter(getActivity(),orderHistoryList);
        orderdetailrecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}