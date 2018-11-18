package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.object.CourierObj;
import com.impakter.impakter.object.ShipmentObj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentAdapter.ViewHolder> {
    private Activity context;
    private List<ShipmentObj> listShipmentService;
    private LayoutInflater inflater;


    public ShipmentAdapter(Activity context, List<ShipmentObj> listShipmentService) {
        this.context = context;
        this.listShipmentService = listShipmentService;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_shipping_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ShipmentObj data = listShipmentService.get(position);

        holder.tvBrandName.setText(data.getBrandName());

        CourierAdapter courierAdapter = new CourierAdapter(context, data.getListCourier());
        holder.rcvCourier.setAdapter(courierAdapter);

        if (data.getListCourier().size() == 0) {
            holder.tvNoData.setVisibility(View.VISIBLE);
            holder.rcvCourier.setVisibility(View.GONE);
        } else {
            holder.tvNoData.setVisibility(View.GONE);
            holder.rcvCourier.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return listShipmentService.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBrandName;
        private RecyclerView rcvCourier;
        private TextView tvNoData;

        public ViewHolder(View itemView) {
            super(itemView);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvNoData = itemView.findViewById(R.id.tv_no_data);

            rcvCourier = itemView.findViewById(R.id.rcv_courier);
            rcvCourier.setHasFixedSize(true);
            rcvCourier.setLayoutManager(new LinearLayoutManager(context));
        }
    }
}
