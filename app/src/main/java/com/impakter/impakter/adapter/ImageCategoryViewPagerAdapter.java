package com.impakter.impakter.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.fragment.ProductByCategoryFragment;
import com.impakter.impakter.object.HomeCategoryRespond;
import com.impakter.impakter.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class ImageCategoryViewPagerAdapter extends PagerAdapter {
    private Activity context;
    private List<HomeCategoryRespond.SubCatArray> listImages;
    private LayoutInflater inflater;

    public ImageCategoryViewPagerAdapter(Activity context, List<HomeCategoryRespond.SubCatArray> listImages) {
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
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.item_categor_view_pager, container, false);

        ImageView ivProduct = view.findViewById(R.id.iv_product);
        ImageView imageView = view.findViewById(R.id.image_view);
        TextView tvSubCat = view.findViewById(R.id.tv_sub_cateogory);

        tvSubCat.setText(listImages.get(position).getName());
        Glide.with(context).load(listImages.get(position).getImage()).into(ivProduct);
        container.addView(view);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                AppUtil.convertDpToPixel(context, 180)
        );
        params.setMargins(AppUtil.convertDpToPixel(context, 8), 0, AppUtil.convertDpToPixel(context, 8), 0);
        ivProduct.setLayoutParams(params);
        imageView.setLayoutParams(params);
        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductByCategoryFragment productByCategoryFragment = new ProductByCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.CURRENT_POSITION, position);
                bundle.putInt(Constants.CATEGORY_ID, listImages.get(position).getParentId());
                bundle.putParcelableArrayList(Constants.LIST_SUB, (ArrayList<? extends Parcelable>) listImages);
                productByCategoryFragment.setArguments(bundle);
                ((MainActivity) context).showFragmentWithAddMethod(productByCategoryFragment, true);
            }
        });
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
