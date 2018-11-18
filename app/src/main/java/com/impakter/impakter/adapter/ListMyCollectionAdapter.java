package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.object.CollectionObj;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import java.util.List;

public class ListMyCollectionAdapter extends RecyclerView.Adapter<ListMyCollectionAdapter.ViewHolder> {
    private Activity context;
    private List<CollectionObj> listCollections;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public ListMyCollectionAdapter(Activity context, List<CollectionObj> listCollections) {
        this.context = context;
        this.listCollections = listCollections;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_my_collection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollectionObj collectionObj = listCollections.get(position);
        if (collectionObj != null) {
            holder.tvName.setText(collectionObj.getCollectionName());
            Glide.with(context).load(collectionObj.getCollectionImage()).into(holder.ivThumbnail);
        }
    }


    @Override
    public int getItemCount() {
        return listCollections.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivThumbnail;
        private TextViewHeeboRegular tvName, btnAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvName = itemView.findViewById(R.id.tv_name);
            btnAdd = itemView.findViewById(R.id.btn_add);

            btnAdd.setOnClickListener(new View.OnClickListener() {
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
