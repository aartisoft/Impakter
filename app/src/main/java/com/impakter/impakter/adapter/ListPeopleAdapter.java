package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.object.ContactRespond;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListPeopleAdapter extends RecyclerView.Adapter<ListPeopleAdapter.ViewHolder> {
    private Activity context;
    private List<ContactRespond.Data> listPeople;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public ListPeopleAdapter(Activity context, List<ContactRespond.Data> listPeople) {
        this.context = context;
        this.listPeople = listPeople;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_people, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactRespond.Data data = listPeople.get(position);
        if (data != null) {
            holder.tvName.setText(data.getName());
            Glide.with(context).load(data.getAvatar()).into(holder.ivAvatar);
            if (data.getFollowStatus()) {
                holder.btnFollow.setText(context.getResources().getString(R.string.following));
                holder.btnFollow.setBackgroundResource(R.drawable.bg_button_blue_border);
                holder.btnFollow.setTextColor(context.getResources().getColor(R.color.text_color_blue));
            } else {
                holder.btnFollow.setText(context.getResources().getString(R.string.follow));
                holder.btnFollow.setBackgroundResource(R.drawable.bg_blue_button);
                holder.btnFollow.setTextColor(context.getResources().getColor(R.color.text_color_white));
            }
        }
    }

    @Override
    public int getItemCount() {
        return listPeople.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivAvatar;
        private TextViewHeeboRegular tvName, btnFollow;

        public ViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            btnFollow = itemView.findViewById(R.id.btn_follow);
            btnFollow.setOnClickListener(new View.OnClickListener() {
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
