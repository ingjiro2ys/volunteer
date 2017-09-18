package com.example.user.volunteer.manager.http;

import com.android.volley.Response;
import com.example.user.volunteer.dao.PhotoItemCollectionDao;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by User on 3/5/2560.
 */

public interface ApiService {

    @POST("show_eventname_image.php")
    Call<PhotoItemCollectionDao> loadPhotoList();

    @POST("show_eventname_userRegis.php")
    Call<PhotoItemCollectionDao> loadListRegis();

    @GET("show_fav.php")
    //Response getMyThing(@Query("userID") String userID);
    Call<PhotoItemCollectionDao> loadFav();

}
