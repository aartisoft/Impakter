package com.impakter.impakter.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.ContactAdapter;
import com.impakter.impakter.adapter.ImageViewPagerAdapter;
import com.impakter.impakter.adapter.ListAppAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.config.GlobalValue;
import com.impakter.impakter.config.PreferencesManager;
import com.impakter.impakter.dblocal.configrealm.DbContext;
import com.impakter.impakter.dblocal.realmobject.CartItemRealmObj;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.CartIdRespond;
import com.impakter.impakter.object.ProductDetailRespond;
import com.impakter.impakter.widget.dialog.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyProductDialog extends AlertDialog implements View.OnClickListener {
    private final String TAG = BuyProductDialog.class.getSimpleName();

    private ImageView ivProduct;
    private TextView tvProductName, tvBrand, tvDescription, tvPrice;
    private ProgressDialog progressDialog;


    private LinearLayout btnAddToBag, btnBuyNow;
    private Spinner spSize, spQuantity, spColor;
    private MaterialRatingBar ratingBar;

    private int quantity;
    private int cartId, numberCartItem;
    private String color = "", size = "";
    private List<String> listSize = new ArrayList<>();
    private List<String> listColor = new ArrayList<>();
    private List<Integer> listQuantity = new ArrayList<>();
    private String description;
    private String introduction;
    private PreferencesManager preferencesManager;

    private Activity activity;
    private ProductDetailRespond.Data product;
    private int brandId;
    private String productName, image, brand, code, option;
    private float price, totalPrice;
    private String userId;

    public BuyProductDialog(@NonNull Activity context, ProductDetailRespond.Data product) {
        super(context);
        this.product = product;
        this.activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);
        preferencesManager = PreferencesManager.getInstance(getContext());
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        ivProduct = findViewById(R.id.iv_product);

        tvProductName = findViewById(R.id.tv_product_name);
        tvProductName.setSelected(true);
        tvBrand = findViewById(R.id.tv_brand);
        tvBrand.setPaintFlags(tvBrand.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvPrice = findViewById(R.id.tv_price);
        tvDescription = findViewById(R.id.tv_description);

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

        btnAddToBag = findViewById(R.id.btn_add_to_bag);
        btnBuyNow = findViewById(R.id.btn_buy_now);

    }

    private void initData() {
        if (isLoggedIn())
            userId = preferencesManager.getUserLogin().getId() + "";
        setData();
    }

    private void initControl() {
        btnAddToBag.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);

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

    private void setData() {
        introduction = product.getIntroduction();
        description = product.getDescription();

        productName = product.getTitle();
        image = product.getImages().get(0);
        brand = product.getBrandName();
        brandId = product.getSellerId();
        code = product.getCountryOfManufacture();
        price = product.getPricePrimary();

        tvProductName.setText(product.getTitle());
        tvDescription.setText(description);
        tvBrand.setText(product.getBrandName());
        tvPrice.setText(getContext().getResources().getString(R.string.lbl_currency) + product.getPricePrimary());
        ratingBar.setRating(product.getAverageRating());

        quantity = product.getQuantity();
        listQuantity.clear();
        for (int i = 1; i <= quantity; i++) {
            listQuantity.add(i);
        }
        ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listQuantity);
        spQuantity.setAdapter(quantityAdapter);

        listSize.clear();
        listSize.addAll(product.getOptionAttribute().getSize());
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listSize);
        spSize.setAdapter(sizeAdapter);

        listColor.clear();
        listColor.addAll(product.getOptionAttribute().getColor());
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listColor);
        spColor.setAdapter(colorAdapter);
        if (product.getImages().size() != 0)
            Glide.with(getContext()).load(product.getImages().get(0)).into(ivProduct);
    }

    protected void showDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void closeDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    protected boolean isLoggedIn() {
        if (preferencesManager.getUserLogin() == null) {
            return false;
        } else {
            if (preferencesManager.getUserLogin().getId() != 0) {
                return true;
            } else
                return false;
        }
    }

    private void addToCart() {
        showDialog();
        ConnectServer.getResponseAPI().addToCart(userId, product.getId(), GlobalValue.cartId, size, color, quantity).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<BaseMessageRespond> call, Throwable t) {
                closeDialog();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCartItemToLocal() {
        option = size + "/" + color;
        DbContext.init(getContext());
        DbContext dbContext = DbContext.getInstance();
        String primaryKey = product.getId() + option;
        CartItemRealmObj cartItemObj = new CartItemRealmObj();
        cartItemObj.setPrimaryKeyItem(primaryKey);
        cartItemObj.setId(product.getId());
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
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_to_bag:
                if (isLoggedIn()) {
                    addToCart();
                } else {
                    addCartItemToLocal();
                }

                break;
            case R.id.btn_buy_now:
                Intent intent = new Intent(getContext(), MyBagActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
                dismiss();
                break;
        }
    }
}
