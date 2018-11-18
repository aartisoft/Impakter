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
import com.impakter.impakter.object.FolderImageObj;

import java.util.ArrayList;

public class PhotoFolderAdapter extends RecyclerView.Adapter<PhotoFolderAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<FolderImageObj> listPhotoFolder;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public PhotoFolderAdapter(Activity context, ArrayList<FolderImageObj> listPhotoFolder) {
        this.context = context;
        this.listPhotoFolder = listPhotoFolder;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_photo_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FolderImageObj folderImageObj = listPhotoFolder.get(position);
        holder.tvFolder.setText(folderImageObj.getFolder());
        holder.tvNumberImage.setText(folderImageObj.getListImages().size() + " images");
        Glide.with(context).load("file://" + folderImageObj.getListImages().get(0))
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return listPhotoFolder.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvFolder;
        private TextView tvNumberImage;

        public ViewHolder(final View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvFolder = itemView.findViewById(R.id.tv_folder);
            tvNumberImage = itemView.findViewById(R.id.tv_number_image);

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
