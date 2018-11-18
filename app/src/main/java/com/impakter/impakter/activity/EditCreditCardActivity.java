package com.impakter.impakter.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.CreditCardAdapter;

public class EditCreditCardActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private RecyclerView rcvCreditCard;
    private CreditCardAdapter creditCardAdapter;
    private TextView tvSave, btnAddNewPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_credit_card);
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        tvSave = findViewById(R.id.tv_save);
        btnAddNewPayment = findViewById(R.id.btn_add_new_payment);

        rcvCreditCard = findViewById(R.id.rcv_credit_card);
        rcvCreditCard.setHasFixedSize(true);
        rcvCreditCard.setLayoutManager(new LinearLayoutManager(self));
    }

    private void initData() {
        creditCardAdapter = new CreditCardAdapter(self, true);
        rcvCreditCard.setAdapter(creditCardAdapter);
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        btnAddNewPayment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_save:

                break;

            case R.id.btn_add_new_payment:
                gotoActivity(self, AddNewPaymentActivity.class);
                break;
        }
    }
}
