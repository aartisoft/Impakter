package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.impakter.impakter.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    private Activity context;
    private List<String> listColors = new ArrayList<>();
    private LayoutInflater inflater;

    public ColorAdapter(Activity context, List<String> listColors) {
        this.context = context;
        this.listColors = listColors;
        inflater = LayoutInflater.from(context);
        fakeData();
    }

    private void fakeData() {
//        listColors.add("")
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_color, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listColors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivColor;
        private Button btnColor;

        public ViewHolder(View itemView) {
            super(itemView);
            ivColor = itemView.findViewById(R.id.iv_color);
            btnColor = itemView.findViewById(R.id.bt_color);
        }
    }
}
