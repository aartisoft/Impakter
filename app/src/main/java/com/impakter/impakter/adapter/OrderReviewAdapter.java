package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.object.OrderDetailRespond;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class OrderReviewAdapter extends RecyclerView.Adapter<OrderReviewAdapter.ViewHolder> {
    private Activity context;
    private List<OrderDetailRespond.ListItems> listOrders;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public OrderReviewAdapter(Activity context, List<OrderDetailRespond.ListItems> listOrders) {
        this.context = context;
        this.listOrders = listOrders;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_order_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetailRespond.ListItems data = listOrders.get(position);
        if (data != null) {
            holder.tvProductName.setText(data.getName());
            holder.tvBrand.setText(data.getName());
            holder.tvOrderNo.setText(context.getResources().getString(R.string.sku) + " - " + data.getCode());
            holder.tvOption.setText(data.getOptions());
            holder.ratingBar.setRating(data.getRate());
            Glide.with(context).load(data.getImage()).into(holder.ivProduct);
        }
    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct;
        private TextView tvProductName, tvBrand, tvOrderNo, tvOption;
        private MaterialRatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductName.setSelected(true);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvBrand.setSelected(true);
            tvOrderNo = itemView.findViewById(R.id.tv_order_no);
            tvOption = itemView.findViewById(R.id.tv_option);
            ivProduct = itemView.findViewById(R.id.iv_product);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            ratingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
