package com.impakter.impakter.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.fragment.ProductByCategoryFragment;
import com.impakter.impakter.object.CartItemObj;
import com.impakter.impakter.object.HomeCategoryRespond;
import com.impakter.impakter.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductViewPagerAdapter extends PagerAdapter {
    private Activity context;
    private List<CartItemObj> listProducts;
    private LayoutInflater inflater;

    public ProductViewPagerAdapter(Activity context, List<CartItemObj> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listProducts.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.item_list_my_bag, container, false);
        CartItemObj data = listProducts.get(position);

        ImageView ivProduct, ivDelete;
        ImageView ivIncrease, ivDecrease;
        TextView tvProductName, tvBrand, tvDescription, tvOption, tvPrice, tvQuantity;

        ivProduct = view.findViewById(R.id.iv_product);
        ivDelete = view.findViewById(R.id.iv_delete);
        ivIncrease = view.findViewById(R.id.iv_increase);
        ivDecrease = view.findViewById(R.id.iv_decrease);

        tvProductName = view.findViewById(R.id.tv_product_name);
        tvBrand = view.findViewById(R.id.tv_brand);
        tvDescription = view.findViewById(R.id.tv_description);
        tvPrice = view.findViewById(R.id.tv_price);
        tvOption = view.findViewById(R.id.tv_option);
        tvQuantity = view.findViewById(R.id.tv_quantity);

        tvProductName.setText(data.getName());
        tvBrand.setText(data.getBrand());
        tvDescription.setText(context.getResources().getString(R.string.sku) + ": " + data.getCode());
        tvOption.setText(data.getOption());
        tvQuantity.setText(data.getQuantity() + "");
        tvPrice.setText(context.getResources().getString(R.string.lbl_currency) + data.getTotalPrice());
        Glide.with(context).load(data.getImage()).into(ivProduct);

        ivDelete.setVisibility(View.GONE);
        ivIncrease.setVisibility(View.INVISIBLE);
        ivDecrease.setVisibility(View.INVISIBLE);
        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
