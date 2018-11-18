package com.impakter.impakter.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.impakter.impakter.R;
import com.impakter.impakter.object.TimeObj;

import java.util.ArrayList;

public class StatusSpinnerAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<TimeObj> listTypeTime = new ArrayList<>();
    private LayoutInflater inflater;

    public StatusSpinnerAdapter(Activity context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        initdata();
    }

    private void initdata() {
        listTypeTime.add(new TimeObj(-1, context.getResources().getString(R.string.all)));
        listTypeTime.add(new TimeObj(1, context.getResources().getString(R.string.cancel)));
        listTypeTime.add(new TimeObj(2, context.getResources().getString(R.string.draft)));
        listTypeTime.add(new TimeObj(3, context.getResources().getString(R.string.lbl_new)));
        listTypeTime.add(new TimeObj(4, context.getResources().getString(R.string.inprogress)));
        listTypeTime.add(new TimeObj(5, context.getResources().getString(R.string.to_ship)));
        listTypeTime.add(new TimeObj(6, context.getResources().getString(R.string.shipped)));
        listTypeTime.add(new TimeObj(7, context.getResources().getString(R.string.delivered)));
        listTypeTime.add(new TimeObj(8, context.getResources().getString(R.string.responsive_required)));
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
