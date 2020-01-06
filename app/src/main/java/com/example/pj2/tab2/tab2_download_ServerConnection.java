package com.example.pj2.tab2;

import android.content.Context;
import android.util.Log;

import com.facebook.AccessToken;

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

        @POST("/download/elem")
        Call downloadImages(@Part("user_id") RequestBody user_id, @Part("filename") RequestBody filename);
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
//                imageAdapter.updateData(filelist);
//                for (int i = 0; i < filelist.size(); i++){
//                    getPhotoFromServer(filelist.get(i).getName());
//                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getPhotoFromServer(String filename) {
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

        call.enqueue(new Callback<List<File_list>>() {
            @Override
            public void onResponse(Call<List<File_list>> call, Response<List<File_list>> response) {
                List<File_list> filelist = response.body();


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
