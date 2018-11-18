package com.impakter.impakter.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.solver.GoalRow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.BrandDetailActivity;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.fragment.BrandDetailFragment;
import com.impakter.impakter.object.BrandObj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<BrandObj> listBrands;
    private LayoutInflater inflater;

    private LinkedHashMap<String, Integer> mMapIndex;
    private ArrayList<String> mSectionList;
    private String[] mSections;


    public BrandAdapter(Activity context, ArrayList<BrandObj> listBrands) {
        this.context = context;
        this.listBrands = listBrands;
        inflater = LayoutInflater.from(context);
    }

    public void fillSections() {
        mMapIndex = new LinkedHashMap<String, Integer>();

        for (int x = 0; x < listBrands.size(); x++) {
            String subCategoryName = listBrands.get(x).getTypeBrand();
            if (subCategoryName.length() > 0) {
                if (!mMapIndex.containsKey(subCategoryName)) {
                    mMapIndex.put(subCategoryName, x);
                }
            } else {
                if (!mMapIndex.containsKey("ALL")) {
                    mMapIndex.put("ALL", x);
                }
            }
        }
        Set<String> sectionLetters = mMapIndex.keySet();
        // create a list from the set to sort
        mSectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(mSectionList);

        mSections = new String[mSectionList.size()];
        mSectionList.toArray(mSections);
    }

    private String getSection(BrandObj brandObj) {
        return brandObj.getTypeBrand();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_brand, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BrandObj brandObj = listBrands.get(position);
        String section = getSection(listBrands.get(position));
        if (section.equals(""))
            section = "ALL";
        boolean bShowSection = mMapIndex.get(section) == position;
        holder.tvTypeBrand.setVisibility(bShowSection ? View.VISIBLE : View.GONE);
        holder.line.setVisibility(bShowSection ? View.VISIBLE : View.GONE);
        if (position == 0) holder.line.setVisibility(View.GONE);

        if (brandObj.getTypeBrand().equals("")) {
            holder.tvTypeBrand.setText("ALL");
        } else {
            holder.tvTypeBrand.setText(brandObj.getTypeBrand());
        }

        holder.tvBrandName.setText(brandObj.getName());

        Glide.with(context).load(brandObj.getAvatar()).into(holder.ivBrand);

    }

    @Override
    public int getItemCount() {
        return listBrands.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTypeBrand;
        private TextView tvBrandName, tvDescription;
        private View line;
        private CircleImageView ivBrand;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTypeBrand = itemView.findViewById(R.id.tv_type_brand);
            tvBrandName = itemView.findViewById(R.id.tv_brand_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            line = itemView.findViewById(R.id.line);
            ivBrand = itemView.findViewById(R.id.iv_brand);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.SELLER_ID, listBrands.get(getAdapterPosition()).getId());
                    Intent intent = new Intent(context, BrandDetailActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
