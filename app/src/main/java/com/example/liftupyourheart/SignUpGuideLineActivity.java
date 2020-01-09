package com.example.liftupyourheart;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.adapter.GuidelineAdapter;
import com.example.liftupyourheart.databinding.SignUpGuideLineBinding;
import com.example.listner.SignUpListner;
import com.example.model.ForgotPasswordDao;
import com.example.model.ServerResponse;
import com.example.model.SignUpDao;
import com.example.utills.AppConstant;
import com.example.utills.PreferanceUtils;
import com.example.utills.Utills;
import com.example.viewModel.SignUpViewModel;
import com.google.gson.Gson;

import java.util.regex.Pattern;

public class SignUpGuideLineActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, Animation.AnimationListener, SignUpListner, View.OnClickListener {
    int dotsCount;
    ImageView[] dots;
    private SignUpGuideLineBinding bind;
    GuidelineAdapter guidelineAdapter;
    private Animation popupShow;
    private Animation popupHide;
    SignUpViewModel signUpViewModel;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String MobilePattern = "[0-9]{10}";
    int[] mResources = {
            R.drawable.screen1_signup,
            R.drawable.screen2_signup,
            R.drawable.screen3_signup,
            R.drawable.screen4_signup,
            R.drawable.screenbg_signup
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bind = DataBindingUtil.setContentView(this, R.layout.sign_up_guide_line);
        initView();

    }

    private void initView() {
        popupShow = AnimationUtils.loadAnimation(this, R.anim.popup_show);
        popupShow.setAnimationListener(this);
        popupHide = AnimationUtils.loadAnimation(this, R.anim.popup_hide);
        popupHide.setAnimationListener(this);
        bind.linearLayoutPopUp.setVisibility(View.GONE);
        guidelineAdapter = new GuidelineAdapter(this, mResources, this);
        bind.pagerSignUp.setAdapter(guidelineAdapter);
        bind.pagerSignUp.addOnPageChangeListener(this);
        bind.closeView.setOnClickListener(this);
        bind.signUp.setOnClickListener(this);
        setUiPageViewController();
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.page_indicator_selected));
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
    }

    private void setUiPageViewController() {

        dotsCount = guidelineAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unselected));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            // left,top,right,bottom
            params.setMargins(4, 0, 4, 0);
            dots[i].setLayoutParams(params);

            bind.viewPagerCountDots.addView(dots[i], params);
            //bind.viewPagerCountDots.bringToFront();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unselected));
        }
        dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.page_indicator_selected));

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation.equals(popupShow)) {
            bind.linearLayoutPopUp.setVisibility(View.VISIBLE);
        } else if (animation.equals(popupHide)) {
            bind.linearLayoutPopUp.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation.equals(popupShow)) {
            //bind.linearLayoutPopUp.setText(getString(R.string.btn_hide_txt));
        } else if (animation.equals(popupHide)) {
            //bind.linearLayoutPopUp.setText(getString(R.string.btn_show_txt));
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void loginMethod() {
        if (bind.linearLayoutPopUp.getVisibility() == View.GONE) {
            bind.linearLayoutPopUp.startAnimation(popupShow);
            bind.linearLayoutPopUp.setVisibility(View.VISIBLE);
            bind.cardViewLoginUp.setVisibility(View.VISIBLE);
            bind.cardViewSignUp.setVisibility(View.GONE);
            bind.signUp.setBackground(getResources().getDrawable(R.drawable.rounded_corner_yellow));
            bind.termCondition.setText("Forgot your Password?");
            bind.termCondition.setTextColor(getResources().getColor(R.color.white));
            bind.termCondition.setOnClickListener(view -> {
                showDialog(SignUpGuideLineActivity.this, "");
            });
        } else {
            bind.linearLayoutPopUp.startAnimation(popupHide);
            //bind.linearLayoutPopUp.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void signUpMethod() {

        if (bind.linearLayoutPopUp.getVisibility() == View.GONE) {
            bind.linearLayoutPopUp.startAnimation(popupShow);
            bind.linearLayoutPopUp.setVisibility(View.VISIBLE);
            bind.cardViewLoginUp.setVisibility(View.GONE);
            bind.signUp.setBackground(getResources().getDrawable(R.drawable.rounded_corner_blue));
            bind.signUp.setText(getString(R.string.signup));
            bind.cardViewSignUp.setVisibility(View.VISIBLE);
            SpannableString spannableString = new SpannableString("By Signing up, you are agree to our \n Terms & Conditions and Privacy policy");
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, 37, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 38, 56, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 57, 60, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 61, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstant.TERMS_CONDITION));
                    startActivity(browserIntent);
                    //Toast.makeText(RegisterActivity.this, "link clicked1", Toast.LENGTH_SHORT).show();
                }
            }, 37, 56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstant.PRIVACY_POLCY));
                    startActivity(browserIntent);
                    //Toast.makeText(RegisterActivity.this, "link clicked2", Toast.LENGTH_SHORT).show();
                }
            }, 61, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            bind.termCondition.setMovementMethod(LinkMovementMethod.getInstance());
            bind.termCondition.setText(spannableString);

            //bind.termCondition.setText("Forgot your Password?");
        } else {
            bind.linearLayoutPopUp.startAnimation(popupHide);
            //bind.linearLayoutPopUp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeView:
                if (bind.linearLayoutPopUp.getVisibility() == View.GONE) {
                    bind.linearLayoutPopUp.startAnimation(popupShow);
                    //bind.linearLayoutPopUp.setVisibility(View.VISIBLE);
                } else {
                    bind.linearLayoutPopUp.startAnimation(popupHide);
                    //bind.linearLayoutPopUp.setVisibility(View.VISIBLE);
                }
            case R.id.signUp:
                if (bind.cardViewLoginUp.getVisibility() == View.VISIBLE) {
                    String emailID = bind.emailIdLogin.getText().toString();
                    String pass = bind.passWordLogin.getText().toString();
                    if (!emailValidation(emailID)) {
                        bind.emailIdLogin.setError("Invalid email address");
                    } else if (pass.length() <= 4) {
                        bind.passWordLogin.setError("enter 6 char minimum");
                    } else {
                        Utills.showProgressbar(bind.loaderLayoutSP);
                        SignUpDao signUpDao = new SignUpDao();
                        signUpDao.setEmail(emailID);
                        signUpDao.setPassword(pass);
                        signUpViewModel.getLoginRespone(signUpDao).observe(this, new Observer<ServerResponse>() {
                            @Override
                            public void onChanged(@Nullable ServerResponse serverResponse) {

                                if (serverResponse != null) {
                                    if (serverResponse.getResponse() == 1) {
                                        Toast.makeText(SignUpGuideLineActivity.this, serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                                        Gson gson = new Gson();
                                        AppConstant.userId = serverResponse.getData().getId();
                                        String jsonObject = gson.toJson(serverResponse);
                                        PreferanceUtils.setLoginDetail(SignUpGuideLineActivity.this, jsonObject);
                                        PreferanceUtils.setPaymentTime(SignUpGuideLineActivity.this, false);
                                        //Log.d("loginDetail",PreferanceUtils.getLoginDetail(SignUpGuideLineActivity.this));
                                        Intent registerIntent;
                                        if(serverResponse.getData().getIs_paid().equalsIgnoreCase("0")){
                                             registerIntent = new Intent(SignUpGuideLineActivity.this, BrowsePlan.class);
                                        }
                                        else
                                         registerIntent = new Intent(SignUpGuideLineActivity.this, MainActivity.class);

                                        startActivity(registerIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(SignUpGuideLineActivity.this, serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(SignUpGuideLineActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                                }
                                Utills.hideProgressbar(bind.loaderLayoutSP);

                            }
                        });

                    }
                } else if (bind.cardViewSignUp.getVisibility() == View.VISIBLE) {
                    String firstName = bind.firstName.getText().toString();
                    String lastname = bind.lastname.getText().toString();
                    String emailID = bind.emailId.getText().toString();
                    String pass = bind.passWord.getText().toString();
                    String phoneNo = bind.phoneNo.getText().toString();

                    if (firstName.isEmpty()) {
                        bind.firstName.setError("Enter first name");
                    } else if (lastname.isEmpty()) {
                        bind.lastname.setError("Enter last name");
                    } else if (!emailValidation(emailID)) {
                        bind.emailId.setError("Invalid email address");
                    } else if (pass.length() <= 4) {
                        bind.passWord.setError("enter 6 char minimum");
                    } else if (!isValidMobile(phoneNo)) {
                        bind.phoneNo.setError("Not Valid Number");
                    } else {
                        SignUpDao signUpDao = new SignUpDao();
                        signUpDao.setEmail(emailID);
                        signUpDao.setFirst_name(firstName);
                        signUpDao.setLast_name(lastname);
                        signUpDao.setPhone(phoneNo);
                        signUpDao.setPassword(pass);
                        Utills.showProgressbar(bind.loaderLayoutSP);
                        signUpViewModel.getSignUpRespone(signUpDao).observe(this, new Observer<ServerResponse>() {
                            @Override
                            public void onChanged(@Nullable ServerResponse serverResponse) {
                                Utills.hideProgressbar(bind.loaderLayoutSP);
                                if (serverResponse != null) {
                                    if (serverResponse.getResponse() == 1) {
                                        Toast.makeText(SignUpGuideLineActivity.this, serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                                        AppConstant.userId = serverResponse.getData().getId();
                                        Gson gson = new Gson();
                                        String jsonObject = gson.toJson(serverResponse);
                                        PreferanceUtils.setLoginDetail(SignUpGuideLineActivity.this, jsonObject);
                                        Intent registerIntent;
                                        if(serverResponse.getData().getIs_paid().equalsIgnoreCase("0")){
                                            registerIntent = new Intent(SignUpGuideLineActivity.this, BrowsePlan.class);
                                        }
                                        else
                                            registerIntent = new Intent(SignUpGuideLineActivity.this, MainActivity.class);
                                        startActivity(registerIntent);
                                        finish();
                                    }else {
                                        Toast.makeText(SignUpGuideLineActivity.this, serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(SignUpGuideLineActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                    }
                    //phoneNo;
                    break;
                }

        }
    }

    public void showDialog(AppCompatActivity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_forgot_password);
        final EditText emailIdForgot = (EditText) dialog.findViewById(R.id.emailIdForgot);
        Button dialogButton = (Button) dialog.findViewById(R.id.ok);
        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailID = emailIdForgot.getText().toString();
                if (!emailValidation(emailID)) {
                    emailIdForgot.setError("Invalid email address");
                    //
                } else {
                    Utills.showProgressbar(bind.loaderLayoutSP);
                    ForgotPasswordDao forgotPasswordDao = new ForgotPasswordDao();
                    forgotPasswordDao.setEmail(emailID);
                    signUpViewModel.getForgetRespone(forgotPasswordDao).observe(SignUpGuideLineActivity.this, new Observer<ServerResponse>() {
                        @Override
                        public void onChanged(@Nullable ServerResponse serverResponse) {
                            Utills.hideProgressbar(bind.loaderLayoutSP);
                            if (serverResponse != null) {
                                if (serverResponse.getResponse() == 1) {
                                    Toast.makeText(SignUpGuideLineActivity.this, serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SignUpGuideLineActivity.this, serverResponse.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            } else {
                                Toast.makeText(SignUpGuideLineActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                            }
                            //PreferanceUtils.setLoginDetail(SignUpGuideLineActivity.this,serverResponse.getData().toString());
                            //Log.d("loginDetail",PreferanceUtils.getLoginDetail(SignUpGuideLineActivity.this));
                        }
                    });
                    dialog.dismiss();
                }

            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public boolean emailValidation(String emailId) {
        if (emailId.matches(emailPattern)) {

            return true;
        }
        return false;
    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                //txtPhone.setError("");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }
}
