package com.android.shop_vitara.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.Model.Subcategory;
import com.android.shop_vitara.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.vhold> {
    Context ctx;
    List<Subcategory> cartProductList;
    int total = 0;
    int counter = 1;
    int qty = 1;
    int shipping = 29;
    int subtotal = 0;
    public OrderViewAdapter(Context ctx, List<Subcategory> cartProductList) {
        this.ctx = ctx;
        this.cartProductList = cartProductList;
    }

    @NonNull
    @Override
    public vhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_ordersummary, parent, false);
        return new vhold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vhold holder, int position) {
        final TextView tv = ((Activity) ctx).findViewById(R.id.subtot);
        final TextView tv1 = ((Activity) ctx).findViewById(R.id.shpcharge);
        final TextView tv2 = ((Activity) ctx).findViewById(R.id.amtpay);
        final Subcategory cl = cartProductList.get(position);
        holder.ordsum_MRP.setText("Mrp: ₹"+cl.getMRP());
        holder.ordsum_MRP.setPaintFlags(holder.ordsum_MRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.ordsum_Price.setText("Price: ₹"+cl.getPrice());
        holder.ordsum_Qty.setText("Qty: "+cl.getQty());
        holder.ordsum_ProductName.setText(cl.getProductName());
        int price= Integer.parseInt(cl.getPrice());
        int qty= Integer.parseInt(cl.getQty());
        total = shipping*qty+ total;
        subtotal = subtotal+ price*qty;
        holder.totamt.setText("Ammount : ₹ "+price*qty);
        Glide.with(this.ctx).load(AppConfig.BASE_URL + cl.getProductImages().replace("..", "")).into(holder.ordsum_ProductImage);
        int tot=total+subtotal;
        if(subtotal<=1000){
        tv.setText(String.valueOf("₹ "+subtotal));
        tv1.setText(String.valueOf("₹ "+total));
        tv2.setText(String.valueOf("₹ "+tot));}
        else{
            tv.setText(String.valueOf("₹ "+subtotal));
            tv1.setText(String.valueOf("Free"));
            tv2.setText(String.valueOf("₹ "+subtotal));}
        }

    @Override
    public int getItemCount() {
        return cartProductList.size();
    }

    public class vhold extends RecyclerView.ViewHolder {
        ImageView ordsum_ProductImage;
        TextView ordsum_ProductName, ordsum_Price, ordsum_MRP, ordsum_Qty,totamt;

        public vhold(@NonNull View itemView) {
            super(itemView);
            ordsum_ProductImage = itemView.findViewById(R.id.ordsum_ProductImage);
            ordsum_ProductName = itemView.findViewById(R.id.ordsum_ProductName);
            ordsum_Price = itemView.findViewById(R.id.ordsum_Price);
            ordsum_MRP = itemView.findViewById(R.id.ordsum_MRP);
            ordsum_Qty = itemView.findViewById(R.id.ordsum_Qty);
            totamt=itemView.findViewById(R.id.totamt);

        }
    }
}
