package com.mg.syrianpound.api.classes;

import com.mg.syrianpound.models.Coin;
import com.mg.syrianpound.models.gold;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {


    String BASE_URL = "https://api.spstocks.com/";
    @GET("getCoins")
    Call<ApiResponse<List<Coin>>>  getCoins(@Query("key") String key);

    @GET("getGolds")
    Call<ApiResponse<List<gold>>> getGold(@Query("key") String key);
}
