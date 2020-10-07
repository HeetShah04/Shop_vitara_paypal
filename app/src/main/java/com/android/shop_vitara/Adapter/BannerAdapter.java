package com.android.shop_vitara.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.android.shop_vitara.Model.AppConfig;
import com.android.shop_vitara.Model.Banner;
import com.android.shop_vitara.R;
import com.bumptech.glide.Glide;

import java.util.List;


public class BannerAdapter extends PagerAdapter {
    Context context;
    List<Banner> bannerList;

    public BannerAdapter(Context context, List<Banner> bannerList) {
        this.context = context;
        this.bannerList = bannerList;
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_layout_banner, container, false);
        ImageView imageView = view.findViewById(R.id.bannerlist);

        Glide.with(this.context).load(AppConfig.BASE_URL + bannerList.get(position).getBannerPath().replace("..", "")).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
