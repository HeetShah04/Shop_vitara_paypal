package com.android.shop_vitara.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shop_vitara.Activities.LoginActivity;
import com.android.shop_vitara.Adapter.CartViewAdapter;
import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.Model.PreferenceHelper;
import com.android.shop_vitara.Model.Subcategory;
import com.android.shop_vitara.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    Button btnStartShopping, btn_Checkout;
    LinearLayout linearnoorder;
    RelativeLayout relativeOrders;
    TextView txtTotalAmount;
    PreferenceHelper preferenceHelper;
    String cstid, result;
    RecyclerView recyclerCart;
    int total;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        linearnoorder = view.findViewById(R.id.Linear_No_Orders);
        relativeOrders = view.findViewById(R.id.relative_Orders);
        recyclerCart = view.findViewById(R.id.recyclerCart);
        txtTotalAmount = view.findViewById(R.id.txtTotalAmount);
        btn_Checkout = view.findViewById(R.id.btnCheckout);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("View Cart");
        btnStartShopping = view.findViewById(R.id.btnStartShopping);
        btnStartShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStackImmediate();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(new CartFragment());
                ft.add(R.id.nav_host_fragment, new HomeFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        btn_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Total", Integer.parseInt(txtTotalAmount.getText().toString()));
                orderSummaryFragment.setArguments(bundle);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(new HomeFragment());
                ft.replace(R.id.nav_host_fragment, new OrderSummaryFragment());
                ft.addToBackStack("a");
                ft.commit();
                txtTotalAmount.setText("");
            }
        });
        preferenceHelper = new PreferenceHelper(getActivity());
        Boolean a = preferenceHelper.getisLogin();
        if (a) {
            cstid = preferenceHelper.getcustomerid(getActivity());
            viewcartsync sync = new viewcartsync();
            sync.execute();
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        return view;
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
        total = 0;
        List<Subcategory> subcategoryList = new ArrayList<>();
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
                    subcategory.setPCid(obj.getString("PCid"));
                    subcategory.setPSCid(obj.getString("PSCid"));
                    subcategory.setProductName(obj.getString("ProductName"));
                    subcategory.setPrice(obj.getString("Price"));
                    subcategory.setDiscount(obj.getString("Discount"));
                    subcategory.setDescription(obj.getString("Description"));
                    subcategory.setProductImages(obj.getString("ProductImages"));
                    subcategory.setAvgRating(obj.getString("AvgRating"));
                    subcategory.setStatus(obj.getString("Status"));
                    subcategory.setMRP(obj.getString("MRP"));
                    subcategory.setDiscPercnt(obj.getString("DiscPercnt"));
                    subcategory.setQty(obj.getString("Qty"));
                    subcategory.setTax(obj.getString("Tax"));
                    subcategory.setProductCode(obj.getString("ProductCode"));
                    subcategory.setHSNCode(obj.getString("HSNCode"));
                    subcategory.setCartid(obj.getString("Cartid"));
                    subcategoryList.add(subcategory);
                    int a = Integer.parseInt(obj.getString("Price"));
                    int qty = Integer.parseInt(obj.getString("Qty"));
                    total = total + a * qty;
                }
            }
            txtTotalAmount.setText(String.valueOf(total));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerCart.setLayoutManager(layoutManager);
            CartViewAdapter cartViewAdapter = new CartViewAdapter(getActivity(), subcategoryList);
            recyclerCart.setAdapter(cartViewAdapter);
            cartViewAdapter.notifyDataSetChanged();

        } catch (Exception e) {

        }
    }

    public class viewcartsync extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Loading!!!!!");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            result = viewcart(cstid);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result.equals("AnyType{}") || result.equals("null") || result.equals("[]")) {
                linearnoorder.setVisibility(View.VISIBLE);
                relativeOrders.setVisibility(View.GONE);
            } else {
                linearnoorder.setVisibility(View.GONE);
                relativeOrders.setVisibility(View.VISIBLE);
                adddatainviewcart();
            }
            dialog.cancel();
        }
    }
}