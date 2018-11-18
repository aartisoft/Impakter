package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.impakter.impakter.R;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Activity context;
    private List<String> listPeople = new ArrayList<>();
    private LayoutInflater inflater;

    public ContactAdapter(Activity context, List<String> listPeople) {
        this.context = context;
        this.listPeople = listPeople;
        inflater = LayoutInflater.from(context);
        fakeData();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_contact, parent, false);
        return new ViewHolder(view);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listPeople.size();
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
