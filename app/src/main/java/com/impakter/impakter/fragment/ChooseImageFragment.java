package com.impakter.impakter.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.ChangeAvatarActivity;
import com.impakter.impakter.adapter.PhotoFolderAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.object.FolderImageObj;

import java.util.ArrayList;

public class ChooseImageFragment extends BaseFragment {
    private View rootView;
    private RecyclerView rcvFolder;
    private PhotoFolderAdapter photoFolderAdapter;
    public ArrayList<FolderImageObj> listPhotoFolder = new ArrayList<>();
    boolean boolean_folder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_choose_image, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        rcvFolder = rootView.findViewById(R.id.rcv_folder);
        rcvFolder.setHasFixedSize(true);
        rcvFolder.setLayoutManager(new LinearLayoutManager(self));
    }

    private void initData() {
        getListPhotoFolder();
    }

    private void initControl() {
        photoFolderAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ChangeAvatarFragment changeAvatarFragment = new ChangeAvatarFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FOLDER_NAME, listPhotoFolder.get(position).getFolder());
                bundle.putStringArrayList(Constants.ARR_IMAGE, listPhotoFolder.get(position).getListImages());
                changeAvatarFragment.setArguments(bundle);
                ((ChangeAvatarActivity) self).showFragment(changeAvatarFragment);
            }
        });
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
        photoFolderAdapter = new PhotoFolderAdapter(self, listPhotoFolder);
        rcvFolder.setAdapter(photoFolderAdapter);
        return listPhotoFolder;
    }
}
