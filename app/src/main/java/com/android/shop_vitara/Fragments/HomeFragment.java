package com.android.shop_vitara.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.shop_vitara.Adapter.BannerAdapter;
import com.android.shop_vitara.Adapter.ProductListAdapter;
import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.Model.Banner;
import com.android.shop_vitara.Model.ProductList;
import com.android.shop_vitara.Model.Subcategory;
import com.android.shop_vitara.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {
    private static int currentPage = 0;
    private static int NUM_PAGE = 0;
    List<Banner> banner = new ArrayList<>();
    List<ProductList> productLists = new ArrayList<>();
    ProductList pl;
    ViewPager viewPager;
    RecyclerView recyclerView;
    private SoapObject resp;
    private SoapPrimitive resp1;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = root.findViewById(R.id.banner);
        bannersync bannersync = new bannersync();
        bannersync.execute();
        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        productlistsync productlistsync = new productlistsync();
        productlistsync.execute();
        recyclerView = root.findViewById(R.id.product_list_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
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
        }, 2000, 2000);
        return root;
    }

    public void fetchbannerdata() {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_BANNER);
        SoapSerializationEnvelope enve = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        enve.dotNet = true;
        enve.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        httpTransport.debug = true;

        try {
            httpTransport.call(AppConfig.ACTION_BANNER, enve);
            resp = (SoapObject) enve.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindbannerdata() {
        try {
            banner.clear();
            for (int i = 0; i < resp.getPropertyCount(); i++) {

                SoapObject obj = (SoapObject) resp.getProperty(i);
                Banner bd = new Banner();
                bd.setBannerPath(obj.getProperty("BannerPath").toString());
                bd.setBid(obj.getProperty("Bid").toString());
                bd.setDescription(obj.getProperty("Description").toString());
                bd.setProIndex(obj.getProperty("ProIndex").toString());
                banner.add(bd);
            }
            NUM_PAGE = banner.size();
            BannerAdapter adapter = new BannerAdapter(getActivity(), banner);
            viewPager.setPadding(0, 0, 0, 0);
            viewPager.setAdapter(adapter);
        } catch (Exception ex) {
            Log.d("Area get Data Error", ex.toString());
        }
    }

    private void fetchProductdata() {

        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_CATEGORIES);
        SoapSerializationEnvelope enve = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        enve.dotNet = true;
        enve.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        httpTransport.debug = true;
        resp1 = null;
        try {
            httpTransport.call(AppConfig.ACTION_CATEGORIES, enve);
            resp1 = (SoapPrimitive) enve.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindproductdata() {
        productLists.clear();
        try {
            String jsonresponse = resp1.toString();
            JSONArray jsonArray = new JSONArray(jsonresponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonArrayJSONObject = jsonArray.getJSONObject(i);
                String a = jsonArrayJSONObject.getString("categories");
                JSONArray jArray = new JSONArray(a);
                for (int j = 0; j < jArray.length(); j++) {
                    pl = new ProductList();
                    JSONObject object = jArray.getJSONObject(j);
                    pl.setCategoryName(object.getString("CategoryName"));
                    String b = object.getString("subCategories");
                    JSONArray array = new JSONArray(b);
                    List<Subcategory> subcategoryList = new ArrayList<>();
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
                        subcategoryList.add(subcategory);
                    }
                    pl.setSubcategories(subcategoryList);
                    productLists.add(pl);
                }

            }
            ProductListAdapter listAdapter = new ProductListAdapter(getActivity(), productLists);
            recyclerView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Log.d("Area get Data Error", ex.toString());
        }
    }

    public class productlistsync extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
          dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            fetchProductdata();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            bindproductdata();
            super.onPostExecute(s);
            dialog.dismiss();
        }
    }

    public class bannersync extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("PLease Wait!!");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            fetchbannerdata();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bindbannerdata();
            dialog.dismiss();
        }
    }
}