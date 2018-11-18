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
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnDeleteClickListener;
import com.impakter.impakter.events.OnMoveClickListener;
import com.impakter.impakter.events.OnShareClickListener;
import com.impakter.impakter.object.HomeLatestRespond;
import com.impakter.impakter.object.ProOfCollectionRespond;
import com.impakter.impakter.object.ProductObj;

import java.util.List;

public class CollectionDetailAdapter extends RecyclerView.Adapter<CollectionDetailAdapter.ViewHolder> {
    private Activity context;
    private List<ProOfCollectionRespond.Data> listProducts;
    private LayoutInflater inflater;
    private boolean isEdit;

    private OnMoveClickListener onMoveClickListener;
    private OnDeleteClickListener onDeleteClickListener;
    private OnShareClickListener onShareClickListener;

    public CollectionDetailAdapter(Activity context, List<ProOfCollectionRespond.Data> listProducts, boolean isEdit) {
        this.context = context;
        this.isEdit = isEdit;
        this.listProducts = listProducts;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_collection_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProOfCollectionRespond.Data data = listProducts.get(position);
        holder.tvProductName.setText(data.getName());
        holder.tvBrand.setText(data.getBrand());
        holder.tvPrice.setText(context.getResources().getString(R.string.lbl_currency) + data.getPrice());
        holder.tvNumberLike.setText(data.getLike() + " " + context.getResources().getString(R.string.like));
        Glide.with(context).load(data.getImage()).into(holder.ivProduct);
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct, ivShare, ivMove, ivDelete;
        private TextView tvProductName, tvBrand, tvPrice, tvNumberLike;
        private RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            ivShare = itemView.findViewById(R.id.iv_share);
            ivMove = itemView.findViewById(R.id.iv_move);
            ivDelete = itemView.findViewById(R.id.iv_delete);

            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvNumberLike = itemView.findViewById(R.id.tv_number_like);
            ratingBar = itemView.findViewById(R.id.rating_bar);

            if (isEdit) {
                ivMove.setVisibility(View.VISIBLE);
                ivDelete.setVisibility(View.VISIBLE);
                ivShare.setVisibility(View.GONE);
            } else {
                ivMove.setVisibility(View.GONE);
                ivDelete.setVisibility(View.GONE);
                ivShare.setVisibility(View.VISIBLE);
            }
            ivMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMoveClickListener.onMoveClick(v, getAdapterPosition());
                }
            });
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClickListener.onShareClick(v, getAdapterPosition());
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(v, getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(Constants.PRODUCT_ID, listProducts.get(getAdapterPosition()).getId());
                    context.startActivityForResult(intent, Constants.REQUEST_CODE_GO_TO_PRODUCT_DETAIL_ACTIVITY);
                    context.overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_left);
                }
            });
        }
    }

    public void setOnMoveClickListener(OnMoveClickListener onMoveClickListener) {
        this.onMoveClickListener = onMoveClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }
}
