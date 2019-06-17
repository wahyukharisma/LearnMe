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
    @FormUrlEncoded
    @POST("getPointHistoryUser.php")
    Call<ResponsePoint> getPointHistoryUser(@Field("id") String id,
                                @Field("month") String month,
                                @Field("year") String year);

    @FormUrlEncoded
    @POST("updatePasswordBy.php")
    Call<Response> updatePassword(@Field("id") String id,
                                @Field("oldpassword") String oldpassword,
                                @Field("newpassword") String newpassword);

    @FormUrlEncoded
    @POST("getUserBy.php")
    Call<Response> getUserBy(@Field("id") String id);



    // Person API
    @FormUrlEncoded
    @POST("registerPerson.php")
    Call<Response> registerPerson(@Field("username") String username,
                                      @Field("first_name") String first_name,
                                      @Field("last_name") String last_name,
                                      @Field("address") String address,
                                      @Field("phone") String phone);
    @FormUrlEncoded
    @POST("updatePersonBy.php")
    Call<Response> updatePerson(@Field("id") String id,
                                  @Field("first_name") String first_name,
                                  @Field("last_name") String last_name,
                                  @Field("address") String address,
                                  @Field("phone") String phone,
                                  @Field("username") String username,
                                  @Field("email") String email);

    @FormUrlEncoded
    @POST("getPersonBy.php")
    Call<ResponsePerson> getPersonBy(@Field("id") String id);

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
    @FormUrlEncoded
    @POST("storeQuestion.php") Call<Response> storeQuestion(@Field("title") String title,
                                                                          @Field("description") String description,
                                                                          @Field("tag") String tag,
                                                                          @Field("user") String user,
                                                                          @Field("image") String image);
    @FormUrlEncoded
    @POST("getUserQuestion.php")
    Call<ResponseTrendsQuestion> getUserQuestion(@Field("id") String id);

    @FormUrlEncoded
    @POST("getQuestionKeywordUser.php")
    Call<ResponseTrendsQuestion> getQuestionKeywordUser(@Field("id") String id,
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
    @FormUrlEncoded
    @POST("getAnswerKeywordUser.php") Call<ResponseAnswer> getAnswerKeywordUser(@Field("id") String id,
                                                                              @Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("getUserAnswer.php")
    Call<ResponseAnswer> getUserAnswer(@Field("id") String id);


    // Ranking
    @FormUrlEncoded
    @POST("getRanking.php") Call<ResponseRanking> getRanking(@Field("month") String month,
                                                            @Field("year") String year);
    @FormUrlEncoded
    @POST("getMyRanking.php") Call<ResponseMRanking> getMyRanking(@Field("month") String month,
                                                                @Field("year") String year,
                                                                 @Field("user") String user);

    //Point
    @FormUrlEncoded
    @POST("updateUserPoint.php") Call<Response> updateUserPoint(@Field("id") String id,
                                                                  @Field("status") String status,
                                                                  @Field("point") String point,
                                                                  @Field("description") String description);

    //Item
    @GET("getItemValid.php") Call<ResponseItem> getItem();
    @FormUrlEncoded
    @POST("buyItem.php") Call<ResponseItem> buyItem(@Field("id") String id,
                                                                @Field("id_item") String status,
                                                                @Field("description") String point,
                                                                @Field("point") String description,
                                                                @Field("price") String price);
    @FormUrlEncoded
    @POST("sortingItemBy.php") Call<ResponseItem> sortingItem(@Field("sort") String sort);
    @FormUrlEncoded
    @POST("statusItemBuy.php") Call<ResponseItem> statusItem(@Field("id") String id,
                                                             @Field("id_item") String id_item);

    //Quiz
    @POST("getQuizBy.php") Call<ResponseQuiz> getQuiz();
    @FormUrlEncoded
    @POST("getQuizQuestionBy.php") Call<ResponseQuizQuestion> getQuizQuestion(@Field("id") String id);
    @FormUrlEncoded
    @POST("getQuizAnswerBy.php") Call<ResponseQuizAnswer> getQuizAnswer(@Field("id") String id);
    @FormUrlEncoded
    @POST("getQuizAnswerByQuiz.php") Call<ResponseQuizAnswer> getQuizAnswerBy(@Field("id") String id);
    @FormUrlEncoded
    @POST("getQuizId.php") Call<ResponseQuiz> getQuizId(@Field("id") String id);
    @FormUrlEncoded
    @POST("storeQuizHistory.php") Call<ResponseQuiz> storeQuizHistory(@Field("id") String id,
                                                                    @Field("id_quiz") String id_quiz);
    @FormUrlEncoded
    @POST("getQuizByUser.php") Call<ResponseQuiz> getQuizByUser(@Field("id") String id,
                                                                      @Field("id_quiz") String id_quiz);



}

