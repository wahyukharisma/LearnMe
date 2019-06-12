package com.example.learnme.API;

import com.example.learnme.Model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
    // Forgot Password
    @FormUrlEncoded
    @POST("forgotPassword.php")
    Call<Response> forgotPassword(@Field("username") String username,
                             @Field("email") String email);

    //Question
    @GET("getTrendingsQuestion.php") Call<ResponseTrendsQuestion> getTendingQuestion();
    @FormUrlEncoded
    @POST("getQuestionTag.php") Call<ResponseTrendsQuestion> getQuestionTag(@Field("tag") String tag);
    @FormUrlEncoded
    @POST("getQuestionKeyword.php") Call<ResponseTrendsQuestion> getQuestionKeyword(@Field("keyword") String keyword);
    @FormUrlEncoded
    @POST("getQuestionById.php") Call<ResponseTrendsQuestion> getQuestionById(@Field("id") String id);
    @FormUrlEncoded
    @POST("updateQuestionLike.php") Call<ResponseTrendsQuestion> updateQuestionLike(@Field("id") String id,
                                                                                    @Field("request") String request);
    @FormUrlEncoded
    @POST("updateQuestionDislike.php") Call<ResponseTrendsQuestion> updateQuestionDislike(@Field("id") String id,
                                                                                    @Field("request") String request);
    @FormUrlEncoded
    @POST("getQuestionByKeywordTag.php") Call<ResponseTrendsQuestion> getQuestionKeywordTag(@Field("tag") String tag,
                                                                                          @Field("keyword") String keyword);



    //Answer
    @FormUrlEncoded
    @POST("getAnswerById.php") Call<ResponseAnswer> getAnswerById(@Field("id") String id);
    @FormUrlEncoded
    @POST("storeAnswer.php") Call<ResponseAnswer> storeAnswer(@Field("id_question") String id_question,
                                                              @Field("id") String id,
                                                              @Field("answer") String answer);
    @FormUrlEncoded
    @POST("updateAnswerLike.php") Call<ResponseAnswer> updateAnswerLike(@Field("id") String id,
                                                                        @Field("request") String request);
    @FormUrlEncoded
    @POST("updateAnswerDislike.php") Call<ResponseAnswer> updateAnswerDislike(@Field("id") String id,
                                                                              @Field("keyword") String keyword);
}
