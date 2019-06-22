package com.creative.share.apps.rubbish.ui_head_manager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import com.creative.share.apps.rubbish.ui_head_manager.fragmens.Fragment_Head_Manager_User_Data;
import com.creative.share.apps.rubbish.ui_head_manager.fragmens.Head_Manager_Fragment_Home;
import com.creative.share.apps.rubbish.ui_head_manager.fragmens.Head_Manager_Fragment_Reports;
import com.creative.share.apps.rubbish.ui_head_manager.fragmens.Head_Manager_Fragment_Search;
import com.google.firebase.auth.FirebaseAuth;

public class HeadMangerHomeActivity extends AppCompatActivity {

    private int fragment_count = 0;
    private FragmentManager fragmentManager;
    private Head_Manager_Fragment_Home head_manager_fragment_home;
    private Fragment_Profile fragment_profile;
    private Head_Manager_Fragment_Search head_manager_fragment_search;
    private Fragment_Settings fragment_settings;
    private Fragment_Terms_Condition fragment_terms_condition;
    private Fragment_Contact_Us fragment_contact_us;
    private Head_Manager_Fragment_Reports head_manager_fragment_reports;
    private Fragment_Head_Manager_User_Data fragment_head_manager_user_data;
    ///////////////////////////////////////////



    private UserModel userModel;
    private Preference preference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_manager_home);
        mAuth = FirebaseAuth.getInstance();
        preference = Preference.newInstance();
        userModel = preference.getUserData(this);
        fragmentManager = this.getSupportFragmentManager();

        if (savedInstanceState == null) {
            DisplayFragmentHome();
            DisplayFragmentProfile();
        }

        initView();
    }

    private void initView() {


    }



    public void DisplayFragmentHome() {

        fragment_count += 1;

        if (head_manager_fragment_home == null) {
            head_manager_fragment_home = Head_Manager_Fragment_Home.newInstance();
        }

        if (head_manager_fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(head_manager_fragment_home).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, head_manager_fragment_home, "head_manager_fragment_home").addToBackStack("head_manager_fragment_home").commit();
        }

    }

    public void DisplayFragmentProfile() {

        if (head_manager_fragment_reports != null && head_manager_fragment_reports.isAdded()) {
            fragmentManager.beginTransaction().hide(head_manager_fragment_reports).commit();
        }

        if (head_manager_fragment_search != null && head_manager_fragment_search.isAdded()) {
            fragmentManager.beginTransaction().hide(head_manager_fragment_search).commit();
        }

        if (fragment_profile == null) {
            fragment_profile = Fragment_Profile.newInstance();
        }


        if (fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

        }
        if (head_manager_fragment_home != null && head_manager_fragment_home.isAdded()) {
            head_manager_fragment_home.UpdateAHBottomNavigationPosition(0);
        }

    }

    public void DisplayFragmentSearch() {


        if (head_manager_fragment_reports != null && head_manager_fragment_reports.isAdded()) {
            fragmentManager.beginTransaction().hide(head_manager_fragment_reports).commit();
        }

        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }
        if (head_manager_fragment_search == null) {
            head_manager_fragment_search = Head_Manager_Fragment_Search.newInstance();
        }

        if (head_manager_fragment_search.isAdded()) {
            fragmentManager.beginTransaction().show(head_manager_fragment_search).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, head_manager_fragment_search, "head_manager_fragment_search").addToBackStack("head_manager_fragment_search").commit();

        }
        if (head_manager_fragment_home != null && head_manager_fragment_home.isAdded()) {
            head_manager_fragment_home.UpdateAHBottomNavigationPosition(1);
        }

    }

    public void DisplayFragmentReports() {


        if (head_manager_fragment_search != null && head_manager_fragment_search.isAdded()) {
            fragmentManager.beginTransaction().hide(head_manager_fragment_search).commit();
        }

        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }
        if (head_manager_fragment_reports == null) {
            head_manager_fragment_reports = Head_Manager_Fragment_Reports.newInstance();
        }

        if (head_manager_fragment_reports.isAdded()) {
            fragmentManager.beginTransaction().show(head_manager_fragment_reports).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, head_manager_fragment_reports, "head_manager_fragment_reports").addToBackStack("head_manager_fragment_reports").commit();

        }
        if (head_manager_fragment_home != null && head_manager_fragment_home.isAdded()) {
            head_manager_fragment_home.UpdateAHBottomNavigationPosition(2);
        }

    }

    public void DisplayFragmentSettings() {

        fragment_count += 1;

        fragment_settings = Fragment_Settings.newInstance();


        if (fragment_settings.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_settings).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_settings, "fragment_settings").addToBackStack("fragment_settings").commit();

        }


    }

    public void DisplayFragmentUserData(UserModel userModel) {

        fragment_count += 1;

        fragment_head_manager_user_data = Fragment_Head_Manager_User_Data.newInstance(userModel);


        if (fragment_head_manager_user_data.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_head_manager_user_data).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_head_manager_user_data, "fragment_head_manager_user_data").addToBackStack("fragment_head_manager_user_data").commit();

        }


    }

    public void DisplayFragmentTerms_AboutUs(int type) {

        fragment_count += 1;

        fragment_terms_condition = Fragment_Terms_Condition.newInstance(type);


        if (fragment_terms_condition.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_terms_condition).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_terms_condition, "fragment_terms_condition").addToBackStack("fragment_terms_condition").commit();

        }


    }

    public void DisplayFragmentContact() {

        fragment_count += 1;

        fragment_contact_us = Fragment_Contact_Us.newInstance();


        if (fragment_contact_us.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_contact_us).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_contact_us, "fragment_contact_us").addToBackStack("fragment_contact_us").commit();

        }


    }

    //from fragment update user data
    public void UpdateEmployeeData(UserModel userModel)
    {
        if (head_manager_fragment_search!=null&&head_manager_fragment_search.isAdded())
        {
            head_manager_fragment_search.updateUserData(userModel);
        }
    }

    public void UserDeletedListener() {
        if (head_manager_fragment_search!=null&&head_manager_fragment_search.isAdded())
        {
            head_manager_fragment_search.deleteUser();
        }
    }

    public void NavigateToSignInActivity(boolean isSignIn) {

        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("sign_up", isSignIn);
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
            if (fragment_profile != null && fragment_profile.isAdded() && !fragment_profile.isVisible()) {
                DisplayFragmentProfile();
            } else {
                if (userModel != null) {
                    finish();
                } else {
                    NavigateToSignInActivity(true);
                }
            }
        }


    }

    public void Logout() {

        mAuth.signOut();
        preference.create_update_session(this, Tags.SESSION_LOGOUT);
        NavigateToSignInActivity(true);
    }


}
