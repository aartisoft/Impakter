package com.impakter.impakter.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.SendMessageRespond;
import com.impakter.impakter.utils.RealPathUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMessageImageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivClose;
    private ImageView ivBackground;
    private EditText edtDescription;
    private ImageView ivSend;
    private String imageUrl;
    private int userId;
    private int receiverId, conversationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message_image);
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        ivClose = findViewById(R.id.iv_close);
        ivBackground = findViewById(R.id.iv_background);
        ivSend = findViewById(R.id.iv_send);

        edtDescription = findViewById(R.id.edt_description);
    }

    private void initData() {
        if (isLoggedIn())
            userId = preferencesManager.getUserLogin().getId();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                imageUrl = bundle.getString(Constants.IMAGE_URL);
                receiverId = bundle.getInt(Constants.RECEIVER_ID);
                conversationId = bundle.getInt(Constants.CONVERSATION_ID);
                Glide.with(self).load(new File(imageUrl)).into(ivBackground);
            }
        }
    }

    private void initControl() {
        ivClose.setOnClickListener(this);
        ivSend.setOnClickListener(this);
    }

    private void sendMessageToServer(int senderId, int receiverId, int roomId, String content, String fileUrl) {
        RequestBody message = createPartFromString(content);

        MultipartBody.Part file = null;
        if (fileUrl != null && fileUrl.length() != 0) {
            file = prepareFilePart("file", Uri.fromFile(new File(fileUrl)));
        }
        showDialog();
        ConnectServer.getResponseAPI().sendMessage(senderId, receiverId, roomId, message, file).enqueue(new Callback<SendMessageRespond>() {
            @Override
            public void onResponse(Call<SendMessageRespond> call, Response<SendMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        Intent intent = new Intent();
                        intent.putExtra(Constants.MESSAGE, response.body().getData());
                        intent.putExtra(Constants.FILE_ATTACH, response.body().getData().getFileAttach());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<SendMessageRespond> call, Throwable t) {
                showToast(t.getMessage());
                closeDialog();
            }
        });
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = new File(RealPathUtil.getRealPath(self, fileUri));
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                onBackPressed();
                break;
            case R.id.iv_send:
                String message = edtDescription.getText().toString().trim();
                sendMessageToServer(userId, receiverId, conversationId, message, imageUrl);
                break;
        }
    }
}
