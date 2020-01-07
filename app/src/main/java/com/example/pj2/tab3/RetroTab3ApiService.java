package com.example.pj2.tab3;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetroTab3ApiService {

    @POST("toilet/emergency")
    Call<Problem> emergency_Problem(@Body Problem emergency_problem);
}
