package com.impakter.impakter.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.impakter.impakter.R;
import com.impakter.impakter.object.CategoryObj;
import com.impakter.impakter.object.MenuCategoryRespond;

import java.util.List;

public class CategorySpinnerAdapter extends BaseAdapter {
    private Activity context;
    private List<CategoryObj> listMenuCategories;
    private LayoutInflater inflater;

    public CategorySpinnerAdapter(Activity context, List<CategoryObj> listMenuCategories) {
        this.context = context;
        this.listMenuCategories = listMenuCategories;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listMenuCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return listMenuCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryObj data = listMenuCategories.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_spiner, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(data.getCatName());
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
    }
}
