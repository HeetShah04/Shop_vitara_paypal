package com.android.shop_vitara.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shop_vitara.Adapter.OrderViewAdapter;
import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.Model.PreferenceHelper;
import com.android.shop_vitara.Model.Subcategory;
import com.android.shop_vitara.R;
import com.android.shop_vitara.paymentintegration.paypalconfig;
import com.bumptech.glide.Glide;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class OrderSummaryFragment extends Fragment {
    private static final String TAG = "sdgsfg";
    RecyclerView rv;
    TextView name, email, mobile, address1, address2, landmark, city_pincode;
    ImageView ordsumProductImage;
    TextView ordsumProductName, ordsumPrice, ordsumMRP, ordsumQty, totamt1, subtot, amtpay, shpcharge, ordersum_no;
    ImageView edit_add;
    String cstid, result;
    CardView cv1;
    int q;
    String type,type1;
    String pid,price1,qty1,tax1,discount1,discpercnt1,mrp1;
    int price, qty, tot,shippingcharge=29,finalamt;
    Dialog dialog;
    String na, mob, ad1, city, pincod;
    Button btn_proceed;
    RadioGroup radioGroup;
    PreferenceHelper preferenceHelper;
    RadioButton radioButton;
    String OrderNo;
    List<Subcategory> subcategoryList = new ArrayList<>();


    //Payment Trial  with paypal
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(paypalconfig.PAYPAL_CLIENT_ID)
            .forceDefaultsOnSandbox(true);
            // The following are only used in PayPalFuturePaymentActivity.
//            .merchantName("Shop_Vitara");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_order_summary, container, false);
        cv1 = view.findViewById(R.id.cv1);
        ordersum_no = view.findViewById(R.id.ordersum_no);
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String date = df.format(Calendar.getInstance().getTime());
        String milli = String.valueOf(Calendar.getInstance().getTimeInMillis());
        OrderNo = "ORD-" + date + "-" + milli;
        ordersum_no.setText("Order No:- " + OrderNo);
        preferenceHelper = new PreferenceHelper(getActivity());
        name = view.findViewById(R.id.ordersum_name);
        subtot = view.findViewById(R.id.subtot);
        amtpay = view.findViewById(R.id.amtpay);
        shpcharge = view.findViewById(R.id.shpcharge);
        email = view.findViewById(R.id.ordersum_email);
        mobile = view.findViewById(R.id.ordersum_mobile);
        address1 = view.findViewById(R.id.ordersum_Address1);
        ordsumMRP = view.findViewById(R.id.ordsumMRP);
        ordsumQty = view.findViewById(R.id.ordsumQty);
        ordsumPrice = view.findViewById(R.id.ordsumPrice);
        ordsumProductName = view.findViewById(R.id.ordsumProductName);
        ordsumProductImage = view.findViewById(R.id.ordsumProductImage);
        totamt1 = view.findViewById(R.id.totamt1);
        address2 = view.findViewById(R.id.ordersum_Address2);
        landmark = view.findViewById(R.id.ordersum_landmark);
        city_pincode = view.findViewById(R.id.ordersum_citynam_pincode);
        rv = view.findViewById(R.id.ordersumrecycler);
        edit_add = view.findViewById(R.id.edit_address);
        cstid = preferenceHelper.getcustomerid(getActivity());
        name.setText("Name: " + preferenceHelper.getcustname(getActivity()));
        email.setText("Email: " + preferenceHelper.getcustemail(getActivity()));
        mobile.setText("Mobile No: " + preferenceHelper.getcustmobile(getActivity()));
        // mobile.setVisibility(View.GONE);
        address1.setText("Address: " + preferenceHelper.getcustadd1(getActivity()));
        // address2.setText(preferenceHelper.getcustadd2(getActivity()));
        address2.setVisibility(View.GONE);
        landmark.setVisibility(View.GONE);
        type="Cart";
        //landmark.setText(preferenceHelper.getcustlandmark(getActivity()));
        city_pincode.setText("City: " + preferenceHelper.getcustcity(getActivity()) + " - " + preferenceHelper.getcustpincode(getActivity()));
        Bundle args = getArguments();
        if (args != null) {
            cv1.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
            type="Buy Now";
            if (args.containsKey("Image")) {
                Glide.with(getActivity()).load(AppConfig.BASE_URL + args.getString("Image").replace("..", "")).into(ordsumProductImage);
            }
            if(args.containsKey("Pid")){
                pid = args.getString("Pid");
            }
            if (args.containsKey("Name")) {
                ordsumProductName.setText(args.getString("Name"));
            }
            if (args.containsKey("Price")) {
                ordsumPrice.setText("Price: ₹ " + args.getString("Price"));
                price = Integer.parseInt(args.getString("Price"));
                price1 =args.getString("Price");
            }
            if (args.containsKey("MRP")) {
                ordsumMRP.setText("MRP: ₹ " + args.getString("MRP"));
                mrp1=args.getString("MRP");
                ordsumMRP.setPaintFlags(ordsumMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (args.containsKey("Qty")) {
                ordsumQty.setText("Qty: " + args.getString("Qty"));
                qty = Integer.parseInt(args.getString("Qty"));
                qty1 = args.getString("Qty");
            }
            if(args.containsKey("Discountpercent")){
                discpercnt1=args.getString("Discountpercent");
            }
            if(args.containsKey("Discount")){
                discount1=args.getString("Discount");
            }
            if(args.containsKey("Tax")) {
                tax1 = args.getString("Tax");
            }
            tot = price * qty;
            finalamt=price+shippingcharge;
            totamt1.setText("Ammount ₹: " + tot);
            subtot.setText("₹" + tot);
            q = 29 * qty;
            shpcharge.setText("₹" + q);
            int total = tot + q;
            amtpay.setText("₹" + total);
        }
        edit_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.row_layout_change_address);
                dialog.setTitle("Update address");

                final EditText nam = dialog.findViewById(R.id.edt_name);
                final EditText mno = dialog.findViewById(R.id.edt_mob);
                final EditText add1 = dialog.findViewById(R.id.edt_add1);
                final EditText cit = dialog.findViewById(R.id.edt_city);
                final EditText pincode = dialog.findViewById(R.id.edt_pincode);

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_updt_add);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        na = nam.getText().toString();
                        mob = mno.getText().toString();
                        ad1 = add1.getText().toString();
                        city = cit.getText().toString();
                        pincod = pincode.getText().toString();
                        updateaddresssync sync = new updateaddresssync();
                        sync.execute();
                    }
                });
                dialog.show();
            }
        });

        //Paypal Integration
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);


        radioGroup = view.findViewById(R.id.radiogrp);
        btn_proceed = view.findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                int selectid = radioGroup.getCheckedRadioButtonId();
                radioButton = view.findViewById(selectid);
                String rgb = radioButton.getText().toString();
                type1 = rgb;
                if (rgb.equalsIgnoreCase("Cash On Delivery")) {
                    orderfinal orderfinal = new orderfinal();
                    orderfinal.execute();

                    Toast.makeText(getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStackImmediate();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.nav_host_fragment, new CheckoutDetailFragment());
                    ft.commit();

                }
                if (rgb.equalsIgnoreCase("Online Payment")) {


                    //Paypal integration trial
                    PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(getContext(), PaymentActivity.class);

                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);

                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        viewcartsync viewcartsync = new viewcartsync();
        viewcartsync.execute();
        return view;
    }
//Paypal INtegration example


    private PayPalPayment getThingToBuy(String paymentIntent) {
        if(type.equalsIgnoreCase("Cart")){
            int a=Integer.parseInt(amtpay.getText().toString().replace("₹ ", ""));
        return new PayPalPayment(new BigDecimal(a), "USD", OrderNo,
                paymentIntent);
        }else{
            return new PayPalPayment(new BigDecimal(finalamt), "USD", "Order Number "+OrderNo,
                    paymentIntent);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        orderfinal orderfinal = new orderfinal();
                        orderfinal.execute();

                        Toast.makeText(getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStackImmediate();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.addToBackStack(null);
                        ft.replace(R.id.nav_host_fragment, new CheckoutDetailFragment());
                        ft.commit();

                    } catch (JSONException e) {
                        Log.e( TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
         }


    @Override
    public void onDestroy() {
        // Stop service when done
        getActivity().stopService(new Intent(getContext(), PayPalService.class));
        super.onDestroy();
    }




    private String viewcart(String cstid) {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.VIEW_CART);
        PropertyInfo pf = new PropertyInfo();
        pf.setName("CSTid");
        pf.setType(android.R.string.class);
        pf.setValue(cstid);
        request.addProperty(pf);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        httpTransport.debug = true;
        SoapPrimitive resp = null;
        try {
            httpTransport.call(AppConfig.ACTION_VIEW_CART, envelope);
            resp = (SoapPrimitive) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resp != null) {
            return resp.toString();
        } else {
            return "null";
        }
    }

    private void adddatainviewcart() {
        try {
            String jsonresponse = result;
            JSONArray jsonArray = new JSONArray(jsonresponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonArrayJSONObject = jsonArray.getJSONObject(i);
                String b = jsonArrayJSONObject.getString("subCategory");
                JSONArray array = new JSONArray(b);
                for (int k = 0; k < array.length(); k++) {
                    Subcategory subcategory = new Subcategory();
                    JSONObject obj = array.getJSONObject(k);
                    subcategory.setPid(obj.getString("Pid"));
                    subcategory.setProductName(obj.getString("ProductName"));
                    subcategory.setPrice(obj.getString("Price"));
                    subcategory.setDiscount(obj.getString("Discount"));
                    subcategory.setProductImages(obj.getString("ProductImages"));
                    subcategory.setMRP(obj.getString("MRP"));
                    subcategory.setQty(obj.getString("Qty"));
                    subcategory.setCartid(obj.getString("Cartid"));
                    subcategory.setDiscPercnt(obj.getString("DiscPercnt"));
                    subcategory.setTax(obj.getString("Tax"));
                    subcategoryList.add(subcategory);
                }
            }
            OrderViewAdapter adapter = new OrderViewAdapter(getActivity(), subcategoryList);
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (Exception e) {

        }
    }

    private String ordermaster(String orderno, String pid, String qty, String price, int shpcharge, int finalamt, String mrp, String discount, String discountpercnt,String tax) {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_ORDERSUMMARY);

        PropertyInfo pf = new PropertyInfo();
        pf.setName("OrderNo");
        pf.setValue(orderno);
        pf.setType(android.R.string.class);
        request.addProperty(pf);

        PropertyInfo p = new PropertyInfo();
        p.setName("Pid");
        p.setValue(pid);
        p.setType(android.R.string.class);
        request.addProperty(p);

        PropertyInfo f = new PropertyInfo();
        f.setName("qty");
        f.setValue(qty);
        f.setType(android.R.string.class);
        request.addProperty(f);

        PropertyInfo r = new PropertyInfo();
        r.setName("price");
        r.setValue(price);
        r.setType(android.R.string.class);
        request.addProperty(r);

        PropertyInfo o = new PropertyInfo();
        o.setName("shippingCharge");
        o.setValue(shpcharge);
        o.setType(android.R.string.class);
        request.addProperty(o);

        PropertyInfo e = new PropertyInfo();
        e.setName("FinalAmt");
        e.setValue(finalamt);
        e.setType(android.R.string.class);
        request.addProperty(e);

        PropertyInfo q = new PropertyInfo();
        q.setName("MRP");
        q.setValue(mrp);
        q.setType(android.R.string.class);
        request.addProperty(q);

        PropertyInfo w = new PropertyInfo();
        w.setName("Discount");
        w.setValue(discount);
        w.setType(android.R.string.class);
        request.addProperty(w);

        PropertyInfo t = new PropertyInfo();
        t.setName("DiscPercnt");
        t.setValue(discountpercnt);
        t.setType(android.R.string.class);
        request.addProperty(t);

        PropertyInfo y = new PropertyInfo();
        y.setName("tax");
        y.setValue(tax);
        y.setType(android.R.string.class);
        request.addProperty(y);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        SoapPrimitive response = null;
        try {
            httpTransport.debug = true;
            httpTransport.call(AppConfig.ACTION_ORDERSUMMARY, envelope);
            response = (SoapPrimitive) envelope.getResponse();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response.toString();
    }

   private String orderaddmaster(int cstid, String paymenttype, String Orderstatus, String PaymentStatus, String Orderno) {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_ORDERADDDETAIL);

        PropertyInfo pf = new PropertyInfo();
        pf.setName("cstid");
        pf.setValue(cstid);
        pf.setType(android.R.string.class);
        request.addProperty(pf);

        PropertyInfo p = new PropertyInfo();
        p.setName("paymenttype");
        p.setValue(paymenttype);
        p.setType(android.R.string.class);
        request.addProperty(p);

        PropertyInfo f = new PropertyInfo();
        f.setName("Orderstatus");
        f.setValue(Orderstatus);
        f.setType(android.R.string.class);
        request.addProperty(f);

        PropertyInfo r = new PropertyInfo();
        r.setName("PaymentStatus");
        r.setValue(PaymentStatus);
        r.setType(android.R.string.class);
        request.addProperty(r);

        PropertyInfo o = new PropertyInfo();
        o.setName("Orderno");
        o.setValue(Orderno);
        o.setType(android.R.string.class);
        request.addProperty(o);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        SoapPrimitive response = null;
        try {
            httpTransport.debug = true;
            httpTransport.call(AppConfig.ACTION_ORDERADDDETAIL, envelope);
            response = (SoapPrimitive) envelope.getResponse();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response.toString();
    }

    private String UpdateAddress(String cstid, String na, String mob, String ad1, String city, String pincod) {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_UPDATEADD);

        PropertyInfo pf = new PropertyInfo();
        pf.setName("CSTid");
        pf.setValue(cstid);
        pf.setType(android.R.string.class);
        request.addProperty(pf);

        PropertyInfo f = new PropertyInfo();
        f.setName("City");
        f.setValue(city);
        f.setType(android.R.string.class);
        request.addProperty(f);

        PropertyInfo r = new PropertyInfo();
        r.setName("Name");
        r.setValue(na);
        r.setType(android.R.string.class);
        request.addProperty(r);

        PropertyInfo o = new PropertyInfo();
        o.setName("Mno");
        o.setValue(mob);
        o.setType(android.R.string.class);
        request.addProperty(o);

        PropertyInfo t = new PropertyInfo();
        t.setName("Add1");
        t.setValue(ad1);
        t.setType(android.R.string.class);
        request.addProperty(t);

        PropertyInfo i = new PropertyInfo();
        i.setName("Pincode");
        i.setValue(pincod);
        i.setType(android.R.string.class);
        request.addProperty(i);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        SoapPrimitive response = null;
        try {
            httpTransport.debug = true;
            httpTransport.call(AppConfig.ACTION_UPDATEADD, envelope);
            response = (SoapPrimitive) envelope.getResponse();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response.toString();
    }

    public class viewcartsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            result = viewcart(cstid);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result.equals("AnyType{}") || result.equals("null")) {
                Toast.makeText(getActivity(), "Something Went Wrong..!!!", Toast.LENGTH_SHORT).show();
            } else {
                adddatainviewcart();
            }
        }
    }

    public class updateaddresssync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            result = UpdateAddress(cstid, na, mob, ad1, city, pincod);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result.equals("1")) {
                Toast.makeText(getContext(), "Address Updated", Toast.LENGTH_SHORT).show();
                preferenceHelper.setcustadd1(ad1);
                preferenceHelper.setcustname(na);
                preferenceHelper.setcustcity(city);
                preferenceHelper.setcustmobile(mob);
                preferenceHelper.setcustpincode(pincod);
                name.setText(na);
                mobile.setText(mob);
                city_pincode.setText(city + "-" + pincod);
                address1.setText(ad1);

            } else {
                Toast.makeText(getActivity(), "Something Went Wrong..!!!", Toast.LENGTH_SHORT).show();
            }
            dialog.cancel();
        }
    }

    public String deletecart(String cstid) {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_EMPTYCART);
        PropertyInfo pf = new PropertyInfo();
        pf.setName("cstid");
        pf.setValue(cstid);
        pf.setType(android.R.string.class);
        request.addProperty(pf);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        httpTransport.debug = true;
        SoapPrimitive response = null;
        try {
            httpTransport.call(AppConfig.ACTION_EMPTYCART, envelope);
            response = (SoapPrimitive) envelope.getResponse();
        } catch (Exception soapFault) {
            soapFault.printStackTrace();
        }
        return response.toString();
    }

    public class orderfinal extends AsyncTask<String,Void,String>{
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Processing Order for Checkout...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(type.equalsIgnoreCase("Cart"))
            {for(int i=0;i<subcategoryList.size();i++){
                result ="0";
                int shppingcharge = 29;
                int qty=Integer.parseInt(subcategoryList.get(i).getQty());
                int shpcharge = shppingcharge*qty;
                int pric = Integer.parseInt(subcategoryList.get(i).getPrice());
                int famt = pric*qty;
                result = ordermaster(OrderNo,subcategoryList.get(i).getPid(),subcategoryList.get(i).getQty(),subcategoryList.get(i).getPrice() ,shpcharge, famt, subcategoryList.get(i).getMRP(),subcategoryList.get(i).getDiscount(),subcategoryList.get(i).getDiscPercnt(),subcategoryList.get(i).getTax());
            }}else{
                result = ordermaster(OrderNo,pid,qty1,price1 ,shippingcharge, finalamt, mrp1,discount1,discpercnt1,tax1);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result.equals("1")) {
                addorder addorder = new addorder();
                addorder.execute();
                emptycart emptycart=new emptycart();
                emptycart.execute();
            }
            dialog.dismiss();
        }
    }

    public class emptycart extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            result = "0";
            result = deletecart(cstid);
            return result;
        }
    }

    public class addorder extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            result= orderaddmaster(Integer.parseInt(cstid),type1,"Success","Success",OrderNo);
            return result;
        }
    }

}