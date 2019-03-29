package com.example.annoncepei.Networking;

import com.example.annoncepei.Models.Annonce;
import com.example.annoncepei.Models.Favoris;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;


public interface ApiConfig {

    @Multipart
    @POST("upload")
    Call<Annonce> addAnnonce(
            @Header("Authorization") String authorization,
            @PartMap Map<String, RequestBody> map,
            @Part("annonce") Annonce annonce
    );

    @POST("Favoris")
    Call<Favoris> addFavoris (
        @Header("Authorization") String authorization,
        @Body Favoris favoris
        );
}
