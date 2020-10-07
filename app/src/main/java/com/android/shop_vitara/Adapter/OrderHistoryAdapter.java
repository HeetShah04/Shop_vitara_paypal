package com.android.shop_vitara.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shop_vitara.Fragments.HomeFragment;
import com.android.shop_vitara.Fragments.OrderDetailsFragment;
import com.android.shop_vitara.Model.OrderHistory;
import com.android.shop_vitara.R;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.vholder> {
    Context context;
    List<OrderHistory> historyList;

    public OrderHistoryAdapter(Context context, List<OrderHistory> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_myorder, parent, false);
        return new vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vholder holder, int position) {
        final OrderHistory od = historyList.get(position);
        holder.order_no.setText("Order No:-"+od.getOrderNo());
        holder.order_date.setText("Order Date:- "+od.getOrderDate());
        holder.order_status.setText("Order Status:- "+od.getOrderStatus());
        holder.payment_type.setText("Payment Type:- "+od.getPaymentType());
        holder.payment_status.setText("Payment Status:- "+od.getPaymentStatus());
        holder.cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailsFragment orderDetailsFragment=new OrderDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Orderno",od.getOrderNo());
                orderDetailsFragment.setArguments(bundle);

                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(new HomeFragment());
                ft.replace(R.id.nav_host_fragment, orderDetailsFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class vholder extends RecyclerView.ViewHolder {
        CardView cv1;
        TextView order_no, order_status, order_date, payment_type, payment_status;
        public vholder(@NonNull View itemView) {
            super(itemView);
            cv1 = itemView.findViewById(R.id.cv1);
            order_no = itemView.findViewById(R.id.order_no);
            order_status = itemView.findViewById(R.id.order_status);
            order_date = itemView.findViewById(R.id.order_date);
            payment_type = itemView.findViewById(R.id.payment_type);
            payment_status = itemView.findViewById(R.id.payment_status);
        }
    }
}
