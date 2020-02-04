package com.liftupmyheart.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.liftupmyheart.model.AddPrayerDao;
import com.liftupmyheart.model.AnswerListDao;
import com.liftupmyheart.model.AnswerPrayerDao;
import com.liftupmyheart.model.ChangePasswordDao;
import com.liftupmyheart.model.ForgotPasswordDao;
import com.liftupmyheart.model.PaymentConfirmationResponce;
import com.liftupmyheart.model.PaymentDetail;
import com.liftupmyheart.model.PrayerDao;
import com.liftupmyheart.model.ServerResponse;
import com.liftupmyheart.model.ServerResponseHeart;
import com.liftupmyheart.model.SignUpDao;
import com.liftupmyheart.rest.ProjectRepository;
import com.liftupmyheart.utills.AppConstant;

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
