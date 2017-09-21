package com.example.user.volunteer.manager.http;

import com.android.volley.Response;
import com.example.user.volunteer.dao.PhotoItemCollectionDao;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by User on 3/5/2560.
 */

public interface ApiService {

    @POST("show_eventname_image.php")
    Call<PhotoItemCollectionDao> loadPhotoList();

    @GET("show_eventname_userRegis.php")
    Call<PhotoItemCollectionDao> loadListRegis(@QueryMap Map<String, String> map);

    @GET("show_fav.php")
    Call<PhotoItemCollectionDao> loadFav(@QueryMap Map<String, String> map);

    @POST("show_eventname_owner.php")
    Call<PhotoItemCollectionDao> loadOwner(@QueryMap Map<String, String> map);
}
