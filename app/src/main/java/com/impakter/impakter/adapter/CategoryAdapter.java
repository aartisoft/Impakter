package com.impakter.impakter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.impakter.impakter.R;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.object.CategoryObj;
import com.impakter.impakter.object.HomeCategoryRespond;
import com.impakter.impakter.object.ProductObj;
import com.impakter.impakter.transition.CustPagerTransformer;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Activity context;
    private List<HomeCategoryRespond.Data> listCategories = new ArrayList<>();
    private LayoutInflater inflater;
    private ProductAdapter productAdapter;
    private ImageCategoryViewPagerAdapter imageViewPagerAdapter;

    public CategoryAdapter(Activity context, List<HomeCategoryRespond.Data> listCategories) {
        this.context = context;
        this.listCategories = listCategories;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        HomeCategoryRespond.Data categoryObj = listCategories.get(position);
        if (categoryObj != null) {
            holder.tvCategoryName.setText(categoryObj.getCategoryName());
            productAdapter = new ProductAdapter(context, categoryObj.getListProduct());
            holder.rcvProduct.setAdapter(productAdapter);

            imageViewPagerAdapter = new ImageCategoryViewPagerAdapter(context, categoryObj.getSubCatArray());
            holder.viewPager.setAdapter(imageViewPagerAdapter);
            if (categoryObj.getSubCatArray().size() == 0) {
                holder.layoutViewpager.setVisibility(View.GONE);
            } else {
                holder.layoutViewpager.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listCategories.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private RecyclerView rcvProduct;
        private ViewPager viewPager;
        private PageIndicatorView pageIndicatorView;
        private RelativeLayout layoutViewpager;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.category_name);
            rcvProduct = itemView.findViewById(R.id.rcv_product);
            rcvProduct.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            rcvProduct.setHasFixedSize(true);

            pageIndicatorView = itemView.findViewById(R.id.pageIndicatorView);
            viewPager = itemView.findViewById(R.id.viewpager);
            viewPager.setPageTransformer(false, new CustPagerTransformer(context));
            layoutViewpager = itemView.findViewById(R.id.layout_image);
        }
    }
}
