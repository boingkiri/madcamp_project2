package com.example.pj2.tab1;

import com.example.pj2.tab1.Contacts;
import com.example.pj2.tab3.Problem;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetroBaseApiService {

    // 통신담당 메소드 구현


    // mobile -> server 전송
    // 하나씩 볼때는 쿼리로 날리기
    // 여러개는 Map으로 하면 된다고 함
    @GET("get-Contacts")
    Call<List<Contacts>> get_Contacts(@Query("name") String name, @Query("phone") String phone);
//    Call<List<Contacts>> get_Contacts(@Query("name") String name, @Query("phone") String phone, @Query("fb_id") String fb_id); // 따옴표 안에는 보내는 키, 선언한 것은 보내는 값
//    Call<List<Contacts>> get_Contacts(@QueryMap Map<String, String> contacts);

    @POST("Contacts")
    Call<List<Contacts>> post_Contacts(@Body List<Contacts> post_contacts);
//    Call<List<Contacts>> post_Contacts(@Query("name") String name, @Query("phone") String phone, @Query("fb_id") String fb_id);

    @GET("get-Contacts")
    Call<List<Contacts>> get_start_Contacts(@Query("name") String name, @Query("phone") String phone, @Query("fb_id") String fb_id);


}
