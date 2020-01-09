package com.example.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.model.AddPrayerDao;
import com.example.model.AnswerListDao;
import com.example.model.AnswerPrayerDao;
import com.example.model.ChangePasswordDao;
import com.example.model.ForgotPasswordDao;
import com.example.model.PaymentConfirmationResponce;
import com.example.model.PaymentDetail;
import com.example.model.PrayerDao;
import com.example.model.ServerResponse;
import com.example.model.ServerResponseHeart;
import com.example.model.SignUpDao;
import com.example.rest.ProjectRepository;
import com.example.utills.AppConstant;

import java.util.List;

public class SignUpViewModel extends AndroidViewModel {
    public SignUpViewModel(Application application) {
        super(application);
    }

    public LiveData<ServerResponse> getSignUpRespone(SignUpDao signUpDao){
        return ProjectRepository.getInstance().getSinup(signUpDao);
    }
    public LiveData<ServerResponse> editProfile(SignUpDao signUpDao){
        return ProjectRepository.getInstance().editProfile(AppConstant.userId,signUpDao);
    }
    public LiveData<ServerResponse> getForgetRespone(ForgotPasswordDao forgotPasswordDao){
        return ProjectRepository.getInstance().getforgotPassword(forgotPasswordDao);
    }
    public LiveData<ServerResponse> getLoginRespone(SignUpDao signUpDao){
        return ProjectRepository.getInstance().getloginResponce(signUpDao);
    }

    public LiveData<ServerResponse> changePasswordRespone(ChangePasswordDao changePasswordDao){
        return ProjectRepository.getInstance().changePasswordResponce(AppConstant.userId,changePasswordDao);
    }
    public LiveData<ServerResponse> addPrayer(AddPrayerDao addPrayerDao){
        return ProjectRepository.getInstance().addPrayer(addPrayerDao);
    }
    public LiveData<ServerResponse> answerPrayer(String prayerId,AnswerPrayerDao answerPrayerDao){
        return ProjectRepository.getInstance().answerPrayer(prayerId,answerPrayerDao);
    }
    public LiveData<ServerResponse> deletePrayer(String prayerId,AnswerPrayerDao answerPrayerDao){
        return ProjectRepository.getInstance().deletePrayer(prayerId,answerPrayerDao);
    }
    public LiveData<ServerResponseHeart<List<AnswerListDao>>> answerPrayerList(){
        return ProjectRepository.getInstance().AnswerPrayerList();
    }
    public LiveData<ServerResponseHeart<List<PrayerDao>>> prayerList(){
        return ProjectRepository.getInstance().prayerList();
    }
    public LiveData<PaymentConfirmationResponce> paymentDetail(){
        return ProjectRepository.getInstance().isPayment();
    }

    public LiveData<ServerResponse> updatePaymentDetail(PaymentDetail paymentDetail){
        return ProjectRepository.getInstance().updatePaymentDetail(paymentDetail);
    }
}
