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
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.object.OrderDetailRespond;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private Activity context;
    private List<OrderDetailRespond.ListItems> listOrders;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public OrderDetailAdapter(Activity context, List<OrderDetailRespond.ListItems> listOrders) {
        this.context = context;
        this.listOrders = listOrders;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetailRespond.ListItems data = listOrders.get(position);
        if (data != null) {
            holder.tvProductName.setText(data.getName());
            holder.tvBrand.setText(data.getName());
            holder.tvOrderNo.setText(context.getResources().getString(R.string.sku) + " - " + data.getCode());
            String totalPriceForm = " (" + data.getPrice() + " * " + data.getQuantity() + " - " + data.getDiscount() + " )";
            holder.tvTotalPrice.setText(context.getResources().getString(R.string.total) + ": " + context.getResources().getString(R.string.lbl_currency) + data.getTotalPrice() /*+ totalPriceForm*/);
            holder.tvPrice.setText(context.getResources().getString(R.string.price) + ": " + context.getResources().getString(R.string.lbl_currency) + data.getPrice());
            holder.tvOption.setText(data.getOptions());
            holder.tvDiscount.setText(context.getResources().getString(R.string.discount) + ": " + context.getResources().getString(R.string.lbl_currency) + data.getDiscount());
            holder.tvQuantity.setText(context.getResources().getString(R.string.quantity) + ": " + data.getQuantity());
            Glide.with(context).load(data.getImage()).into(holder.ivProduct);
        }
    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct;
        private TextViewHeeboRegular tvProductName, tvBrand, tvOrderNo, tvPrice, tvTotalPrice, tvOption;
        private TextView tvQuantity, tvDiscount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvOrderNo = itemView.findViewById(R.id.tv_order_no);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            tvTotalPrice.setSelected(true);
            tvOption = itemView.findViewById(R.id.tv_option);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvDiscount = itemView.findViewById(R.id.tv_discount);
            tvPrice = itemView.findViewById(R.id.tv_price);

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
