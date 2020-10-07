package com.android.shop_vitara.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shop_vitara.Model.OrderHistory;
import com.android.shop_vitara.R;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.vhold> {
    Context context;
    List<OrderHistory> historyList = new ArrayList<>();

    public OrderDetailAdapter(Context context, List<OrderHistory> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public vhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_orderdetail, parent, false);
        return new vhold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vhold holder, int position) {
        OrderHistory history=historyList.get(position);
        holder.pdt_price.setText("Price: ₹ "+history.getPrice());
        holder.pdt_qty.setText("Qty: "+history.getQty());
        holder.pdt_shipping.setText("Shipping Charge: ₹ "+history.getShippingCharge());
        holder.pdt_total.setText("Total Amount: ₹ "+history.getFinalamt());
        holder.pdt_mrp.setText("Mrp: ₹ "+history.getMRP());
        holder.pdt_mrp.setPaintFlags(holder.pdt_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.orderno.setText("Order No: "+history.getOrderNo());
        holder.pdt_name.setText(history.getProductName());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class vhold extends RecyclerView.ViewHolder {
        TextView orderno, pdt_name, pdt_qty, pdt_price, pdt_total, pdt_shipping, pdt_mrp;

        public vhold(@NonNull View itemView) {
            super(itemView);
            orderno=itemView.findViewById(R.id.orderno);
            pdt_name=itemView.findViewById(R.id.pdt_name);
            pdt_qty=itemView.findViewById(R.id.pdt_qty);
            pdt_price=itemView.findViewById(R.id.pdt_price);
            pdt_total=itemView.findViewById(R.id.pdt_total);
            pdt_shipping=itemView.findViewById(R.id.pdt_shipping);
            pdt_mrp=itemView.findViewById(R.id.pdt_mrp);
        }
    }
}
