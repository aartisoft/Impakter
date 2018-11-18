package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.impakter.impakter.R;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import java.util.ArrayList;
import java.util.List;

public class CreditCardAdapter extends RecyclerView.Adapter<CreditCardAdapter.ViewHolder> {
    private Activity context;
    private List<String> listActivities = new ArrayList<>();
    private LayoutInflater inflater;
    private boolean isEdit = false;

    public CreditCardAdapter(Activity context, List<String> listActivities) {
        this.context = context;
        this.listActivities = listActivities;
        inflater = LayoutInflater.from(context);
    }

    public CreditCardAdapter(Activity context, boolean isEdit) {
        this.context = context;
        this.isEdit = isEdit;
        inflater = LayoutInflater.from(context);
        fakeData();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_credit_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    private void fakeData() {
        listActivities.add("a");
        listActivities.add("a");
        listActivities.add("a");
        listActivities.add("a");
        listActivities.add("a");
        listActivities.add("a");
        listActivities.add("a");
        listActivities.add("a");
    }

    @Override
    public int getItemCount() {
        return listActivities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivDelete;
        private TextViewHeeboRegular tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            tvDescription = itemView.findViewById(R.id.tv_description);

            if (isEdit) {
                ivDelete.setVisibility(View.VISIBLE);
            } else {
                ivDelete.setVisibility(View.GONE);
            }
        }
    }
}
