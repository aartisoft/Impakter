package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.object.BrandObj;
import com.impakter.impakter.object.CourierObj;
import com.impakter.impakter.object.CourierRespond;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class CourierAdapter extends RecyclerView.Adapter<CourierAdapter.ViewHolder> {
    private Activity context;
    private List<CourierObj> listCouriers;
    private LayoutInflater inflater;
    private int lastSelectedPosition = 0;

    public CourierAdapter(Activity context, List<CourierObj> listCouriers) {
        this.context = context;
        this.listCouriers = listCouriers;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_sub_shipping_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourierObj data = listCouriers.get(position);

        holder.tvType.setText(data.getType());
        holder.tvTypeName.setText(data.getTypeName());
        holder.tvPickUpAvailable.setText(data.getPickupAvailable());
        holder.tvDeliveryTime.setText(data.getDeliveryTime());
        holder.tvTaxDuties.setText(context.getResources().getString(R.string.lbl_currency) + data.getTaxDuties());
        holder.tvShipmentCharge.setText(context.getResources().getString(R.string.lbl_currency) + data.getShipmentCharge());
        holder.tvTotal.setText(context.getResources().getString(R.string.lbl_currency) + data.getTotal());

        holder.radioButton.setChecked(lastSelectedPosition == position);
        data.setCheck(lastSelectedPosition == position);

        Glide.with(context).load(data.getImage()).into(holder.ivShipmentType);
    }

    @Override
    public int getItemCount() {
        return listCouriers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBrandName, tvType, tvTypeName, tvPickUpAvailable, tvDeliveryTime;
        private TextView tvTaxDuties, tvShipmentCharge, tvTotal;
        private RadioButton radioButton;
        private ImageView ivShipmentType;

        public ViewHolder(View itemView) {
            super(itemView);
            ivShipmentType = itemView.findViewById(R.id.iv_shipment_type);
            radioButton = itemView.findViewById(R.id.rad_button);

            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvType = itemView.findViewById(R.id.tv_type);
            tvTypeName = itemView.findViewById(R.id.tv_type_name);
            tvPickUpAvailable = itemView.findViewById(R.id.tv_available);
            tvDeliveryTime = itemView.findViewById(R.id.tv_delivery_time);
            tvTaxDuties = itemView.findViewById(R.id.tv_tax);
            tvShipmentCharge = itemView.findViewById(R.id.tv_shipment_charge);
            tvTotal = itemView.findViewById(R.id.tv_total);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                }
            });
        }
    }

}
