package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.impakter.impakter.R;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.events.OnLoadMoreListener;
import com.impakter.impakter.object.NotificationRespond;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import java.util.ArrayList;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter {
    private static final int VIEW_PRODUCT = 1;
    private static final int LIKE_PRODUCT = 2;
    private static final int UP_VOTE = 3;
    private static final int RATE_PRODUCT = 4;
    private static final int FOLLOW = 5;
    private static final int MESSAGE = 6;
    private static final int COMMENT_PRODUCT = 7;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private Activity context;
    private List<NotificationRespond.Data> listActivities = new ArrayList<>();
    private LayoutInflater inflater;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private OnItemClickListener onItemClickListener;

    public ActivityAdapter(RecyclerView recyclerView, Activity context, List<NotificationRespond.Data> listActivities) {
        this.context = context;
        this.listActivities = listActivities;
        inflater = LayoutInflater.from(context);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return listActivities.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View view = inflater.inflate(R.layout.item_list_activity, parent, false);
            return new ViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_loading, parent, false);
            return new ProgressViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NotificationRespond.Data data = listActivities.get(position);
        if (data != null) {
            if (holder instanceof ViewHolder) {
                ((ViewHolder) holder).tvDescription.setText(Html.fromHtml(data.getContent()));
                if (data.getType() == MESSAGE ||data.getType() == COMMENT_PRODUCT) {
                    ((ViewHolder) holder).ivType.setImageResource(R.drawable.ic_black_chatting);
                } else if (data.getType() == LIKE_PRODUCT) {
                    ((ViewHolder) holder).ivType.setImageResource(R.drawable.ic_black_favorite);
                } else {
                    ((ViewHolder) holder).ivType.setImageResource(R.drawable.ic_black_profile);
                }
            } else {
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            }
        }
    }


    @Override
    public int getItemCount() {
        return listActivities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivType;
        private TextViewHeeboRegular tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ivType = itemView.findViewById(R.id.iv_type);
            tvDescription = itemView.findViewById(R.id.tv_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
