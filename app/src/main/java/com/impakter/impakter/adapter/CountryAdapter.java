package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.impakter.impakter.R;
import com.impakter.impakter.object.CountryRespond;

import java.util.List;

public class CountryAdapter extends BaseAdapter {
    private Activity context;
    private List<CountryRespond.Data> listCountry;
    private LayoutInflater inflater;

    public CountryAdapter(Activity context, List<CountryRespond.Data> listCountry) {
        this.context = context;
        this.listCountry = listCountry;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listCountry.size();
    }

    @Override
    public Object getItem(int position) {
        return listCountry.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CountryRespond.Data data = listCountry.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_common, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(data.getTitle());
        return convertView;
    }


    class ViewHolder {
        private TextView tvTitle;
    }
}
