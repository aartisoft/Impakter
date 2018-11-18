package com.impakter.impakter.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.MessageDetailAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.MessageObj;
import com.impakter.impakter.object.MessageRespond;
import com.impakter.impakter.object.SendMessageRespond;
import com.impakter.impakter.utils.RealPathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_GALLERY = 1234;
    public static final int REQUEST_IMAGE_CAPTURE = 9999;

    private ImageView ivBack;
    private RecyclerView rcvMessage;
    private List<MessageObj> listMessages = new ArrayList<>();
    private MessageDetailAdapter messageAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoData, tvTitle;
    private ProgressBar progressBar;

    private ImageView ivCamera, ivSend;
    private EditText edtContent;

    private int userId;
    private int conversationId;
    private int page = 1;
    private int totalPage;
    private String urlImage;
    private int receiverId, position;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        ivCamera = findViewById(R.id.iv_camera);
        ivSend = findViewById(R.id.iv_send);
        edtContent = findViewById(R.id.edt_content);

        tvNoData = findViewById(R.id.tv_no_data);
        tvTitle = findViewById(R.id.tv_title);
        progressBar = findViewById(R.id.progress_bar);
        swipeRefreshLayout = findViewById(R.id.swf_layout);

        rcvMessage = findViewById(R.id.rcv_message);
        rcvMessage.setHasFixedSize(true);
        rcvMessage.setLayoutManager(new LinearLayoutManager(self));
    }

    private void initData() {
        if (isLoggedIn())
            userId = preferencesManager.getUserLogin().getId();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                conversationId = bundle.getInt(Constants.CONVERSATION_ID);
                receiverId = bundle.getInt(Constants.RECEIVER_ID);
                position = intent.getIntExtra(Constants.CURRENT_POSITION, -1);
                userName = intent.getStringExtra(Constants.USER_NAME);
            }
        }
        tvTitle.setText(userName);

        messageAdapter = new MessageDetailAdapter(self, listMessages);
        rcvMessage.setAdapter(messageAdapter);

        getListMessage();
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivSend.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page++;
                if (page <= totalPage) {
                    getMoreMessage();
                } else {
                    showToast("Have no message anymore");
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

    private void getListMessage() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getListMessages(userId, conversationId, 1).enqueue(new Callback<MessageRespond>() {
            @Override
            public void onResponse(Call<MessageRespond> call, Response<MessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        totalPage = response.body().getAllPages();

                        listMessages.clear();
                        listMessages.addAll(response.body().getData());
                        Collections.reverse(listMessages);
                        messageAdapter.notifyDataSetChanged();
                        rcvMessage.scrollToPosition(listMessages.size() - 1);
                    }
                }
                progressBar.setVisibility(View.GONE);
                checkNoData();
            }

            @Override
            public void onFailure(Call<MessageRespond> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showToast(t.getMessage());
                checkNoData();
            }
        });
    }

    private void getMoreMessage() {
        swipeRefreshLayout.setRefreshing(true);
        ConnectServer.getResponseAPI().getListMessages(userId, conversationId, page).enqueue(new Callback<MessageRespond>() {
            @Override
            public void onResponse(Call<MessageRespond> call, Response<MessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        int currentPosition = listMessages.size();
                        listMessages.addAll(0, response.body().getData());
                        Collections.sort(listMessages, new Comparator<MessageObj>() {
                            @Override
                            public int compare(MessageObj o1, MessageObj o2) {
                                return (int) (o1.getTime() - o2.getTime());
                            }
                        });
                        rcvMessage.scrollToPosition(currentPosition);
                        messageAdapter.notifyDataSetChanged();
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
                checkNoData();
            }

            @Override
            public void onFailure(Call<MessageRespond> call, Throwable t) {
                showToast(t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                checkNoData();
            }
        });
    }

    private void sendMessageToServer(int senderId, final int receiverId, int roomId, String content, String fileUrl) {
        final RequestBody message = createPartFromString(content);

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
                        listMessages.add(response.body().getData());
                        messageAdapter.notifyItemInserted(listMessages.size() - 1);
                        rcvMessage.smoothScrollToPosition(listMessages.size() - 1);

                        Intent intent = new Intent();
                        intent.putExtra(Constants.CURRENT_POSITION, position);
                        intent.putExtra(Constants.MESSAGE, response.body().getData().getContent());

                        intent.setAction(Constants.REFRESH);
                    }
                }
                edtContent.setText("");
                closeDialog();
                checkNoData();
            }

            @Override
            public void onFailure(Call<SendMessageRespond> call, Throwable t) {
                showToast(t.getMessage());
                closeDialog();
                checkNoData();
            }
        });
    }

    @NonNull
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

    private void checkNoData() {
        if (listMessages.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }
    }

    public void chooseImage() {
        new AlertDialog.Builder(self)
                .setTitle(R.string.app_name)
                .setMessage(R.string.select_photo_from)
                .setPositiveButton(R.string.gallery,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // one can be replaced with any action code
                                Intent pickPhoto = new Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                self.startActivityForResult(pickPhoto,
                                        REQUEST_IMAGE_GALLERY);
                            }
                        })
                .setNegativeButton(R.string.camera,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // zero can be replaced with any action code
                                Intent takePictureIntent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent
                                        .resolveActivity(self.getPackageManager()) != null) {
                                    self.startActivityForResult(takePictureIntent,
                                            REQUEST_IMAGE_CAPTURE);
                                }
                            }
                        }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            urlImage = RealPathUtil.getRealPath(self, data.getData());
            Bundle bundle = new Bundle();
            bundle.putString(Constants.IMAGE_URL, urlImage);
            bundle.putInt(Constants.CONVERSATION_ID, conversationId);
            bundle.putInt(Constants.RECEIVER_ID, receiverId);
            bundle.putInt(Constants.CURRENT_POSITION, position);
            Intent intent = new Intent(self, SendMessageImageActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, Constants.REQUEST_SEND_IMAGE);
//            gotoActivity(self, SendMessageImageActivity.class, bundle);
        }
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            urlImage = RealPathUtil.getRealPath(self, data.getData());
            Bundle bundle = new Bundle();
            bundle.putString(Constants.IMAGE_URL, urlImage);
            bundle.putInt(Constants.CONVERSATION_ID, conversationId);
            bundle.putInt(Constants.RECEIVER_ID, receiverId);
            bundle.putInt(Constants.CURRENT_POSITION, position);

            Intent intent = new Intent(self, SendMessageImageActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, Constants.REQUEST_SEND_IMAGE);
            checkNoData();
//            gotoActivity(self, SendMessageImageActivity.class, bundle);
        }
        if (requestCode == Constants.REQUEST_SEND_IMAGE && resultCode == RESULT_OK) {
            MessageObj messageObj = data.getParcelableExtra(Constants.MESSAGE);
            MessageObj.FileAttach fileAttach = data.getParcelableExtra(Constants.FILE_ATTACH);
            messageObj.setFileAttach(fileAttach);
            listMessages.add(messageObj);
            messageAdapter.notifyItemInserted(listMessages.size() - 1);
            rcvMessage.scrollToPosition(listMessages.size() - 1);
            checkNoData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_camera:
                chooseImage();
                break;
            case R.id.iv_send:
                String message = edtContent.getText().toString().trim();
                sendMessageToServer(userId, receiverId, conversationId, message, "");
                break;
        }
    }
}
