package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.events.OnDeleteClickListener;
import com.impakter.impakter.object.CartItemObj;
import com.impakter.impakter.object.CartItemRespond;

import java.util.ArrayList;
import java.util.List;

public class MyBagAdapter extends RecyclerView.Adapter<MyBagAdapter.ViewHolder> {
    private Activity context;
    private List<CartItemObj> listProducts = new ArrayList<>();
    private LayoutInflater inflater;

    private OnDeleteClickListener onDeleteClickListener;
    private OnInCreaseClickListener onInCreaseClickListener;
    private OnDecreaseClickListener onDecreaseClickListener;

    public MyBagAdapter(Activity context, List<CartItemObj> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_my_bag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItemObj data = listProducts.get(position);
        if (data != null) {
            holder.tvProductName.setText(data.getName());
            holder.tvBrand.setText(data.getBrand());
            holder.tvDescription.setText(context.getResources().getString(R.string.sku) + ": " + data.getCode());
            holder.tvOption.setText(data.getOption());
            holder.tvQuantity.setText(data.getQuantity() + "");
            holder.tvPrice.setText(context.getResources().getString(R.string.lbl_currency) + data.getTotalPrice());
            Glide.with(context).load(data.getImage()).into(holder.ivProduct);
        }
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct, ivDelete;
        private ImageView ivIncrease, ivDecrease;
        private TextView tvProductName, tvBrand, tvDescription, tvOption, tvPrice, tvQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivIncrease = itemView.findViewById(R.id.iv_increase);
            ivDecrease = itemView.findViewById(R.id.iv_decrease);

            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvOption = itemView.findViewById(R.id.tv_option);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(v, getAdapterPosition());
                }
            });
            ivIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInCreaseClickListener.onIncreaseClick(v, getAdapterPosition());
                }
            });
            ivDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDecreaseClickListener.onDecreaseClick(v, getAdapterPosition());
                }
            });
        }
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public interface OnInCreaseClickListener {
        void onIncreaseClick(View view, int position);
    }

    public void setOnIncreaseClickListener(OnInCreaseClickListener onIncreaseClickListener) {
        this.onInCreaseClickListener = onIncreaseClickListener;
    }

    public interface OnDecreaseClickListener {
        void onDecreaseClick(View view, int position);
    }

    public void setOnDecreaseClickListener(OnDecreaseClickListener onDecreaseClickListener) {
        this.onDecreaseClickListener = onDecreaseClickListener;
    }
}
