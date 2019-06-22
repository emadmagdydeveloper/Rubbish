package com.creative.share.apps.rubbish.activity_sign_in_sign_up;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.activity_sign_in_sign_up.fragments.Fragment_Login;
import com.creative.share.apps.rubbish.activity_sign_in_sign_up.fragments.Fragment_Signup;
import com.creative.share.apps.rubbish.tags.Tags;

public class SignInActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment_Login fragmentLogin;
    private Fragment_Signup fragmentSignup;

    private int fragment_counter = 0;
    private Preference preferences;
    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        preferences = Preference.newInstance();


        if (savedInstanceState == null) {
            fragmentManager = this.getSupportFragmentManager();

            if (preferences.getSession(this).equals(Tags.SESSION_LOGIN))
            {

                userModel = preferences.getUserData(this);

                NavigateToHomeActivity(userModel.getUser_type());
            }else
            {
                DisplayFragmentLogin();

            }

        }

        getDataFromIntent();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("sign_up"))
        {
            boolean isSign_in = intent.getBooleanExtra("sign_up",true);
            if (!isSign_in)
            {
                DisplayFragmentSignUp();

            }
        }
    }




    private void DisplayFragmentLogin()
    {

        fragment_counter += 1;
        if (fragmentLogin == null) {
            fragmentLogin = Fragment_Login.newInstance();
        }
        if (fragmentLogin.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentLogin).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragmentLogin, "fragmentLogin").addToBackStack("fragmentLogin").commit();
        }
    }

    public void DisplayFragmentSignUp()
    {

        fragment_counter += 1;

        if (fragmentSignup == null) {
            fragmentSignup = Fragment_Signup.newInstance();
        }
        if (fragmentSignup.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentSignup).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragmentSignup, "fragmentSignup").addToBackStack("fragmentSignup").commit();
        }
    }

    public void NavigateToHomeActivity(int type)
    {
        if (type ==Tags.USER_TYPE_CLIENT||type ==0)
        {
            Intent intent = new Intent(SignInActivity.this, ClientHomeActivity.class);
            startActivity(intent);
            finish();

        }else if (type ==Tags.USER_TYPE_EMPLOYEE)
        {

        }
        else if (type ==Tags.USER_TYPE_FINANCIAL_MANAGER)
        {

        }
        else if (type ==Tags.USER_TYPE_SUPERVISOR)
        {

        }
        else if (type ==Tags.USER_TYPE_HEAD_MANAGER)
        {

        }

    }



    public void Back() {
        if (fragment_counter == 1) {
            finish();
        } else {
            fragment_counter -= 1;
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        Back();
    }



}
