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

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter {
    private static final int TYPE_TAKE_PHOTO = 0;
    private static final int TYPE_ITEM = 1;
    private Activity context;
    private ArrayList<String> listImages;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public ImageAdapter(Activity context, ArrayList<String> listImages) {
        this.context = context;
        this.listImages = listImages;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TAKE_PHOTO) {
            View view = inflater.inflate(R.layout.layout_item_take_photo, parent, false);
            return new TakePhotoViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_list_image_change_avatar, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Glide.with(context).load(new File(listImages.get(position))).into(((ViewHolder) holder).imageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? TYPE_TAKE_PHOTO : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listImages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }
    }

    class TakePhotoViewHolder extends RecyclerView.ViewHolder {

        public TakePhotoViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
