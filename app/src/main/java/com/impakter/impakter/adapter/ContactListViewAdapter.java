package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.impakter.impakter.R;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import java.util.ArrayList;
import java.util.List;

public class ContactListViewAdapter extends BaseAdapter {
    private Activity context;
    private List<String> listPeople = new ArrayList<>();
    private LayoutInflater inflater;

    public ContactListViewAdapter(Activity context, List<String> listPeople) {
        this.context = context;
        this.listPeople = listPeople;
        inflater = LayoutInflater.from(context);
        fakeData();
    }


    private void fakeData() {
        listPeople.add("a");
        listPeople.add("a");
        listPeople.add("a");
        listPeople.add("a");
        listPeople.add("a");
        listPeople.add("a");
        listPeople.add("a");
        listPeople.add("a");
        listPeople.add("a");
        listPeople.add("a");
        listPeople.add("a");
    }


    @Override
    public int getCount() {
        return listPeople.size();
    }

    @Override
    public Object getItem(int position) {
        return listPeople.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAvatar;
        private TextViewHeeboRegular tvName, btnAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            btnAdd = itemView.findViewById(R.id.btn_add);
        }
    }
}
