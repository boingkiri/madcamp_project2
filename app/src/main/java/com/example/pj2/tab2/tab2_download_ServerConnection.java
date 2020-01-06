package com.example.pj2.tab2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.AccessToken;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class tab2_download_ServerConnection {

    Context mContext;
    ServerImageAdapter imageAdapter;

    public tab2_download_ServerConnection(Context ctx, ServerImageAdapter adapter){
        mContext = ctx;
        imageAdapter = adapter;
    }

    public interface DownloadAPIs {
        @Multipart
        @POST("/download/entire")
        Call<List<File_list>> getPhotoList(@Part("user_id") RequestBody user_id);

        @POST("/download/view")
        Call downloadImages(@Part("user_id") RequestBody user_id, @Query("filename") RequestBody filename);
    }

    public void downloadToServer() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(mContext);
        DownloadAPIs uploadAPIs = retrofit.create(DownloadAPIs.class);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        //Create request body with Facebook id for auth.
        RequestBody usr_id = RequestBody.create(MediaType.parse("text/plain"), accessToken.getUserId());
//        Log.d("userid",accessToken.getUserId());

        Call call = uploadAPIs.getPhotoList(usr_id);

        call.enqueue(new Callback<List<File_list>>() {
            @Override
            public void onResponse(Call<List<File_list>> call, Response<List<File_list>> response) {
                Log.d("File_download",response.toString());

                List<File_list> filelist = response.body();
                ArrayList<String> converted_filelist = new ArrayList<>();

                for (int i = 0; i < filelist.size(); i++){
//                    getPhotoFromServer(filelist.get(i).getName());
                    converted_filelist.add(filelist.get(i).getName());
                }
                imageAdapter.updateData(converted_filelist);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getPhotoFromServer(String filename, final ImageView img) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(mContext);
        DownloadAPIs uploadAPIs = retrofit.create(DownloadAPIs.class);

        // create list of file parts (photo, video, ...)

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        //Create request body with Facebook id for auth.
        RequestBody usr_id = RequestBody.create(MediaType.parse("text/plain"), accessToken.getUserId());
        Log.d("userid",accessToken.getUserId());
        RequestBody filenameRequest = RequestBody.create(MediaType.parse("text/plain"), filename);
//        Call call = uploadAPIs.uploadImage(part, description, usr_id);

        Call call = uploadAPIs.downloadImages(usr_id, filenameRequest);

//        call.enqueue(new Callback<List<File_list>>() {
        call.enqueue(new Callback<InputStream>() {
            @Override
            public void onResponse(Call<InputStream> call, Response<InputStream> response) {
                InputStream filedata = response.body();
                Bitmap bm = BitmapFactory.decodeStream(filedata);
//                img.setImageBitmap(bm);

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
