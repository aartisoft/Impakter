package com.impakter.impakter.fragment;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.impakter.impakter.R;
import com.impakter.impakter.adapter.ChooserArrayAdapter;
import com.impakter.impakter.adapter.ContactAdapter;
import com.impakter.impakter.adapter.ListAppAdapter;
import com.impakter.impakter.events.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetShareFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private View rootView;
    private RecyclerView rcvContact, rcvApp;
    private ContactAdapter contactAdapter;
    private List<String> listContacts = new ArrayList<>();
    private List<String> listPackages = new ArrayList<>();
    private ImageView ivClose;
    private EditText edtSearch;
    private BottomSheetBehavior sheetBehavior;
    private ListAppAdapter listAppAdapter;

    public BottomSheetShareFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
                sheetBehavior = BottomSheetBehavior.from(bottomSheet);
//                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_sheet_share, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        ivClose = rootView.findViewById(R.id.iv_close);
        edtSearch = rootView.findViewById(R.id.edt_search);

        rcvContact = rootView.findViewById(R.id.rcv_contact);
        rcvContact.setHasFixedSize(true);
        rcvContact.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        rcvApp = rootView.findViewById(R.id.rcv_app);
        rcvApp.setHasFixedSize(true);
        rcvApp.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

    }

    private void initData() {
        contactAdapter = new ContactAdapter(getActivity(), listContacts);
        rcvContact.setAdapter(contactAdapter);

        getListApp();
    }

    private void initControl() {
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivClose.setOnClickListener(this);
    }

    public void getListApp() {
        final List<String> packages = new ArrayList<String>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        final List<ResolveInfo> resInfosNew = new ArrayList<ResolveInfo>();
        final List<ResolveInfo> resInfos = getActivity().getPackageManager().queryIntentActivities(shareIntent, 0);
        resInfosNew.addAll(resInfos);
        if (!resInfos.isEmpty()) {
            int count = 0;
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
//                if (packageName.contains("com.facebook.katana")) {
//                    resInfosNew.remove(count);
//                } else
                packages.add(packageName);
                count++;
            }
        }
        listAppAdapter = new ListAppAdapter(getActivity(), packages);
        rcvApp.setAdapter(listAppAdapter);

        listAppAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                invokeApplication(packages.get(position), resInfosNew.get(position));
            }
        });

    }

    private void invokeApplication(String packageName, ResolveInfo resolveInfo) {
        // if(packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana") || packageName.contains("com.kakao.story")) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi guys, I found amazing thing to share. Send your love and care in form of gifts to your loved ones from anywhere in world. Log on to giftjaipur.com or download app" + "https://goo.gl/YslIVT" + "and use coupon code app50 to get Rs 50 off on your first purchase.");
        intent.putExtra(Intent.EXTRA_SUBJECT, "GiftJaipur 50 Rs Coupon...");
        intent.setPackage(packageName);

        startActivity(intent);
        // }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                this.dismiss();
                break;
        }
    }
}
