package com.creative.share.apps.rubbish.ui_clients;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.activity_sign_in_sign_up.SignInActivity;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Contact_Us;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Profile;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Settings;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Terms_Condition;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_clients.fragments.Client_Fragment_Home;
import com.creative.share.apps.rubbish.ui_clients.fragments.Client_Fragment_Notifications;
import com.creative.share.apps.rubbish.ui_clients.fragments.Client_Fragment_main;
import com.creative.share.apps.rubbish.ui_clients.fragments.Fragment_Client_Send_Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ClientHomeActivity extends AppCompatActivity {

    private int fragment_count = 0;
    private FragmentManager fragmentManager;
    private Client_Fragment_Home client_fragment_home;
    private Client_Fragment_main client_fragment_main;
    private Client_Fragment_Notifications client_fragment_notifications;
    private Fragment_Profile fragment_profile;
    private Fragment_Settings fragment_settings;
    private Fragment_Terms_Condition fragment_terms_condition;
    private Fragment_Contact_Us fragment_contact_us;
    private Fragment_Client_Send_Order fragment_client_send_order;

    private UserModel userModel;
    private Preference preference;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);
        mAuth = FirebaseAuth.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference();
        preference = Preference.newInstance();
        userModel = preference.getUserData(this);
        fragmentManager = this.getSupportFragmentManager();

        if (savedInstanceState == null) {
            DisplayFragmentHome();
            DisplayFragmentMain();
        }

    }


    public void DisplayFragmentHome()
    {

        fragment_count += 1;

        if (client_fragment_home == null) {
            client_fragment_home = Client_Fragment_Home.newInstance();
        }

        if (client_fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(client_fragment_home).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, client_fragment_home, "client_fragment_home").addToBackStack("client_fragment_home").commit();
        }

    }
    public void DisplayFragmentMain()
    {


        if (client_fragment_notifications != null && client_fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(client_fragment_notifications).commit();
        }

        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }

        if (client_fragment_main == null) {
            client_fragment_main = Client_Fragment_main.newInstance();
        }

        if (client_fragment_main.isAdded()) {
            fragmentManager.beginTransaction().show(client_fragment_main).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, client_fragment_main, "client_fragment_main").addToBackStack("client_fragment_main").commit();

        }
        if (client_fragment_home != null && client_fragment_home.isAdded()) {
            client_fragment_home.UpdateAHBottomNavigationPosition(0);
        }


    }

    public void DisplayFragmentNotification()
    {




        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }
        if (client_fragment_main != null && client_fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(client_fragment_main).commit();
        }

        if (client_fragment_notifications == null) {
            client_fragment_notifications = Client_Fragment_Notifications.newInstance();
        }

        if (client_fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().show(client_fragment_notifications).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, client_fragment_notifications, "client_fragment_notifications").addToBackStack("client_fragment_notifications").commit();

        }
        if (client_fragment_home != null && client_fragment_home.isAdded()) {
            client_fragment_home.UpdateAHBottomNavigationPosition(1);
        }

    }
    public void DisplayFragmentProfile()
    {

        if (client_fragment_main != null && client_fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(client_fragment_main).commit();
        }
        if (client_fragment_notifications != null && client_fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(client_fragment_notifications).commit();
        }




        if (fragment_profile == null) {
            fragment_profile = Fragment_Profile.newInstance();
        }

        if (fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

        }
        if (client_fragment_home != null && client_fragment_home.isAdded()) {
            client_fragment_home.UpdateAHBottomNavigationPosition(2);
        }

    }

    public void DisplayFragmentSettings()
    {

        fragment_count +=1;

        fragment_settings = Fragment_Settings.newInstance();


        if (fragment_settings.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_settings).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_settings, "fragment_settings").addToBackStack("fragment_settings").commit();

        }


    }

    public void DisplayFragmentTerms_AboutUs(int type)
    {

        fragment_count +=1;

        fragment_terms_condition = Fragment_Terms_Condition.newInstance(type);


        if (fragment_terms_condition.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_terms_condition).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_terms_condition, "fragment_terms_condition").addToBackStack("fragment_terms_condition").commit();

        }


    }

    public void DisplayFragmentContact()
    {

        fragment_count +=1;

        fragment_contact_us = Fragment_Contact_Us.newInstance();


        if (fragment_contact_us.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_contact_us).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_contact_us, "fragment_contact_us").addToBackStack("fragment_contact_us").commit();

        }


    }
    public void DisplayFragmentSendOrder()
    {

        fragment_count +=1;

        fragment_client_send_order = Fragment_Client_Send_Order.newInstance();



        if (fragment_client_send_order.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_client_send_order).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_client_send_order, "fragment_client_send_order").addToBackStack("fragment_client_send_order").commit();

        }


    }






    public void NavigateToSignInActivity(boolean isSignIn) {

        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("sign_up",isSignIn);
        startActivity(intent);
        finish();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        Back();
    }

    public void Back() {
        if (fragment_count > 1) {
            super.onBackPressed();
            fragment_count -= 1;
        } else {
            if (client_fragment_main!=null&&client_fragment_main.isAdded()&&!client_fragment_main.isVisible())
            {
                DisplayFragmentMain();
            }else
            {
                if (userModel!=null)
                {
                    finish();
                }else
                {
                    NavigateToSignInActivity(true);
                }
            }
        }

    }



    public void CreateUserNotSignInAlertDialog()
    {


        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null);
        Button btn_sign_in = view.findViewById(R.id.btn_sign_in);
        Button btn_sign_up = view.findViewById(R.id.btn_sign_up);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        TextView tv_msg = view.findViewById(R.id.tv_msg);
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                NavigateToSignInActivity(true);
                finish();



            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                NavigateToSignInActivity(false);
                finish();



            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations= R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();

    }



    public void Logout() {

        mAuth.signOut();
        preference.create_update_session(this, Tags.SESSION_LOGOUT);
        NavigateToSignInActivity(true);
    }

}
