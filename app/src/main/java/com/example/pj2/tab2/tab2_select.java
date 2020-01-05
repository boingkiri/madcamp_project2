package com.example.pj2.tab2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
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
import androidx.fragment.app.FragmentTransaction;

import com.example.pj2.R;
import com.example.pj2.fragment_viewpager;
import com.example.pj2.helper.AppConstant;
import com.example.pj2.helper.Utils;
import com.facebook.AccessToken;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class tab2_select extends Fragment {
    private ImageAdapter imgAdapter;
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

//        Log.d("Facebook_User", accessToken.getUserId());

//        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File directory = new File(android.os.Environment.getExternalStorageDirectory() + File.separator + AppConstant.PHOTO_ALBUM);
//        File a = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        Log.d("aaa",mediaStorageDir.getPath());

        if (!directory.exists()){
            if(!directory.mkdirs()){
                Log.e("tab2","failed to create directory");
            }
        }
        basePath = directory.getPath();

        imgAdapter = new ImageAdapter(getContext().getApplicationContext());
        gridView.setAdapter(imgAdapter);
        final Utils utils = new Utils(getActivity());

        //Set activity to send parameter to event listener
        final Activity activity = getActivity();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filepath = (String)imgAdapter.getItem(position);
                if(!selected_photo.contains(filepath)){
                    selected_photo.add(filepath);
                    ((ImageView)view).setColorFilter(Color.parseColor("#AAAAAA"), PorterDuff.Mode.MULTIPLY);
                }
                else{
                    selected_photo.remove(filepath);
                    ((ImageView)view).setColorFilter(null);
                }
            }
        });

        Button Uploadbtn = v.findViewById(R.id.tab2_select_upload);
        Uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToServer((String)imgAdapter.getItem(1));
            }
        });

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

    public interface UploadAPIs {
        @Multipart
        @POST("/upload")
        Call<ResponseBody> uploadImage(@Part MultipartBody.Part file,
                                       @Part("name") RequestBody requestBody,
                                       @Part("user_id")RequestBody user_id);
    }

    private void uploadToServer(String filePath) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        //Check file path
        Log.d("uploadToServer",filePath);
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        Log.d("Facebook_User", accessToken.getUserId());

        //Create request body with Facebook id for auth.
        RequestBody usr_id = RequestBody.create(MediaType.parse("text/plain"), accessToken.getUserId());
        Call call = uploadAPIs.uploadImage(part, description, usr_id);

//        Log.d("multipartbody",part.headers().toString());
//        Log.d("multipartbody",part.body().contentType().toString());
//        Log.d("description",description.toString());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("File upload response",response.toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

//    @NonNull
//    private MultipartBody.Part prepareFilePart(String partName, String filepath) {
//        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
//        // use the FileUtils to get the actual file by uri
//        File file = new File(filepath);
////        File file = FileUtils.getFile(this, fileUri);
//        //compress the image using Compressor lib
//        Timber.d("size of image before compression --> " + file.getTotalSpace());
//        compressedImageFile = new Compressor(this).compressToFile(file);
//        Timber.d("size of image after compression --> " + compressedImageFile.getTotalSpace());
//        // create RequestBody instance from file
//        RequestBody requestFile =
//                RequestBody.create(
//                        MediaType.parse(getContentResolver().getType(fileUri)),
//                        compressedImageFile);
//
//        // MultipartBody.Part is used to send also the actual file name
//        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
//    }
}
