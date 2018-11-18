package com.impakter.impakter.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.impakter.impakter.R;
import com.impakter.impakter.events.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ListAppAdapter extends RecyclerView.Adapter<ListAppAdapter.ViewHolder> {
    private Context context;
    private List<String> listPackages;
    private LayoutInflater inflater;
    private PackageManager mPm;
    private OnItemClickListener onItemClickListener;

    public ListAppAdapter(Context context, List<String> listPackages) {
        this.context = context;
        this.listPackages = listPackages;
        mPm = context.getPackageManager();
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String pkg = listPackages.get(position);
        try {
            ApplicationInfo ai = mPm.getApplicationInfo(pkg, 0);

            CharSequence appName = mPm.getApplicationLabel(ai);
            Drawable appIcon = mPm.getApplicationIcon(pkg);
            holder.ivApp.setImageDrawable(appIcon);
//            textView.setCompoundDrawablesWithIntrinsicBounds(appIcon, null, null, null);
//            textView.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().getResources().getDisplayMetrics()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void invokeApplication(String packageName, ResolveInfo resolveInfo) {
        // if(packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana") || packageName.contains("com.kakao.story")) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi guys, I found amazing thing to share. Send your love and care in form of gifts to your loved ones from anywhere in world. Log on to giftjaipur.com or download app" + "https://goo.gl/YslIVT" + "and use coupon code app50 to get Rs 50 off on your first purchase.");
        intent.putExtra(Intent.EXTRA_SUBJECT, "GiftJaipur 50 Rs Coupon...");
        intent.setPackage(packageName);

        context.startActivity(intent);
        // }
    }

    @Override
    public int getItemCount() {
        return listPackages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivApp;

        public ViewHolder(View itemView) {
            super(itemView);
            ivApp = itemView.findViewById(R.id.iv_app);
            ivApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(ivApp, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
