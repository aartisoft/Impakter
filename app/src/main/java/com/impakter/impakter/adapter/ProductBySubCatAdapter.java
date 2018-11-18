package com.impakter.impakter.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.DetailActivity;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnBuyClickListener;
import com.impakter.impakter.events.OnFavoriteClickListener;
import com.impakter.impakter.events.OnLoadMoreListener;
import com.impakter.impakter.events.OnShareClickListener;
import com.impakter.impakter.object.HomeLatestRespond;
import com.impakter.impakter.object.ProductByCategoryRespond;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ProductBySubCatAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private Activity context;
    private List<ProductByCategoryRespond.Data> listProducts;
    private LayoutInflater inflater;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private OnBuyClickListener onBuyClickListener;
    private OnFavoriteClickListener onFavoriteClickListener;
    private OnShareClickListener onShareClickListener;

    public ProductBySubCatAdapter(RecyclerView recyclerView, Activity context, List<ProductByCategoryRespond.Data> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
        inflater = LayoutInflater.from(context);

        final GridLayoutManager linearLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return listProducts.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View view = inflater.inflate(R.layout.item_list_latest, parent, false);
            return new ViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_loading, parent, false);
            return new ProgressViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProductByCategoryRespond.Data data = listProducts.get(position);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).tvProductName.setText(data.getName());
            ((ViewHolder) holder).tvBrand.setText(data.getBrand());
            ((ViewHolder) holder).tvPrice.setText(context.getResources().getString(R.string.lbl_currency) + data.getPrice());
            ((ViewHolder) holder).ratingBar.setRating(data.getRating());
            Glide.with(context).load(data.getImage()).into(((ViewHolder) holder).ivProduct);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct, ivBuy, ivShare, ivFavorite;
        private TextView tvProductName, tvBrand, tvPrice;
        private MaterialRatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvPrice = itemView.findViewById(R.id.tv_price);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            ratingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

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
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(Constants.PRODUCT_ID, listProducts.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_left);
                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
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
