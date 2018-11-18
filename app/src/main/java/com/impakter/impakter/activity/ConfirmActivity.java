package com.impakter.impakter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.ProductViewPagerAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.config.GlobalValue;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.CartItemObj;
import com.impakter.impakter.object.CartItemRespond;
import com.impakter.impakter.object.CourierObj;
import com.impakter.impakter.object.UserObj;
import com.impakter.impakter.utils.DateTimeUtility;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmActivity extends BaseActivity implements View.OnClickListener {
    private static String PAYPAY_RESONDE = "response";
    private ImageView ivBack;

    private TextView tvOrderReference, tvOrderDate, tvShipmentInformation, tvShippingAddress, tvCardInfo, tvSubTotal, tvTotalPrice, tvShippingPrice;
    private ImageView ivTypeCard, ivShipmentType;

    private ViewPager viewPager;
    private ProductViewPagerAdapter productViewPagerAdapter;
    private List<CartItemObj> listProducts = new ArrayList<>();

    private Button btnConfirm;

    private List<CourierObj> listCourier = new ArrayList<>();
    private String shippingAddress, cardHolderName, cardNumber, month, year, cvv;
    private String dataShipping, phoneNumber;
    private int paymentMethod;
    private UserObj userObj;
    private String arr[];
    private Calendar calendar;
    private float subTotal = 0, shippingPrice = 0, total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        calendar = Calendar.getInstance();
        initViews();
        initData();
        initControl();

        startPayPalService();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        ivTypeCard = findViewById(R.id.iv_type_card);
        ivShipmentType = findViewById(R.id.iv_type_shipment);

        tvOrderReference = findViewById(R.id.tv_oder_reference);
        tvOrderDate = findViewById(R.id.tv_order_date);
        tvShipmentInformation = findViewById(R.id.tv_shipment_information);
        tvShippingAddress = findViewById(R.id.tv_shipping_address);
        tvCardInfo = findViewById(R.id.tv_card_information);
        tvSubTotal = findViewById(R.id.tv_sub_total);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvShippingPrice = findViewById(R.id.tv_shipping_price);
        btnConfirm = findViewById(R.id.btn_confirm);

        viewPager = findViewById(R.id.viewpager);
    }

    private void initData() {
        if (isLoggedIn()) {
            userObj = preferencesManager.getUserLogin();
        }
        productViewPagerAdapter = new ProductViewPagerAdapter(self, listProducts);
        viewPager.setAdapter(productViewPagerAdapter);
        viewPager.setCurrentItem(0);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                listCourier = bundle.getParcelableArrayList(Constants.ARR_COURIER);
                shippingAddress = bundle.getString(Constants.SHIPPING_ADDRESS);
                cardNumber = bundle.getString(Constants.CARD_NUMBER);
                cardHolderName = bundle.getString(Constants.CARD_HOLDER_NAME);
                month = bundle.getString(Constants.MONTH);
                year = bundle.getString(Constants.YEAR);
                cvv = bundle.getString(Constants.CVV);
                phoneNumber = bundle.getString(Constants.PHONE_NUMBER);
                paymentMethod = bundle.getInt(Constants.PAYMENT_METHOD);
                dataShipping = bundle.getString(Constants.DATA_SHIPPING);
                arr = shippingAddress.split(",");
                Log.e(TAG, "size: " + bundle.getParcelableArrayList(Constants.ARR_COURIER).size());
            }
        }
        if (paymentMethod == Constants.PAYPAL) {
            ivTypeCard.setImageResource(R.drawable.ic_paypal);
        } else {
            ivTypeCard.setImageResource(R.drawable.ic_master_card);
        }
        for (CourierObj courierObj : listCourier) {
            shippingPrice += courierObj.getShipmentCharge();
            tvShippingPrice.setText(getResources().getString(R.string.lbl_currency) + shippingPrice);
        }
        tvOrderDate.setText(DateTimeUtility.convertTimeStampToDate(calendar.getTimeInMillis() / 1000L + "", "dd/MM/yy"));
        tvShippingAddress.setText(shippingAddress);

        String lastFourDigit = cardNumber.substring(cardNumber.length() - 4);
        tvCardInfo.setText("xxxx xxxx xxxx " + lastFourDigit + "   " + month + "/" + year);


        getCartItems(GlobalValue.cartId);
    }

    private String createJonDataPayment() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentMethod", paymentMethod + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "createJonDataPayment: " + jsonObject.toString());
        return jsonObject.toString();
    }

    private String createJsonDataOrder() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cartId", GlobalValue.cartId + "");
            jsonObject.put("userId", userObj.getId() + "");
            jsonObject.put("firstName", userObj.getFirstName());
            jsonObject.put("lastName", userObj.getLastName());
            jsonObject.put("email", userObj.getEmail());
            jsonObject.put("address", userObj.getAddress());
            jsonObject.put("address2", userObj.getAddress2());
            jsonObject.put("state", arr[2]);
            jsonObject.put("city", arr[1]);
            jsonObject.put("country", arr[3]);
            jsonObject.put("zipcode", "00185");
            jsonObject.put("phoneNumber", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "createJsonDataOrder: " + jsonObject.toString());
        return jsonObject.toString();
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int brandId = listProducts.get(position).getBrandId();
                String shipmentInformation = null;
                for (CourierObj courierObj : listCourier) {
                    if (courierObj.getBrandId() == brandId) {
                        shipmentInformation = courierObj.getDeliveryTime();
                        if (courierObj.getImage() != null)
                            Glide.with(self).load(courierObj.getImage()).into(ivShipmentType);
                        break;
                    }
                }
                tvShipmentInformation.setText(shipmentInformation);
                Log.e(TAG, "onPageScrolled: " + shipmentInformation);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getCartItems(final int cartId) {
        showDialog();
        ConnectServer.getResponseAPI().getCartItem(cartId).enqueue(new Callback<CartItemRespond>() {
            @Override
            public void onResponse(Call<CartItemRespond> call, Response<CartItemRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listProducts.clear();
                        listProducts.addAll(response.body().getData());
                        productViewPagerAdapter.notifyDataSetChanged();

                        for (CartItemObj cartItemObj : response.body().getData()) {
                            subTotal += cartItemObj.getPrice();
                            total += cartItemObj.getTotalPrice();
                        }

                        tvSubTotal.setText(getResources().getString(R.string.lbl_currency) + subTotal);
                        tvTotalPrice.setText(getResources().getString(R.string.lbl_currency) + total);
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<CartItemRespond> call, Throwable t) {
                closeDialog();
                showToast(t.getMessage());
            }
        });
    }

    private void saveCartToOrder() {
        showDialog();
        ConnectServer.getResponseAPI().saveCartToOrder(GlobalValue.cartId, userObj.getId()).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        sendOrder(createJsonDataOrder(), dataShipping, createJonDataPayment());
                    }
                } else {
                    closeDialog();
                }
            }

            @Override
            public void onFailure(Call<BaseMessageRespond> call, Throwable t) {
                closeDialog();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void sendOrder(String dataOrder, String dataShipping, String dataPayment) {
        showDialog();
        ConnectServer.getResponseAPI().sendOrder(dataOrder, dataShipping, dataPayment).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        showToast(response.body().getMessage());
                        gotoActivity(self, OrderConfirmationActivity.class);
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

    //Stripe Payment
    public void submitCard() {
        // TODO: replace with your own test key
        showDialog();
        final String publishableApiKey = this.getString(R.string.stripe_publishable_key);
//        Card card = new Card(cardNumber, Integer.valueOf(month), Integer.valueOf(year), cvv);
        Card card = new Card("4242424242424242", 12, 20, "123");
        if (card.validateCard() && card.validateCVC() && card.validateExpMonth() && card.validateExpYear()) {
            Stripe stripe = new Stripe(this);
            stripe.createToken(card, publishableApiKey, new TokenCallback() {
                public void onSuccess(final Token token) {
                    // TODO: Send Token information to your backend to initiate a charge
                    Log.e(TAG, "token:" + token.getId());
                    closeDialog();
                    sendRequest(token.getId(), total);
                }

                public void onError(Exception error) {
                    Log.e(TAG, error.getLocalizedMessage());
                    closeDialog();
                }
            });
        } else {
            Toast.makeText(self, "Information invalid", Toast.LENGTH_LONG).show();
        }


    }

    public void sendRequest(final String token, final float amount) {
        showDialog();
        ConnectServer.getResponseAPI().sendStripeRequest(token, amount).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        sendOrder(createJsonDataOrder(), dataShipping, createJonDataPayment());
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

    /*PayPal Payment*/
    public void paymentPayPal() {
        if (total < 0) {
            showToastMessage("Point is invalid");
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            Log.d(TAG, "payOut: " + decimalFormat.format(total));
            requestPayPalPayment(total,
                    "GET POINT", "USD");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("eee", "onActivityResult");
        if (requestCode == Constants.REQUEST_CODE_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i("paymentExample",
                                "Payment OK. Response :" + confirm.toJSONObject());
                        JSONObject json = confirm.toJSONObject();
                        getTransactionFromPayPal(json);

                        sendOrder(createJsonDataOrder(), dataShipping, createJonDataPayment());

                    } catch (Exception e) {
                        Log.e("paymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample",
                        "An invalid payment was submitted. Please see the docs.");
            }
        }
    }

    public static String getTransactionFromPayPal(JSONObject json) {
        try {
            JSONObject proof_of_payment = json
                    .getJSONObject(PAYPAY_RESONDE);

            return proof_of_payment.getString("state")
                    + proof_of_payment.getString("id");
        } catch (Exception e) {

        }
        return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_confirm:
                Log.e(TAG, "dataShipping: " + dataShipping);
                if (paymentMethod == Constants.PAYPAL) {
                    paymentPayPal();
                } else {
                    submitCard();
//                    sendOrder(createJsonDataOrder(), dataShipping, createJonDataPayment());
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));
    }
}
