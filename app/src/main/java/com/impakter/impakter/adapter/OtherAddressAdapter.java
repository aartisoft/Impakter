package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.impakter.impakter.R;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import java.util.ArrayList;
import java.util.List;

public class OtherAddressAdapter extends RecyclerView.Adapter<OtherAddressAdapter.ViewHolder> {
    private Activity context;
    private List<String> listActivities = new ArrayList<>();
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public OtherAddressAdapter(Activity context, List<String> listActivities) {
        this.context = context;
        this.listActivities = listActivities;
        inflater = LayoutInflater.from(context);
    }

    public OtherAddressAdapter(Activity context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        fakeData();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_other_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    private void fakeData() {
        listActivities.add("a");
        listActivities.add("a");
        listActivities.add("a");
    }

    @Override
    public int getItemCount() {
        return listActivities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
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
