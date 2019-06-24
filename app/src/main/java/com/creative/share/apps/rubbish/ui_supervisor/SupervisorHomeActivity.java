package com.creative.share.apps.rubbish.ui_supervisor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.activity_sign_in_sign_up.SignInActivity;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Account_Deactivated;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Contact_Us;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Edit_Profile;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Profile;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Settings;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Terms_Condition;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_supervisor.fragments.Fragment_Supervisor_Send_Report;
import com.creative.share.apps.rubbish.ui_supervisor.fragments.Fragment_Supervisor_User_Data;
import com.creative.share.apps.rubbish.ui_supervisor.fragments.Supervisor_Fragment_Home;
import com.creative.share.apps.rubbish.ui_supervisor.fragments.Supervisor_Fragment_Orders;
import com.creative.share.apps.rubbish.ui_supervisor.fragments.Supervisor_Fragment_Search;
import com.creative.share.apps.rubbish.ui_supervisor.fragments.Supervisor_Fragment_Support;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SupervisorHomeActivity extends AppCompatActivity {

    private int fragment_count = 0;
    private FragmentManager fragmentManager;
    private Supervisor_Fragment_Home supervisor_fragment_home;
    private Fragment_Profile fragment_profile;
    private Supervisor_Fragment_Search supervisor_fragment_search;
    private Supervisor_Fragment_Orders supervisor_fragment_orders;
    private Fragment_Settings fragment_settings;
    private Fragment_Terms_Condition fragment_terms_condition;
    private Fragment_Contact_Us fragment_contact_us;
    private Fragment_Supervisor_User_Data fragment_supervisor_user_data;
    private Fragment_Supervisor_Send_Report fragment_supervisor_send_report;
    private Supervisor_Fragment_Support supervisor_fragment_support;
    private Fragment_Account_Deactivated fragment_account_deactivated;
    private Fragment_Edit_Profile fragment_edit_profile;


    private UserModel userModel;
    private Preference preference;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_home);
        dRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        preference = Preference.newInstance();
        userModel = preference.getUserData(this);
        fragmentManager = this.getSupportFragmentManager();

        if (savedInstanceState == null) {
            DisplayFragmentHome();
            DisplayFragmentProfile();
            if (userModel!=null)
            {
                getUserAccountState();
            }
        }
    }

    private void getUserAccountState() {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        dRef.child("Users")
                .child(userModel.getUser_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null)
                        {
                            dialog.dismiss();
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            if (userModel!=null&&userModel.getActive()==0)
                            {
                                DisplayFragmentDeactivatedAccount();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }


    public void DisplayFragmentHome()
    {

        fragment_count += 1;

        if (supervisor_fragment_home == null) {
            supervisor_fragment_home = Supervisor_Fragment_Home.newInstance();
        }

        if (supervisor_fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(supervisor_fragment_home).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, supervisor_fragment_home, "supervisor_fragment_home").addToBackStack("supervisor_fragment_home").commit();
        }

    }

    public void DisplayFragmentProfile()
    {

        if (supervisor_fragment_support!=null&&supervisor_fragment_support.isAdded())
        {
            fragmentManager.beginTransaction().hide(supervisor_fragment_support).commit();
        }
        if (supervisor_fragment_orders!=null&&supervisor_fragment_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(supervisor_fragment_orders).commit();
        }

        if (supervisor_fragment_search!=null&&supervisor_fragment_search.isAdded())
        {
            fragmentManager.beginTransaction().hide(supervisor_fragment_search).commit();
        }

        if (fragment_profile == null) {
            fragment_profile = Fragment_Profile.newInstance();
        }


        if (fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

        }
        if (supervisor_fragment_home != null && supervisor_fragment_home.isAdded()) {
            supervisor_fragment_home.UpdateAHBottomNavigationPosition(0);
        }

    }

    public void DisplayFragmentOrder()
    {

        if (supervisor_fragment_support!=null&&supervisor_fragment_support.isAdded())
        {
            fragmentManager.beginTransaction().hide(supervisor_fragment_support).commit();
        }
        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }


        if (supervisor_fragment_search!=null&&supervisor_fragment_search.isAdded())
        {
            fragmentManager.beginTransaction().hide(supervisor_fragment_search).commit();
        }


        if (supervisor_fragment_orders == null) {
            supervisor_fragment_orders = Supervisor_Fragment_Orders.newInstance();
        }

        if (supervisor_fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().show(supervisor_fragment_orders).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, supervisor_fragment_orders, "supervisor_fragment_orders").addToBackStack("supervisor_fragment_orders").commit();

        }
        if (supervisor_fragment_home != null && supervisor_fragment_home.isAdded()) {
            supervisor_fragment_home.UpdateAHBottomNavigationPosition(1);
        }

    }


    public void DisplayFragmentSupport()
    {

        if (supervisor_fragment_orders!=null&&supervisor_fragment_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(supervisor_fragment_orders).commit();
        }
        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }


        if (supervisor_fragment_search!=null&&supervisor_fragment_search.isAdded())
        {
            fragmentManager.beginTransaction().hide(supervisor_fragment_search).commit();
        }


        if (supervisor_fragment_support == null) {
            supervisor_fragment_support = Supervisor_Fragment_Support.newInstance();
        }

        if (supervisor_fragment_support.isAdded()) {
            fragmentManager.beginTransaction().show(supervisor_fragment_support).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, supervisor_fragment_support, "supervisor_fragment_support").addToBackStack("supervisor_fragment_support").commit();

        }
        if (supervisor_fragment_home != null && supervisor_fragment_home.isAdded()) {
            supervisor_fragment_home.UpdateAHBottomNavigationPosition(3);
        }

    }

    public void DisplayFragmentSearch()
    {


        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }


        if (supervisor_fragment_orders!=null&&supervisor_fragment_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(supervisor_fragment_orders).commit();
        }

        if (supervisor_fragment_search == null) {
            supervisor_fragment_search = Supervisor_Fragment_Search.newInstance();
        }

        if (supervisor_fragment_search.isAdded()) {
            fragmentManager.beginTransaction().show(supervisor_fragment_search).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, supervisor_fragment_search, "supervisor_fragment_search").addToBackStack("supervisor_fragment_search").commit();

        }
        if (supervisor_fragment_home != null && supervisor_fragment_home.isAdded()) {
            supervisor_fragment_home.UpdateAHBottomNavigationPosition(2);
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
    public void DisplayFragmentDeactivatedAccount()
    {

        fragment_count +=1;

        fragment_account_deactivated = Fragment_Account_Deactivated.newInstance();


        if (fragment_account_deactivated.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_account_deactivated).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_account_deactivated, "fragment_account_deactivated").addToBackStack("fragment_account_deactivated").commit();

        }


    }

    public void DisplayFragmentUserData(UserModel userModel)
    {

        fragment_count +=1;

        fragment_supervisor_user_data = Fragment_Supervisor_User_Data.newInstance(userModel);


        if (fragment_supervisor_user_data.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_supervisor_user_data).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_supervisor_user_data, "fragment_supervisor_user_data").addToBackStack("fragment_supervisor_user_data").commit();

        }


    }
    public void DisplayFragmentSendReport(UserModel userModel)
    {

        fragment_count +=1;

        fragment_supervisor_send_report = Fragment_Supervisor_Send_Report.newInstance(userModel);


        if (fragment_supervisor_send_report.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_supervisor_send_report).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_supervisor_send_report, "fragment_supervisor_send_report").addToBackStack("fragment_supervisor_send_report").commit();

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

    public void DisplayFragmentEditProfile()
    {

        fragment_count +=1;

        fragment_edit_profile = Fragment_Edit_Profile.newInstance();



        if (fragment_edit_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_edit_profile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_edit_profile, "fragment_edit_profile").addToBackStack("fragment_edit_profile").commit();

        }


    }

    public void UpdateUserData(UserModel userModel)
    {
        this.userModel = userModel;
        preference.create_update_user_data(this,userModel);
        if (fragment_profile!=null&&fragment_profile.isAdded())
        {
            fragment_profile.updateUserData(userModel);
        }
    }

    public void NavigateToSignInActivity(boolean isSignIn) {

        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("sign_up",isSignIn);
        startActivity(intent);
        finish();

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
            if (fragment_profile!=null&&fragment_profile.isAdded()&&!fragment_profile.isVisible())
            {
                DisplayFragmentProfile();
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

    public void Logout() {

        mAuth.signOut();
        userModel = null;
        preference.create_update_user_data(this,null);
        preference.create_update_session(this, Tags.SESSION_LOGOUT);
        NavigateToSignInActivity(true);
    }
}
