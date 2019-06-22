package com.creative.share.apps.rubbish.activity_splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.activity_sign_in_sign_up.SignInActivity;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.creative.share.apps.rubbish.ui_employee.EmployeeHomeActivity;
import com.creative.share.apps.rubbish.ui_financial_manager.FinancialHomeActivity;
import com.creative.share.apps.rubbish.ui_head_manager.HeadMangerHomeActivity;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;

public class SplashActivity extends AppCompatActivity {

    private FrameLayout fl;
    private Animation animation;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        preference = Preference.newInstance();
        fl = findViewById(R.id.fl);
        animation = AnimationUtils.loadAnimation(this,R.anim.alpha);
        fl.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                String session = preference.getSession(SplashActivity.this);
                if (session.equals(Tags.SESSION_LOGIN))
                {
                    UserModel userModel = preference.getUserData(SplashActivity.this);
                    int user_type = userModel.getUser_type();
                    if (user_type == Tags.USER_TYPE_CLIENT)
                    {
                        Intent intent = new Intent(SplashActivity.this, ClientHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (user_type == Tags.USER_TYPE_EMPLOYEE)
                    {
                        Intent intent = new Intent(SplashActivity.this, EmployeeHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    else if (user_type == Tags.USER_TYPE_SUPERVISOR)
                    {
                        Intent intent = new Intent(SplashActivity.this, SupervisorHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    else if (user_type == Tags.USER_TYPE_FINANCIAL_MANAGER)
                    {
                        Intent intent = new Intent(SplashActivity.this, FinancialHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (user_type == Tags.USER_TYPE_HEAD_MANAGER)
                    {
                        Intent intent = new Intent(SplashActivity.this, HeadMangerHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }else
                    {
                        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
