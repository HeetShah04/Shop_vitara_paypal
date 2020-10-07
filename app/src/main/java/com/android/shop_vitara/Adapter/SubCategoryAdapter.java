package com.android.shop_vitara.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shop_vitara.Activities.LoginActivity;
import com.android.shop_vitara.Fragments.HomeFragment;
import com.android.shop_vitara.Fragments.OrderSummaryFragment;
import com.android.shop_vitara.Fragments.ProductDescriptionFragment;
import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.Model.PreferenceHelper;
import com.android.shop_vitara.Model.Subcategory;
import com.android.shop_vitara.R;
import com.bumptech.glide.Glide;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.vhold> {
    Context context;
    List<Subcategory> subcategoryList;
    String cstid, pid, qty, result;
    PreferenceHelper helper;

    public SubCategoryAdapter(Context context, List<Subcategory> subcategoryList) {
        this.context = context;
        this.subcategoryList = subcategoryList;
    }

    @NonNull
    @Override
    public vhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_subcategory_list, parent, false);
        return new vhold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vhold holder, final int position) {
        helper = new PreferenceHelper(context);
        final Subcategory sb = subcategoryList.get(position);
        Glide.with(this.context).load(AppConfig.BASE_URL + sb.getProductImages().replace("../", "")).into(holder.subcat_image);
        holder.product_name.setText(sb.getProductName());
        holder.subcat_MRP.setPaintFlags(holder.subcat_MRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.subcat_MRP.setText("₹" + sb.getMRP());
        holder.subcat_discountpercent.setText(sb.getDiscPercnt() + "%");
        holder.subcat_price.setText("₹" + sb.getPrice());
        holder.ratingBar.setRating(Float.parseFloat(sb.getAvgRating()));

        holder.btn_subcat_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean a = helper.getisLogin();
                if (a) {
                    cstid = helper.getcustomerid(context);
                    pid = sb.getPid();
                    qty = "1";
                    addtocartsync addtocartsync=new addtocartsync();
                    addtocartsync.execute();
                } else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }
        });
        holder.btn_subcat_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean a = helper.getisLogin();
                if (a) {
                    OrderSummaryFragment orderSummaryFragment=new OrderSummaryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Image", sb.getProductImages());
                    bundle.putString("Name", sb.getProductName());
                    bundle.putString("Pid", sb.getPid());
                    bundle.putString("Price", sb.getPrice());
                    bundle.putString("MRP", sb.getMRP());
                    bundle.putString("Qty","1");
                    bundle.putString("Discountpercent", sb.getDiscPercnt());
                    bundle.putString("MRP", sb.getMRP());
                    bundle.putString("Discount",sb.getDiscount());
                    bundle.putString("Tax",sb.getTax());
                    orderSummaryFragment.setArguments(bundle);
                    FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.hide(new HomeFragment());
                    ft.replace(R.id.nav_host_fragment, orderSummaryFragment);
                    ft.addToBackStack(null);
                    ft.commit();

                } else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDescriptionFragment productDescriptionFragment = new ProductDescriptionFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Image", sb.getProductImages());
                bundle.putString("Name", sb.getProductName());
                bundle.putString("Pid", sb.getPid());
                bundle.putString("Price", sb.getPrice());
                //bundle.putString("Discount", sb.getDiscount());
                bundle.putString("Description", sb.getDescription());
                bundle.putString("Discountpercent", sb.getDiscPercnt());
                bundle.putString("MRP", sb.getMRP());
                bundle.putString("Discount",sb.getDiscount());
                productDescriptionFragment.setArguments(bundle);

                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(new HomeFragment());
                ft.replace(R.id.nav_host_fragment, productDescriptionFragment);
                ft.addToBackStack(null);
                ft.commit();

              /*  FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, productDescriptionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
*/
              /*  FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment,new ProductDescriptionFragment()).commit();*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return subcategoryList.size();
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

    public class vhold extends RecyclerView.ViewHolder {
        ImageView subcat_image;
        CardView cardView;
        TextView subcat_discountpercent, product_name, subcat_price, subcat_MRP;
        Button btn_subcat_add_to_cart, btn_subcat_buy_now;
        RatingBar ratingBar;

        public vhold(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv);
            subcat_image = itemView.findViewById(R.id.subcat_image);
            subcat_discountpercent = itemView.findViewById(R.id.subcat_discountpercent);
            product_name = itemView.findViewById(R.id.product_name);
            subcat_price = itemView.findViewById(R.id.subcat_price);
            subcat_MRP = itemView.findViewById(R.id.subcat_MRP);
            btn_subcat_add_to_cart = itemView.findViewById(R.id.btn_subcat_add_to_cart);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            btn_subcat_buy_now = itemView.findViewById(R.id.btn_subcat_buy_now);

        }
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
                Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
