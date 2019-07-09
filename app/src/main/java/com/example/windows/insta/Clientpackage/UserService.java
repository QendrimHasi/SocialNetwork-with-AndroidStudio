package com.example.windows.insta.Clientpackage;



import com.example.windows.insta.model.File_info;
import com.example.windows.insta.model.Post;
import com.example.windows.insta.model.PostOfFollowers;
import com.example.windows.insta.model.ResObj;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface UserService {

    @POST("login")
    Call<ResponseBody> login(@Body ResObj resObj);

    @GET("profile")
    Call<Post> getprofile(@Header("Authorization") String authheader);

    @Multipart
    @POST("post/image")
    Call<ResponseBody> uplode(@Header("Authorization") String authheader, @Part MultipartBody.Part file);

    @POST("post/")
    Call<ResponseBody> inserPost(@Header("Authorization") String authheader, @Body File_info file_info);

    @GET("home/{offset}/{limit}")
    Call<List<PostOfFollowers>> home(@Header("Authorization") String authheader, @Path("offset") int offset, @Path("limit") int limit);


}
