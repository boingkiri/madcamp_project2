package com.example.pj2.tab1;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Net {
    private static final Net ourInstance = new Net();

    static Net getInstance() {
        return ourInstance;
    }

    private Net() {
    }

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.249.19.254:7080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    RetroBaseApiService retroBaseApiService;

    public RetroBaseApiService getRetro(){ // 인터페이스 객체를 받는 메소드
        if(retroBaseApiService == null){
            retroBaseApiService = retrofit.create(RetroBaseApiService.class); // create의 인자는 인터페이스의 이름 넣으면 됨
        }

        return retroBaseApiService;
    }


}



