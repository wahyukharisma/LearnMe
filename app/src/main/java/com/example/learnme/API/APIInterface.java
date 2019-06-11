package com.example.learnme.API;

import com.example.learnme.Model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {
    @FormUrlEncoded
    @POST("login.php")
    Call<Response> loginUser(@Field("username") String username,
                             @Field("password") String password);

    // User API
    @FormUrlEncoded
    @POST("registerUser.php")
    Call<Response> registerUser(@Field("username") String username,
                                    @Field("password") String password,
                                    @Field("email") String email);

    // Person API
    @FormUrlEncoded
    @POST("registerPerson.php")
    Call<Response> registerPerson(@Field("username") String username,
                                      @Field("first_name") String first_name,
                                      @Field("last_name") String last_name,
                                      @Field("address") String address,
                                      @Field("phone") String phone);
}
