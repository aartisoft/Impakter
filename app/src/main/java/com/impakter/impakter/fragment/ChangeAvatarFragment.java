package com.impakter.impakter.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.ChangeAvatarActivity;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.adapter.ImageAdapter;
import com.impakter.impakter.adapter.PhotoFolderAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.object.FolderImageObj;
import com.impakter.impakter.utils.RealPathUtil;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class ChangeAvatarFragment extends BaseFragment implements ChangeAvatarActivity.OnRequestCaptureImageListener {
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1001;
    private View rootView;
    private RecyclerView rcvImage;
    private ArrayList<String> listImages = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private CropImageView cropImageView;
    private TextView tvSave, tvFolderName;
    private String folderName;
    private RelativeLayout toolbar;
    private ImageView ivDropDown;
    public static final int REQUEST_IMAGE_CAPTURE = 9999;

    public ArrayList<FolderImageObj> listPhotoFolder = new ArrayList<>();
    boolean boolean_folder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_change_avatar, container, false);
        initViews();
        initData();
        initControl();
        ((ChangeAvatarActivity) self).setOnRequestCaptureImageListener(this);
        return rootView;
    }

    private void initViews() {
        toolbar = ((ChangeAvatarActivity) self).getLayoutToolbar();
        tvSave = toolbar.findViewById(R.id.tv_save);
        tvFolderName = toolbar.findViewById(R.id.tv_folder);
        ivDropDown = toolbar.findViewById(R.id.iv_dropdown);
        ivDropDown.setImageResource(R.drawable.ic_arrow_drop_down);

        rcvImage = rootView.findViewById(R.id.rcv_image);
        rcvImage.setHasFixedSize(true);
        rcvImage.setLayoutManager(new GridLayoutManager(self, 4));

        cropImageView = rootView.findViewById(R.id.cropImageView);
        cropImageView.setCropShape(CropImageView.CropShape.OVAL);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setAutoZoomEnabled(true);
        cropImageView.setMultiTouchEnabled(false);
        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                Intent intent = new Intent();
                intent.putExtra(Constants.AVATAR, RealPathUtil.getRealPath(self, result.getOriginalUri()));
                intent.putExtra(Constants.AVATAR_URI, result.getOriginalUri());
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });


    }

    private void initData() {
        getListPhotoFolder();
        Bundle bundle = getArguments();
        listImages.clear();
        if (bundle != null) {
            listImages.add(null);
            listImages.addAll(bundle.getStringArrayList(Constants.ARR_IMAGE));
//            listImages = bundle.getStringArrayList(Constants.ARR_IMAGE);
            folderName = bundle.getString(Constants.FOLDER_NAME);

            tvFolderName.setText(folderName);
        } else {
            listImages.add(null);
            if (listPhotoFolder.size() != 0) {
                listImages.addAll(listPhotoFolder.get(0).getListImages());
                tvFolderName.setText(listPhotoFolder.get(0).getFolder());
            }
        }
        imageAdapter = new ImageAdapter(self, listImages);
        rcvImage.setAdapter(imageAdapter);

        if (listImages.size() > 1)
            cropImageView.setImageUriAsync(Uri.fromFile(new File(listImages.get(1))));
    }

    private void initControl() {
        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    takePhoto();
                } else {
                    cropImageView.setImageUriAsync(Uri.fromFile(new File(listImages.get(position))));
                }

            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.getCroppedImageAsync();
            }
        });
    }

    public static ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent
                .resolveActivity(self.getPackageManager()) != null) {
            self.startActivityForResult(takePictureIntent,
                    REQUEST_IMAGE_CAPTURE);
        }
    }

    public boolean checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(self,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(self,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(self,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL_STORAGE);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(self,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL_STORAGE);
            }
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<FolderImageObj> getListPhotoFolder() {
        listPhotoFolder.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = self.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < listPhotoFolder.size(); i++) {
                if (listPhotoFolder.get(i).getFolder().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }


            if (boolean_folder) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(listPhotoFolder.get(int_position).getListImages());
                al_path.add(absolutePathOfImage);
                listPhotoFolder.get(int_position).setListImages(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                FolderImageObj obj_model = new FolderImageObj();
                obj_model.setFolder(cursor.getString(column_index_folder_name));
                obj_model.setListImages(al_path);

                listPhotoFolder.add(obj_model);


            }


        }


        for (int i = 0; i < listPhotoFolder.size(); i++) {
            Log.e("FOLDER", listPhotoFolder.get(i).getFolder());
            for (int j = 0; j < listPhotoFolder.get(i).getListImages().size(); j++) {
                Log.e("FILE", listPhotoFolder.get(i).getListImages().get(j));
            }
        }
        return listPhotoFolder;
    }

    @Override
    public void onSuccess(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
            cropImageView.setImageUriAsync(data.getData());
//            cropImageView.setImageBitmap(imageBitmap);
        }
    }
}
