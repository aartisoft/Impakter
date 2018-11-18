package com.impakter.impakter.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.DetailActivity;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.events.OnBuyClickListener;
import com.impakter.impakter.events.OnFavoriteClickListener;
import com.impakter.impakter.events.OnShareClickListener;
import com.impakter.impakter.object.HomeLatestRespond;
import com.impakter.impakter.object.ProductObj;

import java.util.ArrayList;
import java.util.List;

public class LatestAdapter extends RecyclerView.Adapter<LatestAdapter.ViewHolder> {
    private Activity context;
    private List<HomeLatestRespond.Data> listProducts;
    private LayoutInflater inflater;
    private OnBuyClickListener onBuyClickListener;
    private OnFavoriteClickListener onFavoriteClickListener;
    private OnShareClickListener onShareClickListener;

    public LatestAdapter(Activity context, List<HomeLatestRespond.Data> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_latest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeLatestRespond.Data data = listProducts.get(position);
        holder.tvProductName.setText(data.getName());
        holder.tvBrand.setText(data.getBrand());
        holder.tvPrice.setText(context.getResources().getString(R.string.lbl_currency) + data.getPrice());
        holder.ratingBar.setRating(data.getAverageRating());
        Glide.with(context).load(data.getImage()).into(holder.ivProduct);
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct, ivBuy, ivShare, ivFavorite;
        private TextView tvProductName, tvBrand, tvPrice;
        private RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvPrice = itemView.findViewById(R.id.tv_price);
            ratingBar = itemView.findViewById(R.id.rating_bar);

            ivBuy = itemView.findViewById(R.id.iv_buy);
            ivFavorite = itemView.findViewById(R.id.iv_favourite);
            ivShare = itemView.findViewById(R.id.iv_share);

            ivBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBuyClickListener.onBuyClick(ivBuy, getAdapterPosition());
                }
            });
            ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFavoriteClickListener.onFavoriteClick(ivFavorite, getAdapterPosition());
                }
            });
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClickListener.onShareClick(ivShare, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, DetailActivity.class));
                    context.overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_left);
                }
            });
        }
    }

    public void setOnBuyClickListener(OnBuyClickListener onBuyClickListener) {
        this.onBuyClickListener = onBuyClickListener;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener onFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener;
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }
}

