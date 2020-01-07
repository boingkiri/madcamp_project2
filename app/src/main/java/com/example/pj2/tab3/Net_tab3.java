package com.example.pj2.tab3;

import com.example.pj2.tab1.Net;
import com.example.pj2.tab1.RetroBaseApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Net_tab3 {
    private static final Net_tab3 ourInstance = new Net_tab3();

    static Net_tab3 getInstance() {
        return ourInstance;
    }

    private Net_tab3() {
    }

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.249.19.254:7280")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    RetroTab3ApiService retroBaseApiService;

    public RetroTab3ApiService getRetro(){ // 인터페이스 객체를 받는 메소드
        if(retroBaseApiService == null){
            retroBaseApiService = retrofit.create(RetroTab3ApiService.class); // create의 인자는 인터페이스의 이름 넣으면 됨
        }

        return retroBaseApiService;
    }
}
