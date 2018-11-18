package com.impakter.impakter.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.impakter.impakter.R;
import com.impakter.impakter.object.CommonObj;

import java.util.ArrayList;
import java.util.List;

public class CommonAdapter extends BaseAdapter {
    private Activity context;
    private List<CommonObj> listData;
    private LayoutInflater inflater;

    public CommonAdapter(Activity context, List<CommonObj> listData) {
        this.context = context;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonObj commonObj = listData.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_common, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(commonObj.getTitle());
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
    }
}
