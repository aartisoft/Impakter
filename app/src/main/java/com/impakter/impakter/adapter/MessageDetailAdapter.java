package com.impakter.impakter.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.impakter.impakter.R;
import com.impakter.impakter.config.PreferencesManager;
import com.impakter.impakter.object.MessageObj;
import com.impakter.impakter.object.MessageRespond;
import com.impakter.impakter.utils.DateTimeUtility;
import com.impakter.impakter.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageDetailAdapter extends RecyclerView.Adapter {
    private final int TYPE_CHAT_LEFT = 1;
    private final int TYPE_CHAT_RIGHT = 2;

    private Activity context;
    private List<MessageObj> listMessages = new ArrayList<>();
    private LayoutInflater inflater;
    private int userId;
    private PreferencesManager preferencesManager;

    public MessageDetailAdapter(Activity context, List<MessageObj> listMessages) {
        this.context = context;
        this.listMessages = listMessages;
        inflater = LayoutInflater.from(context);
        preferencesManager = PreferencesManager.getInstance(context);
        if (preferencesManager.getUserLogin() != null)
            userId = preferencesManager.getUserLogin().getId();
    }

    public MessageDetailAdapter(Activity context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        MessageObj data = listMessages.get(position);
        int senderId = data.getSenderId();
        if (senderId == userId) {
            return TYPE_CHAT_RIGHT;
        } else {
            return TYPE_CHAT_LEFT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CHAT_LEFT) {
            View view = inflater.inflate(R.layout.item_list_chat_left, parent, false);
            return new ItemLeftViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_list_chat_right, parent, false);
            return new ItemRightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MessageObj data = listMessages.get(position);
        MessageObj.FileAttach fileAttach = data.getFileAttach();
        if (holder instanceof ItemLeftViewHolder) {
            try {
                ((ItemLeftViewHolder) holder).tvTime.setText(DateUtils.getRelativeTimeSpanString(data.getTime() * 1000L, System.currentTimeMillis(),
                        0, DateUtils.FORMAT_ABBREV_ALL));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((ItemLeftViewHolder) holder).tvContent.setText(data.getContent());
            ((ItemLeftViewHolder) holder).tvContent.setVisibility(data.getContent().trim().length() == 0 ? View.GONE : View.VISIBLE);

            if (fileAttach != null) {
                ((ItemLeftViewHolder) holder).ivImage.setVisibility(fileAttach.getImage().trim().length() == 0 ? View.GONE : View.VISIBLE);

                int w = (int) ((context.getResources().getDisplayMetrics().widthPixels) * 3 / 5F);
                ((ItemLeftViewHolder) holder).ivImage.getLayoutParams().width = w;
                if (fileAttach.getImage().trim().length() != 0)
                    ((ItemLeftViewHolder) holder).ivImage.getLayoutParams().height = w * fileAttach.getHeight() / fileAttach.getWidth();

                ((ItemLeftViewHolder) holder).tvContent.getLayoutParams().width = w;
                Glide.with(context).load(fileAttach.getImage()).into(((ItemLeftViewHolder) holder).ivImage);
            }

            Glide.with(context).load(data.getReceiverAvatar()).into(((ItemLeftViewHolder) holder).ivAvatar);
        } else {
            try {
                ((ItemRightViewHolder) holder).tvTime.setText(DateUtils.getRelativeTimeSpanString(data.getTime() * 1000L, System.currentTimeMillis(),
                        0, DateUtils.FORMAT_ABBREV_ALL));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((ItemRightViewHolder) holder).tvContent.setText(data.getContent());
            ((ItemRightViewHolder) holder).tvContent.setVisibility(data.getContent().trim().length() == 0 ? View.GONE : View.VISIBLE);

            if (fileAttach != null) {
                ((ItemRightViewHolder) holder).ivImage.setVisibility(fileAttach.getImage().trim().length() == 0 ? View.GONE : View.VISIBLE);
                int w = (int) ((context.getResources().getDisplayMetrics().widthPixels) * 3 / 5F);
                ((ItemRightViewHolder) holder).ivImage.getLayoutParams().width = w;
                if (fileAttach.getImage().trim().length() != 0)
                    ((ItemRightViewHolder) holder).ivImage.getLayoutParams().height = w * fileAttach.getHeight() / fileAttach.getWidth();

                ((ItemRightViewHolder) holder).tvContent.getLayoutParams().width = w;
                Glide.with(context).load(fileAttach.getImage()).into(((ItemRightViewHolder) holder).ivImage);
            }
            Glide.with(context).load(data.getSenderAvatar()).into(((ItemRightViewHolder) holder).ivAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }

    class ItemLeftViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivAvatar;
        private ImageView ivImage;
        private TextView tvContent;
        private TextView tvTime;

        public ItemLeftViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            ivImage = itemView.findViewById(R.id.image_view);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

    class ItemRightViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivAvatar;
        private ImageView ivImage;
        private TextView tvContent;
        private TextView tvTime;

        public ItemRightViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            ivImage = itemView.findViewById(R.id.image_view);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
