package com.impakter.impakter.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.ImageViewPagerAdapter;
import com.impakter.impakter.adapter.ViewPagerAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.config.GlobalValue;
import com.impakter.impakter.dblocal.configrealm.DbContext;
import com.impakter.impakter.dblocal.realmobject.CartItemRealmObj;
import com.impakter.impakter.events.OnAddToBagClickListener;
import com.impakter.impakter.events.OnBuyNowClickListener;
import com.impakter.impakter.fragment.AboutFragment;
import com.impakter.impakter.fragment.BottomSheetFavouriteFragment;
import com.impakter.impakter.fragment.BottomSheetShareFragment;
import com.impakter.impakter.fragment.CommentFragment;
import com.impakter.impakter.fragment.DetailFragment;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.CartIdRespond;
import com.impakter.impakter.object.CartItemObj;
import com.impakter.impakter.object.ProductDetailRespond;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends BaseActivity implements View.OnClickListener, OnAddToBagClickListener, OnBuyNowClickListener {
    private ImageView ivShare, ivFavourite, ivBack, ivShopping, ivOption;
    private Spinner spQuantity, spSize, spColor;
    private ViewPager viewPagerImage, viewPagerProduct;
    private SmartTabLayout tabLayout;
    private ImageViewPagerAdapter imageViewPagerAdapter;
    private List<String> listImages = new ArrayList<>();
    private List<ProductDetailRespond.MoreFromThisBrand> listMoreImages = new ArrayList<>();

    private TextView tvProductName, tvBrand, tvPrice;
    private MaterialRatingBar ratingBar;

    private PageIndicatorView pageIndicatorView;

    private int id, sellerId;
    private String userId;
    private int quantity;
    private String size, color;
    private int cartId, numberCartItem;
    private List<String> listSize = new ArrayList<>();
    private List<String> listColor = new ArrayList<>();
    private List<Integer> listQuantity = new ArrayList<>();
    private String description;
    private String introduction;

    private ProductDetailRespond.Data productItem;
    private int brandId;
    private String productName, image, brand, code, option;
    private float price, totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            Window w = getWindow();
//            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        initViews();
        getDataFromIntent();
        initData();
        initControl();
    }

    private void initViews() {
        ivShare = findViewById(R.id.iv_share);
        ivFavourite = findViewById(R.id.iv_favourite);
        ivBack = findViewById(R.id.iv_back);
        ivShopping = findViewById(R.id.iv_shopping);
        ivOption = findViewById(R.id.iv_option);

        viewPagerImage = findViewById(R.id.viewpager_image);
        viewPagerProduct = findViewById(R.id.viewpager_product);
//        viewPagerProduct.setOffscreenPageLimit(3);

        tabLayout = findViewById(R.id.tab_detail);

        pageIndicatorView = findViewById(R.id.pageIndicatorView);

        tvProductName = findViewById(R.id.tv_product_name);
        tvProductName.setSelected(true);
        tvBrand = findViewById(R.id.tv_brand);
        tvBrand.setPaintFlags(tvBrand.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvPrice = findViewById(R.id.tv_price);

        ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        spQuantity = findViewById(R.id.sp_quantity);
        spSize = findViewById(R.id.sp_size);
        spColor = findViewById(R.id.sp_color);

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        id = intent.getIntExtra(Constants.PRODUCT_ID, -1);
    }

    private void initData() {
        if (isLoggedIn())
            userId = preferencesManager.getUserLogin().getId() + "";
        getProductDetail(id);
//        getMyCartInfo();

    }

    private void initControl() {
        tvBrand.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivShopping.setOnClickListener(this);
        ivOption.setOnClickListener(this);
        ivFavourite.setOnClickListener(this);
        ivShare.setOnClickListener(this);

        spQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quantity = listQuantity.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size = listSize.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = listColor.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getMyCartInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) self.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String ime = telephonyManager.getDeviceId();
        ConnectServer.getResponseAPI().getCartId(userId, ime).enqueue(new Callback<CartIdRespond>() {
            @Override
            public void onResponse(Call<CartIdRespond> call, Response<CartIdRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        cartId = response.body().getData().getId();
                        numberCartItem = response.body().getData().getCountCartItem();
                        preferencesManager.setCartId(cartId);
                    }
                }
            }

            @Override
            public void onFailure(Call<CartIdRespond> call, Throwable t) {

            }
        });
    }

    private void getProductDetail(int id) {
        showDialog();
        ConnectServer.getResponseAPI().getProductDetail(id).enqueue(new Callback<ProductDetailRespond>() {
            @Override
            public void onResponse(@NonNull Call<ProductDetailRespond> call, @NonNull Response<ProductDetailRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        productItem = response.body().getData();

                        sellerId = response.body().getData().getSellerId();
                        // List images may you also like
                        listMoreImages.clear();
                        listMoreImages.addAll(response.body().getData().getMoreFromThisBrand());
                        introduction = response.body().getData().getIntroduction();
                        description = response.body().getData().getDescription();
                        brand = response.body().getData().getBrandName();

                        setupViewPager(viewPagerProduct);

                        productName = response.body().getData().getTitle();
                        image = response.body().getData().getImages().get(0);
                        brandId = response.body().getData().getSellerId();
                        code = response.body().getData().getCountryOfManufacture();
                        price = response.body().getData().getPricePrimary();

                        tvProductName.setText(response.body().getData().getTitle());
                        tvBrand.setText(response.body().getData().getBrandName());
                        tvPrice.setText(getResources().getString(R.string.lbl_currency) + response.body().getData().getPricePrimary());
                        ratingBar.setRating(response.body().getData().getAverageRating());

                        listImages.clear();
                        listImages.addAll(response.body().getData().getImages());
                        imageViewPagerAdapter = new ImageViewPagerAdapter(DetailActivity.this, listImages);
                        viewPagerImage.setAdapter(imageViewPagerAdapter);

                        quantity = response.body().getData().getQuantity();
                        listQuantity.clear();
                        for (int i = 1; i <= quantity; i++) {
                            listQuantity.add(i);
                        }
                        ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<>(self, android.R.layout.simple_list_item_1, listQuantity);
                        spQuantity.setAdapter(quantityAdapter);

                        listSize.clear();
                        listSize.addAll(response.body().getData().getOptionAttribute().getSize());
                        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(self, android.R.layout.simple_list_item_1, listSize);
                        spSize.setAdapter(sizeAdapter);

                        listColor.clear();
                        listColor.addAll(response.body().getData().getOptionAttribute().getColor());
                        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(self, android.R.layout.simple_list_item_1, listColor);
                        spColor.setAdapter(colorAdapter);


                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(@NonNull Call<ProductDetailRespond> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                showToast(t.getMessage());
                closeDialog();
            }
        });
    }

    private void getData() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        DetailFragment detailFragment = new DetailFragment();
        CommentFragment commentFragment = new CommentFragment();
        AboutFragment aboutFragment = new AboutFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.DESCRIPTION, description);
        bundle.putString(Constants.BRAND_NAME,brand);
        bundle.putInt(Constants.PRODUCT_ID, id);
        bundle.putString(Constants.SIZE, size);
        bundle.putString(Constants.COLOR, color);
        bundle.putInt(Constants.QUANTITY, quantity);
        bundle.putParcelableArrayList(Constants.ARR_IMAGE, (ArrayList<? extends Parcelable>) listMoreImages);
        bundle.putString(Constants.INTRODUCTION, introduction);
        bundle.putInt(Constants.SELLER_ID, sellerId);

        detailFragment.setArguments(bundle);
        commentFragment.setArguments(bundle);
        aboutFragment.setArguments(bundle);

        adapter.addFrag(detailFragment, getResources().getString(R.string.detail));
        adapter.addFrag(commentFragment, getResources().getString(R.string.comments));
        adapter.addFrag(aboutFragment, getResources().getString(R.string.about));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setViewPager(viewPager);
    }

    public void showBottomSheetDialog(int productId) {
        BottomSheetFavouriteFragment bottomSheetFragment = new BottomSheetFavouriteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.PRODUCT_ID, productId);
        bottomSheetFragment.setArguments(bundle);
        bottomSheetFragment.show(getSupportFragmentManager(), null);
    }

    private void showBuyProductDialog(ProductDetailRespond.Data product) {
        BuyProductDialog buyProductDialog = new BuyProductDialog(self, product);
        buyProductDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_brand:
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.SELLER_ID, sellerId);
                gotoActivity(self, BrandDetailActivity.class, bundle);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_shopping:
                showBuyProductDialog(productItem);
                break;
            case R.id.iv_option:

                break;
            case R.id.iv_share:
                showShareDialog();
//                BottomSheetShareFragment bottomSheetShareFragment = new BottomSheetShareFragment();
//                bottomSheetShareFragment.show(getSupportFragmentManager(), null);
                break;
            case R.id.iv_favourite:
                if (isLoggedIn()) {
                    showBottomSheetDialog(id);
                } else {
                    showConfirmLoginDialog();
                }
                break;

        }
    }

    private void showShareDialog() {
        ShareDialog shareDialog = new ShareDialog(self, id);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(shareDialog.getWindow().getAttributes());
        lp.dimAmount = 0.7f;
        lp.gravity = Gravity.BOTTOM;
        lp.width = self.getResources().getDisplayMetrics().widthPixels;
        lp.height = (int) (self.getResources().getDisplayMetrics().heightPixels * 9 / 11F);
        shareDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        shareDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        shareDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        shareDialog.show();
        shareDialog.getWindow().setAttributes(lp);
        shareDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void addToCart() {
        showDialog();
        ConnectServer.getResponseAPI().addToCart(userId, id, GlobalValue.cartId, size, color, quantity).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        showBottomSheetDialog();
                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<BaseMessageRespond> call, Throwable t) {
                closeDialog();
                showToast(t.getMessage());
            }
        });
    }

    private void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_add_to_bag_dialog, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(self);
        dialog.setCancelable(false);
        dialog.setContentView(view);

        LinearLayout btnKeepShopping = view.findViewById(R.id.btn_keep_shopping);
        LinearLayout btnBuyNow = view.findViewById(R.id.btn_buy_now);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnKeepShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MyBagActivity.class);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onAddToBagClick() {
        if (isLoggedIn()) {
            addToCart();
        } else {
            addCartItemToLocal();
        }

    }

    private void addCartItemToLocal() {
        option = size + "/" + color;
        DbContext.init(self);
        DbContext dbContext = DbContext.getInstance();
        String primaryKey = id + option;
        CartItemRealmObj cartItemObj = new CartItemRealmObj();
        cartItemObj.setPrimaryKeyItem(primaryKey);
        cartItemObj.setId(id);
        cartItemObj.setName(productName);
        cartItemObj.setImage(image);
        cartItemObj.setBrand(brand);
        cartItemObj.setBrandId(brandId);
        cartItemObj.setCode(code);
        cartItemObj.setOption(option);
        cartItemObj.setQuantity(quantity);
        cartItemObj.setPrice(price);
        cartItemObj.setTotalPrice(price * quantity);

        //check if an item has existed in db or not
        if (dbContext.isExist(primaryKey)) {
            CartItemRealmObj cartItemRealmObj = dbContext.getCartByKey(primaryKey);
            int oldQuantity = cartItemRealmObj.getQuantity();
            int newQuantity = quantity + oldQuantity;
            cartItemObj.setQuantity(newQuantity);
            cartItemObj.setTotalPrice(price * newQuantity);
        }
        dbContext.addItemToCart(cartItemObj);
        showBottomSheetDialog();
    }

    @Override
    public void onBuyNowClick() {
        gotoActivity(self, MyBagActivity.class);
    }
}
