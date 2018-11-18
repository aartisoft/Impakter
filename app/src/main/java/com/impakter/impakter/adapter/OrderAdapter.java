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
import com.impakter.impakter.object.OrderObject;
import com.impakter.impakter.object.OrderRespond;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Activity context;
    private List<OrderObject> listOrders;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    private LinkedHashMap<String, Integer> mMapIndex;
    private ArrayList<String> mSectionList;
    private String[] mSections;

    public OrderAdapter(Activity context, List<OrderObject> listOrders) {
        this.context = context;
        this.listOrders = listOrders;
        inflater = LayoutInflater.from(context);
        fillSections();
    }

    public void fillSections() {
        mMapIndex = new LinkedHashMap<String, Integer>();

        for (int x = 0; x < listOrders.size(); x++) {
            String orderNumber = listOrders.get(x).getOrderNumber() + "";
            if (orderNumber.length() > 0) {
                if (!mMapIndex.containsKey(orderNumber)) {
                    mMapIndex.put(orderNumber, x);
                }
            }
        }
        Set<String> sectionLetters = mMapIndex.keySet();
        // create a list from the set to sort
        mSectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(mSectionList);

        mSections = new String[mSectionList.size()];
        mSectionList.toArray(mSections);
    }

    private String getSection(OrderObject orderObject) {
        return orderObject.getOrderNumber() + "";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderObject data = listOrders.get(position);
        if (data != null) {
            holder.tvProductName.setText(data.getProductName());
            holder.tvBrand.setText(data.getBrandName());
            holder.tvOrderNo.setText(context.getResources().getString(R.string.order_no) + ". " + data.getOrderId());
            holder.tvPrice.setText(context.getResources().getString(R.string.lbl_currency) + data.getTotalPrice() + "");
            holder.tvStatus.setText(data.getStatus());
            holder.tvOrderNumber.setText("#" + data.getOrderNumber());
            Glide.with(context).load(data.getImage()).into(holder.ivProduct);

            String section = getSection(data);
            boolean bShowSection = mMapIndex.get(section) == position;
            holder.tvOrderNumber.setVisibility(bShowSection ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct;
        private TextView tvProductName, tvBrand, tvOrderNo, tvPrice, tvStatus, tvOrderNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvOrderNo = itemView.findViewById(R.id.tv_order_no);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvOrderNumber = itemView.findViewById(R.id.tv_order_number);
            ivProduct = itemView.findViewById(R.id.iv_product);

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
