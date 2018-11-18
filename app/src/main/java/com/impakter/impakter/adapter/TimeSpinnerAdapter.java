package com.impakter.impakter.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.impakter.impakter.R;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.object.TimeObj;

import java.util.ArrayList;

public class TimeSpinnerAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<TimeObj> listTypeTime;
    private LayoutInflater inflater;

    public TimeSpinnerAdapter(Activity context, ArrayList<TimeObj> listTypeTime) {
        this.context = context;
        this.listTypeTime = listTypeTime;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listTypeTime.size();
    }

    @Override
    public Object getItem(int position) {
        return listTypeTime.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimeObj timeObj = listTypeTime.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_spiner, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(timeObj.getTypeName());
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
    }
}
