package com.example.pj2.tab2;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.pj2.R;
import com.example.pj2.helper.AppConstant;
import com.example.pj2.helper.Utils;
import com.facebook.AccessToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class tab2_download extends Fragment {
    private ServerImageAdapter serverImageAdapter;
    private GridView gridView;
    String basePath = null;
    final int TAKE_CAMERA = 2;
    final int TAKE_GALLERY = 3;
    ArrayList<String> selected_photo = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab2_select, container, false);
        gridView = v.findViewById(R.id.mygalleryid);


        File directory = new File(android.os.Environment.getExternalStorageDirectory() + File.separator + AppConstant.PHOTO_ALBUM);
//        File a = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        Log.d("aaa",mediaStorageDir.getPath());

        if (!directory.exists()){
            if(!directory.mkdirs()){
                Log.e("tab2","failed to create directory");
            }
        }
        basePath = directory.getPath();

//        imgAdapter = new ImageAdapter(getContext().getApplicationContext());
        serverImageAdapter = new ServerImageAdapter(getActivity());
        gridView.setAdapter(serverImageAdapter);
        final Utils utils = new Utils(getActivity());

        //Set activity to send parameter to event listener
        final Activity activity = getActivity();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String server_filename = (String)serverImageAdapter.getItem(position);
                if(!selected_photo.contains(server_filename)){
                    selected_photo.add(server_filename);
                    ((ImageView)view).setColorFilter(Color.parseColor("#AAAAAA"), PorterDuff.Mode.MULTIPLY);
                }
                else{
                    selected_photo.remove(server_filename);
                    ((ImageView)view).setColorFilter(null);
                }
            }
        });
//
        Button Uploadbtn = v.findViewById(R.id.tab2_select_upload);
        Uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < selected_photo.size(); i++) {
                    FileStreamThread thr = new FileStreamThread(selected_photo.get(i), basePath);
                    thr.start();
                    try{
                        thr.join();
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        serverImageAdapter.notifyDataSetChanged();

        Button cancelbtn = v.findViewById(R.id.tab2_select_cancel);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        return v;
    }

}
