package com.liftupmyheart.rest;

import com.liftupmyheart.model.AddPrayerDao;
import com.liftupmyheart.model.AnswerListDao;
import com.liftupmyheart.model.AnswerPrayerDao;
import com.liftupmyheart.model.ChangePasswordDao;
import com.liftupmyheart.model.ContactDao;
import com.liftupmyheart.model.CreateGroupDao;
import com.liftupmyheart.model.ForgotPasswordDao;
import com.liftupmyheart.model.GroupDetailDao;
import com.liftupmyheart.model.PaymentConfirmationResponce;
import com.liftupmyheart.model.PaymentDetail;
import com.liftupmyheart.model.PrayerDao;
import com.liftupmyheart.model.ServerResponse;
import com.liftupmyheart.model.ServerResponseHeart;
import com.liftupmyheart.model.SignUpDao;

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