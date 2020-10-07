package com.android.shop_vitara.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.shop_vitara.Activities.LoginActivity;
import com.android.shop_vitara.Adapter.CartViewAdapter;
import com.android.shop_vitara.Adapter.ProductImageAdapter;
import com.android.shop_vitara.Adapter.SubCategoryAdapter;
import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.Model.PreferenceHelper;
import com.android.shop_vitara.Model.ProductImage;
import com.android.shop_vitara.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDescriptionFragment extends Fragment {

    private static int currentPage = 0;
    private static int NUM_PAGE = 0;
    ViewPager viewPager;
    TextView txtProductName, txtPrice, txtMRP, txtDiscountpercent, txtDescription,txtProductCount;
    String pid;
    ImageView imgminus,imgplus;
    List<ProductImage> productImageList = new ArrayList<>();
    private SoapObject resp;
    Button addtocart;
PreferenceHelper helper;
    private String cstid,qty;
    private String result;
LinearLayout linearAddQuantity;
int counter=1;
    public ProductDescriptionFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_description, container, false);
        txtProductName = view.findViewById(R.id.txtProductName);
        txtPrice = view.findViewById(R.id.txtPrice);
        txtMRP = view.findViewById(R.id.txtMRP);
        txtDiscountpercent = view.findViewById(R.id.txtDiscountpercent);
        txtDescription = view.findViewById(R.id.txtDescription);
        viewPager=view.findViewById(R.id.pager);
        addtocart=view.findViewById(R.id.btnAddToCart);
        imgplus = view.findViewById(R.id.imgPlus);
        imgminus=view.findViewById(R.id.imgMinus);
        txtProductCount=view.findViewById(R.id.txtProductCount);
        linearAddQuantity=view.findViewById(R.id.linearAddQuantity);
        txtProductCount.setText(""+counter);
        linearAddQuantity.setVisibility(View.VISIBLE);
        imgplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter>0){
                    counter = ++counter;
                    txtProductCount.setText(""+counter);
                }
            }
        });
        imgminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter>1){
                    counter=--counter;
                    txtProductCount.setText(""+counter);
                }
            }
        });
        helper=new PreferenceHelper(getActivity());
        Bundle args = getArguments();
        if (args != null) {

            if (args.containsKey("Pid")) {
                String a = args.getString("Pid");
                pid = String.valueOf(a);
                productimagesync sync = new productimagesync();
                sync.execute();
                final Handler handler = new Handler();
                final Runnable update = new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == NUM_PAGE) {
                            currentPage = 0;
                        }
                        viewPager.setCurrentItem(currentPage++, true);
                    }
                };

                Timer swiptimer = new Timer();

                swiptimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(update);
                    }
                }, 3000, 3000);
            }

            if (args.containsKey("Name")) {
                txtProductName.setText(args.getString("Name"));
            }

            if (args.containsKey("Price")) {
                txtPrice.setText("Price: ₹" + args.getString("Price"));
            }

            if (args.containsKey("Description")) {
                String des = "<p><span>Is it easy to clean?</span></p><p>All of our detox bottle components are easy to clean and are top rack dishwasher safe!</p><p><br></p><p>Order your VITARA fruit infuser bottle today. Click Add to Cart Now!</p>";

                txtDescription.setText(Html.fromHtml(des,Html.FROM_HTML_MODE_COMPACT));
            }

            if (args.containsKey("Discountpercent")) {
                txtDiscountpercent.setText(args.getString("Discountpercent") + " % off");
            }

            if (args.containsKey("MRP")) {
                txtMRP.setText("MRP: ₹" + args.getString("MRP"));
                txtMRP.setPaintFlags(txtMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean a = helper.getisLogin();
                if (a) {
                    cstid = helper.getcustomerid(getActivity());
                     qty = String.valueOf(counter);
                    addtocartsync addtocartsync=new addtocartsync();
                    addtocartsync.execute();
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

        return view;
    }

    private String insertcart(String cstid, String pid, String qty) {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_ADDTOCART);

        PropertyInfo pf = new PropertyInfo();
        pf.setName("CSTid");
        pf.setType(android.R.string.class);
        pf.setValue(cstid);
        request.addProperty(pf);

        PropertyInfo zx = new PropertyInfo();
        zx.setName("qty");
        zx.setType(android.R.string.class);
        zx.setValue(qty);
        request.addProperty(zx);

        PropertyInfo xc = new PropertyInfo();
        xc.setName("Pid");
        xc.setType(android.R.string.class);
        xc.setValue(pid);
        request.addProperty(xc);

        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        httpTransport.debug = true;
        SoapPrimitive resp1 = null;
        try {
            httpTransport.call(AppConfig.ACTION_ADDTOCART, envelope);
            resp1 = (SoapPrimitive) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp1.toString();
    }

    public class addtocartsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            result = insertcart(cstid, pid, qty);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result.equals("1")) {
                Toast.makeText(getActivity(), "Added To Cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void bindProduct() {
        try {
            productImageList.clear();
            for (int i = 0; i < resp.getPropertyCount(); i++) {

                SoapObject obj = (SoapObject) resp.getProperty(i);
                ProductImage bd = new ProductImage();
                bd.setPiid(obj.getProperty("Piid").toString());
                bd.setProdImgPath(obj.getProperty("ProdImgPath").toString());
                productImageList.add(bd);
            }
            NUM_PAGE = productImageList.size();
            ProductImageAdapter adapter = new ProductImageAdapter(getActivity(), productImageList);
            viewPager.setPadding(0, 0, 0, 0);
            viewPager.setAdapter(adapter);
        } catch (Exception ex) {
            Log.d("Area get Data Error", ex.toString());
        }
    }

    private void fetchproductimage(String pid) {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_PRODUCTIMAGE);
        PropertyInfo pf = new PropertyInfo();
        pf.setName("pid");
        pf.setType(android.R.string.class);
        pf.setValue(pid);
        request.addProperty(pf);

        SoapSerializationEnvelope enve = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        enve.dotNet = true;
        enve.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        httpTransport.debug = true;

        try {
            httpTransport.call(AppConfig.ACTION_PRODUCTIMAGE, enve);
            resp = (SoapObject) enve.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class productimagesync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            fetchproductimage(pid);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            bindProduct();
            super.onPostExecute(s);
        }
    }
}