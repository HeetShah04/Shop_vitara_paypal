package com.android.shop_vitara.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.shop_vitara.Model.ProductList;
import com.android.shop_vitara.R;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.vholder>{
    Context context;
    List<ProductList> productLists;

    public ProductListAdapter(Context context, List<ProductList> productLists) {
        this.context = context;
        this.productLists = productLists;
    }

    @NonNull
    @Override
    public vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_product_header, parent, false);
        return new vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vholder holder, int position) {
        ProductList list = productLists.get(position);
        holder.Category_name.setText(list.getCategoryName());
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false);
        holder.subcategories_recycler.setLayoutManager(layoutManager);
        SubCategoryAdapter adapter = new SubCategoryAdapter(context, list.getSubcategories());
        holder.subcategories_recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }


    public class vholder extends RecyclerView.ViewHolder {
        TextView Category_name;
        RecyclerView subcategories_recycler;

        public vholder(@NonNull View itemView) {
            super(itemView);
            Category_name = itemView.findViewById(R.id.Category_name);
            subcategories_recycler = itemView.findViewById(R.id.subcategories_recycler);

        }
    }
}
