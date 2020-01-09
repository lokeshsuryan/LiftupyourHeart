package com.example.rest;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.model.AddPrayerDao;
import com.example.model.AnswerListDao;
import com.example.model.AnswerPrayerDao;
import com.example.model.ChangePasswordDao;
import com.example.model.ForgotPasswordDao;
import com.example.model.GroupDetailDao;
import com.example.model.PaymentConfirmationResponce;
import com.example.model.PaymentDetail;
import com.example.model.PrayerDao;
import com.example.model.ServerResponse;
import com.example.model.ServerResponseHeart;
import com.example.model.SignUpDao;
import com.example.utills.AppConstant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectRepository {

    private static ProjectRepository projectRepository;
    //…

    /**
     * SignUpApi calling
     *
     * @param signUpDao
     * @return
     */
    public LiveData<ServerResponse> getSinup(SignUpDao signUpDao) {
        final MutableLiveData<ServerResponse> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().signup(signUpDao).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                data.setValue(null);
            }
            // Error handling will be explained in the next article …
        });

        return data;
    }

    /**
     * editProfile Api calling
     *
     * @param userId
     * @param signUpDao
     * @return
     */


    public LiveData<ServerResponse> editProfile(String userId, SignUpDao signUpDao) {
        final MutableLiveData<ServerResponse> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().editProfile(userId, signUpDao).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                data.setValue(null);
            }
            // Error handling will be explained in the next article …
        });

        return data;
    }

    /**
     * forget password api
     *
     * @param forgotPasswordDao
     * @return
     */
    public LiveData<ServerResponse> getforgotPassword(ForgotPasswordDao forgotPasswordDao) {
        final MutableLiveData<ServerResponse> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().forgotPassword(forgotPasswordDao).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                data.setValue(null);
            }

            // Error handling will be explained in the next article …
        });

        return data;
    }

    /**
     * login Api calling
     *
     * @param signUpDao
     * @return
     */
    public LiveData<ServerResponse> getloginResponce(SignUpDao signUpDao) {
        final MutableLiveData<ServerResponse> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().login(signUpDao).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("eerror",t.toString());
                //Toast.makeText(ProjectRepository.this,"hdsghfsghdf",Toast.LENGTH_SHORT).show();
                data.setValue(null);
            }

            // Error handling will be explained in the next article …
        });

        return data;
    }


    /**
     * add prayer on server
     *
     * @param addPrayerDao
     * @return
     */

    public LiveData<ServerResponse> addPrayer(AddPrayerDao addPrayerDao) {
        final MutableLiveData<ServerResponse> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().addPrayer(addPrayerDao).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.code() == 200)
                   data.setValue(response.body());
                 else
                   data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                data.setValue(null);
            }

            // Error handling will be explained in the next article …
        });

        return data;
    }

    /**
     * group DEtail api response
     *
     * @param userId
     * @return
     */
    public LiveData<ServerResponseHeart<List<GroupDetailDao>>> getGroupRespone(String userId) {
        final MutableLiveData<ServerResponseHeart<List<GroupDetailDao>>> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().getGroupList(userId).enqueue(new Callback<ServerResponseHeart<List<GroupDetailDao>>>() {
            @Override
            public void onResponse(Call<ServerResponseHeart<List<GroupDetailDao>>> call, Response<ServerResponseHeart<List<GroupDetailDao>>> response) {
                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponseHeart<List<GroupDetailDao>>> call, Throwable t) {
                data.setValue(null);
            }

            // Error handling will be explained in the next article …
        });

        return data;
    }

    /**
     * change Password Api calling
     *
     * @param userID
     * @param changePasswordDao
     * @return
     */
    public LiveData<ServerResponse> changePasswordResponce(String userID, ChangePasswordDao changePasswordDao) {
        final MutableLiveData<ServerResponse> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().changePassword(userID, changePasswordDao).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                data.setValue(null);
            }

            // Error handling will be explained in the next article …
        });

        return data;
    }

    /**
     * answer prayer Api
     *
     * @param prayerId
     * @param answerPrayerDao
     * @return
     */
    public LiveData<ServerResponse> answerPrayer(String prayerId, AnswerPrayerDao answerPrayerDao) {
        final MutableLiveData<ServerResponse> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().answerPrayer(prayerId, answerPrayerDao).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                data.setValue(null);
            }

            // Error handling will be explained in the next article …
        });

        return data;
    }

    /**
     * delete prayer Api
     *
     * @param prayerId
     * @param answerPrayerDao
     * @return
     */
    public LiveData<ServerResponse> deletePrayer(String prayerId, AnswerPrayerDao answerPrayerDao) {
        final MutableLiveData<ServerResponse> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().deletePrayer(prayerId, answerPrayerDao).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                data.setValue(null);
            }

            // Error handling will be explained in the next article …
        });

        return data;
    }

    /**
     * Answer Prayer List Api Calling
     *
     * @return
     */

    public LiveData<ServerResponseHeart<List<AnswerListDao>>> AnswerPrayerList() {
        final MutableLiveData<ServerResponseHeart<List<AnswerListDao>>> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().answerPrayerList(AppConstant.userId).enqueue(new Callback<ServerResponseHeart<List<AnswerListDao>>>() {
            @Override
            public void onResponse(Call<ServerResponseHeart<List<AnswerListDao>>> call, Response<ServerResponseHeart<List<AnswerListDao>>> response) {
                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponseHeart<List<AnswerListDao>>> call, Throwable t) {
                data.setValue(null);
            }

            // Error handling will be explained in the next article …
        });

        return data;
    }

    /**
     * Check payment Detail List Api Calling
     *
     * @return
     */

    public LiveData<PaymentConfirmationResponce> isPayment() {
        final MutableLiveData<PaymentConfirmationResponce> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().isPayment(AppConstant.userId).enqueue(new Callback<PaymentConfirmationResponce>() {
            @Override
            public void onResponse(Call<PaymentConfirmationResponce> call, Response<PaymentConfirmationResponce> response) {
                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<PaymentConfirmationResponce> call, Throwable t) {
                data.setValue(null);
            }

            // Error handling will be explained in the next article …
        });

        return data;
    }
    /**
     * Check payment Detail List Api Calling
     *
     * @return
     */

    public LiveData<ServerResponse> updatePaymentDetail(PaymentDetail paymentDetail) {
        final MutableLiveData<ServerResponse> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().updatePaymentDetail(AppConstant.userId,paymentDetail).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                data.setValue(null);
            }

            // Error handling will be explained in the next article …
        });

        return data;
    }



    public LiveData<ServerResponseHeart<List<PrayerDao>>> prayerList() {
        final MutableLiveData<ServerResponseHeart<List<PrayerDao>>> data = new MutableLiveData<>();

        Singleton.getInstance().getRestOkClient().prayerList(AppConstant.userId).enqueue(new Callback<ServerResponseHeart<List<PrayerDao>>>() {
            @Override
            public void onResponse(Call<ServerResponseHeart<List<PrayerDao>>> call, Response<ServerResponseHeart<List<PrayerDao>>> response) {
                if (response.code() == 200)
                    data.setValue(response.body());
                else
                    data.setValue(null);
            }

            @Override
            public void onFailure(Call<ServerResponseHeart<List<PrayerDao>>> call, Throwable t) {
                data.setValue(null);
            }

            // Error handling will be explained in the next article …
        });

        return data;
    }

    public static ProjectRepository getInstance() {
        if (projectRepository == null) { //if there is no instance available... create new one
            projectRepository = new ProjectRepository();
        }

        return projectRepository;
    }
    // …
}
