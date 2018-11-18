package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;

import java.util.ArrayList;
import java.util.List;

public class ImageViewPagerAdapter extends PagerAdapter {
    private Activity context;
    private List<String> listImages;
    private LayoutInflater inflater;

    public ImageViewPagerAdapter(Activity context, List<String> listImages) {
        this.context = context;
        this.listImages = listImages;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_view_pager, container, false);

        ImageView ivProduct = view.findViewById(R.id.iv_product);
        Glide.with(context).load(listImages.get(position)).into(ivProduct);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
