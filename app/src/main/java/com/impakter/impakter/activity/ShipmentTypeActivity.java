package com.impakter.impakter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.CourierAdapter;
import com.impakter.impakter.adapter.ShipmentAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.config.GlobalValue;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CourierObj;
import com.impakter.impakter.object.CourierRespond;
import com.impakter.impakter.object.ShipmentObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmentTypeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private Button btnSave;
    private RecyclerView rcvCourier;
    private List<ShipmentObj> listShipmentService = new ArrayList<>();
    private ShipmentAdapter shipmentAdapter;

    private TextView tvNoData;
    private ProgressBar progressBar;
    private String zipCode = "00185";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_type);
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        btnSave = findViewById(R.id.btn_save);
        tvNoData = findViewById(R.id.tv_no_data);
        progressBar = findViewById(R.id.progress_bar);

        rcvCourier = findViewById(R.id.rcv_courier);
        rcvCourier.setHasFixedSize(true);
        rcvCourier.setLayoutManager(new LinearLayoutManager(self));
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            zipCode = intent.getStringExtra(Constants.ZIP_CODE);
        }

        shipmentAdapter = new ShipmentAdapter(self, listShipmentService);
        rcvCourier.setAdapter(shipmentAdapter);
        getCouriers();
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private boolean checkIsValid() {
        for (ShipmentObj shipmentObj : listShipmentService) {
            if (shipmentObj.getListCourier().size() == 0) {
                return false;
            }
        }
        return true;
    }

    private void getCouriers() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getCouriers(GlobalValue.cartId, zipCode).enqueue(new Callback<CourierRespond>() {
            @Override
            public void onResponse(Call<CourierRespond> call, Response<CourierRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listShipmentService.clear();
                        for (CourierRespond.Data data : response.body().getData()) {
                            String brandName = data.getBrandName();
                            int regionShippingId = data.getRegion_shipping_id();
                            int brandId = data.getBrandId();
                            List<CourierObj> listCouriers = new ArrayList<>();
                            CourierRespond.Standard standard = data.getStandard();
                            CourierRespond.Expedited expedited = data.getExpedited();
                            if (standard != null || expedited != null) {

                                if (standard != null) {
                                    CourierObj courierObj = new CourierObj();
                                    courierObj.setId(standard.getId());
                                    courierObj.setType("Stand");
                                    courierObj.setTypeName(standard.getName());
                                    courierObj.setImage(standard.getImage());
                                    courierObj.setPickupAvailable(standard.getPickupAvailable());
                                    courierObj.setDeliveryTime(standard.getDeliveryTime());
                                    courierObj.setTaxDuties(standard.getTaxAndDutiesChange().getPrice());
                                    courierObj.setShipmentCharge(standard.getShipmentCharge().getPrice());
                                    courierObj.setTotal(standard.getTotal().getPrice());
                                    courierObj.setRegionShippingId(regionShippingId);
                                    courierObj.setBrandId(brandId);

                                    listCouriers.add(courierObj);
                                }
                                if (expedited != null) {
                                    CourierObj courierObj = new CourierObj();
                                    courierObj.setId(expedited.getId());
                                    courierObj.setType("Expedited");
                                    courierObj.setTypeName(expedited.getName());
                                    courierObj.setImage(expedited.getImage());
                                    courierObj.setPickupAvailable(expedited.getPickupAvailable());
                                    courierObj.setDeliveryTime(expedited.getDeliveryTime());
                                    courierObj.setTaxDuties(expedited.getTaxAndDutiesChange().getPrice());
                                    courierObj.setShipmentCharge(expedited.getShipmentCharge().getPrice());
                                    courierObj.setTotal(expedited.getTotal().getPrice());
                                    courierObj.setRegionShippingId(regionShippingId);
                                    courierObj.setBrandId(brandId);

                                    listCouriers.add(courierObj);
                                }
                            }
                            ShipmentObj shipmentObj = new ShipmentObj();
                            shipmentObj.setBrandId(brandId);
                            shipmentObj.setBrandName(brandName);
                            shipmentObj.setListCourier(listCouriers);

                            listShipmentService.add(shipmentObj);
                        }

                        shipmentAdapter.notifyDataSetChanged();
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CourierRespond> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showToast(t.getMessage());
            }
        });
    }

    List<CourierObj> listSelectedCourier = new ArrayList<>();

    private String getJsonShipment() {
        JSONObject jsonObject = new JSONObject();

        for (ShipmentObj shipmentObj : listShipmentService) {
            int brandId = shipmentObj.getBrandId();
            List<CourierObj> listCouriers = shipmentObj.getListCourier();
            for (CourierObj courierObj : listCouriers) {
                if (courierObj.isCheck()) {
                    listSelectedCourier.add(courierObj);
                    try {
                        JSONObject object = new JSONObject();
                        object.put("type", courierObj.getType() + "");
                        object.put("courierShippingId", courierObj.getId() + "");
                        object.put("regionShippingId", courierObj.getRegionShippingId() + "");
                        object.put("shipmentPrice", courierObj.getShipmentCharge() + "");
                        object.put("tax", courierObj.getTaxDuties() + "");

                        jsonObject.put(brandId + "", object);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "err: " + e.getMessage());
                    }
                }
            }
        }
        Log.e(TAG, "getJsonShipment: " + jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_save:
//                getJsonShipment();
                if (checkIsValid()) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constants.ARR_COURIER, (ArrayList<? extends Parcelable>) listSelectedCourier);
                    bundle.putString(Constants.DATA_SHIPPING, getJsonShipment());
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);

                    finish();
                } else {
                    showToast(getResources().getString(R.string.some_product_do_not_have_shipment_service));
                }
                break;
        }
    }
}
