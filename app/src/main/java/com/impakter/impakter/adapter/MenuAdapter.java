package com.impakter.impakter.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.impakter.impakter.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<String> listMenu;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private int position;

    public MenuAdapter(Activity context, ArrayList<String> listMenu) {
        this.context = context;
        this.listMenu = listMenu;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMenuName.setText(listMenu.get(position));
        if (position == this.position) {
//            holder.tvMenuName.setTextColor(Color.RED);
            holder.tvMenuName.setTextColor(context.getResources().getColor(R.color.text_color_gray));
        } else {
            holder.tvMenuName.setTextColor(context.getResources().getColor(R.color.text_color_gray));
        }
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMenuName;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvMenuName = itemView.findViewById(R.id.tv_menu_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(tvMenuName, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(TextView textView, int position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
