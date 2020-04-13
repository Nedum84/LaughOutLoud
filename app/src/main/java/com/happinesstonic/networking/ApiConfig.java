package com.happinesstonic.networking;


import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;


public interface ApiConfig {

    @Multipart
    @POST("add_joke_with_image.php")
    Call<ServerResponse> upload(
            @Header("Authorization") String authorization,
            @PartMap Map<String, RequestBody> map,
            @Part("request_type") String request_type,
            @Part("user_id") String user_id
    );
}