package com.example.rest;

import com.example.model.AddPrayerDao;
import com.example.model.AnswerListDao;
import com.example.model.AnswerPrayerDao;
import com.example.model.ChangePasswordDao;
import com.example.model.ContactDao;
import com.example.model.CreateGroupDao;
import com.example.model.ForgotPasswordDao;
import com.example.model.GroupDetailDao;
import com.example.model.PaymentConfirmationResponce;
import com.example.model.PaymentDetail;
import com.example.model.PrayerDao;
import com.example.model.ServerResponse;
import com.example.model.ServerResponseHeart;
import com.example.model.SignUpDao;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Scorpion .
 */
public interface RestClient {

    @POST("api/register")
    Call<ServerResponse> signup(@Body SignUpDao request);
    @POST("api/login")
    Call<ServerResponse> login(@Body SignUpDao request);
    @POST("api/forget")
    Call<ServerResponse> forgotPassword(@Body ForgotPasswordDao forgotPasswordDao);

   @POST("api/contacts/{userId}")
    Call<ServerResponseHeart<List<ContactDao>>> syncContactToServer(@Path("userId") String userId, @Body HashMap<String, String> hashMapContact);
    @POST("api/get-group/{userId}")
    Call<ServerResponseHeart<List<GroupDetailDao>>> getGroupList(@Path("userId") String userId);
    @POST("api/create-group/{userId}")
    Call<ServerResponseHeart> createGroup(@Path("userId") String userId, @Body CreateGroupDao createGroupDao);
   @POST("api/get-prayer/{userId}")
    Call<ServerResponseHeart<List<PrayerDao>>> prayerList(@Path("userId") String userId);
    @POST("api/change-password/{userId}")
    Call<ServerResponse> changePassword(@Path("userId") String userId, @Body ChangePasswordDao changePasswordDao);
    @POST("api/edit-profile/{userId}")
    Call<ServerResponse> editProfile(@Path("userId") String userId,@Body SignUpDao request);
    @POST("api/add-prayer")
    Call<ServerResponse> addPrayer(@Body AddPrayerDao addPrayerDao);
    @POST("api/answer/{prayer_id}")
    Call<ServerResponse> answerPrayer(@Path("prayer_id") String prayer_id,@Body AnswerPrayerDao answerPrayerDao);
    @POST("api/delete/{prayer_id}")
    Call<ServerResponse> deletePrayer(@Path("prayer_id") String prayer_id,@Body AnswerPrayerDao answerPrayerDao);
     @POST("api/answer-prayer/{userId}")
    Call<ServerResponseHeart<List<AnswerListDao>>> answerPrayerList(@Path("userId") String userId);
    @POST("api/register-comfirm/{userId}")
    Call<ServerResponse> updatePaymentDetail(@Path("userId") String userId,@Body PaymentDetail paymentDetail);
    @POST("api/check-login/{userId}")
    Call<PaymentConfirmationResponce> isPayment(@Path("userId") String userId);
/*
    @POST("s/sendotp")
    Call<ServerResponse> sendEmail(@Body EmailDao emailDao);
  */
}