package com.android.shop_vitara.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shop_vitara.Model.AppConfig;
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

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.vh> {
    Context context;
    List<Subcategory> cartProductlistList;
    int total = 0;
    int counter = 1;
    int qty = 1;
    String pcid, result, delid;

    public CartViewAdapter(Context context, List<Subcategory> cartProductlistList) {
        this.context = context;
        this.cartProductlistList = cartProductlistList;
    }

    @NonNull
    @Override
    public CartViewAdapter.vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_cart, parent, false);
        return new vh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewAdapter.vh holder, final int position) {
        final TextView tv = ((Activity) context).findViewById(R.id.txtTotalAmount);
        final RelativeLayout rv = ((Activity) context).findViewById(R.id.relative_Orders);
        final LinearLayout ll = ((Activity) context).findViewById(R.id.Linear_No_Orders);
        final Subcategory cl = cartProductlistList.get(position);
        final int a = Integer.parseInt(cl.getPrice());
        qty = Integer.parseInt(cl.getQty());

        total = a * qty + total;
        holder.cart_MRP.setText("MRP: ₹" + cl.getMRP());
        holder.cart_MRP.setPaintFlags(holder.cart_MRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.cart_Price.setText("Price: ₹" + cl.getPrice());
        holder.cart_ProductName.setText(cl.getProductName());
        holder.cart_Discount.setText("You Save ₹" + cl.getDiscount());
        holder.cart_Discount_percent.setText(cl.getDiscPercnt() + " % off");
        holder.txtCount.setText("" + cl.getQty());
        holder.txttot.setText("Total: ₹ " + a * qty);
        Glide.with(this.context).load(AppConfig.BASE_URL + cl.getProductImages().replace("..", "")).into(holder.cart_ProductImage);
        pcid = cl.getCartid();
        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = ++qty;
                holder.txtCount.setText("" + qty);
                int b = Integer.parseInt(cl.getPrice());
                int amt = b * qty;
                //total=(total-b)+amt;
                total= Integer.parseInt(tv.getText().toString())+b;
                holder.txttot.setText("Amount: ₹ " + amt);
                tv.setText(String.valueOf(total));
                updatequantitysync updatequantitysync = new updatequantitysync();
                updatequantitysync.execute();
            }
        });
        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qty > 1) {
                    qty = --qty;
                    holder.txtCount.setText("" + qty);
                    int b = Integer.parseInt(cl.getPrice());
                    int amt = b * qty;
                    total= Integer.parseInt(tv.getText().toString())-b;
                    holder.txttot.setText("Amount: ₹ " + amt);
                    tv.setText(String.valueOf(total));
                    updatequantitysync updatequantitysync = new updatequantitysync();
                    updatequantitysync.execute();
                }

            }
        });
        holder.imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delid = cl.getCartid();
                int b = Integer.parseInt(cl.getPrice());
                int amt = b * qty;
                total= Integer.parseInt(tv.getText().toString())-amt;
                tv.setText(String.valueOf( total));
                cartProductlistList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                deletefromcart sunc = new deletefromcart();
                sunc.execute();
                if (cartProductlistList.size() == 0) {
                    rv.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartProductlistList.size();
    }

    public String updatequantity(String pcid, String qty) {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_UPDATEQTY);
        PropertyInfo pf = new PropertyInfo();
        pf.setName("pcid");
        pf.setValue(pcid);
        pf.setType(android.R.string.class);
        request.addProperty(pf);

        PropertyInfo zx = new PropertyInfo();
        zx.setName("qty");
        zx.setType(android.R.string.class);
        zx.setValue(qty);
        request.addProperty(zx);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        httpTransport.debug = true;
        SoapPrimitive response = null;
        try {
            httpTransport.call(AppConfig.ACTION_QTY, envelope);
            response = (SoapPrimitive) envelope.getResponse();
        } catch (Exception soapFault) {
            soapFault.printStackTrace();
        }
        return response.toString();
    }

    public String deletequantity(String pcid) {
        SoapObject request = new SoapObject(AppConfig.SOAP_URL, AppConfig.SOAP_DELETECARTPDt);
        PropertyInfo pf = new PropertyInfo();
        pf.setName("pcid");
        pf.setValue(pcid);
        pf.setType(android.R.string.class);
        request.addProperty(pf);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(AppConfig.SOAP_ADDRESS);
        httpTransport.debug = true;
        SoapPrimitive response = null;
        try {
            httpTransport.call(AppConfig.ACTION_DELETE_CART_PDT, envelope);
            response = (SoapPrimitive) envelope.getResponse();
        } catch (Exception soapFault) {
            soapFault.printStackTrace();
        }
        return response.toString();
    }

    public class vh extends RecyclerView.ViewHolder {
        ImageView cart_ProductImage, imgMinus, imgPlus, imgdel;
        TextView cart_Discount_percent, cart_ProductName, cart_Price, cart_MRP, txtCount, txttot, cart_Discount;

        public vh(@NonNull View itemView) {
            super(itemView);
            txttot = itemView.findViewById(R.id.txtot);
            cart_Discount = itemView.findViewById(R.id.cart_Discount);
            cart_Discount_percent = itemView.findViewById(R.id.cart_Discount_percent);
            txtCount = itemView.findViewById(R.id.txtCount);
            cart_ProductImage = itemView.findViewById(R.id.cart_ProductImage);
            cart_ProductName = itemView.findViewById(R.id.cart_ProductName);
            cart_Price = itemView.findViewById(R.id.cart_Price);
            cart_MRP = itemView.findViewById(R.id.cart_MRP);
            txtCount = itemView.findViewById(R.id.txtCount);
            imgPlus = itemView.findViewById(R.id.imgPlus);
            imgMinus = itemView.findViewById(R.id.imgMinus);
            imgdel = itemView.findViewById(R.id.imgdel);

        }
    }

    public class updatequantitysync extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setTitle("PLease Wait!!");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            result = updatequantity(pcid, String.valueOf(qty));
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result.equals("1")) {
                Toast.makeText(context, "Cart Updated Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something Went Wrong...!!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }

    public class deletefromcart extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setTitle("PLease Wait!!");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            result = deletequantity(pcid);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (result.equals("1")) {
                Toast.makeText(context, "Cart Updated Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something Went Wrong...!!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }
}
