package com.creative.share.apps.rubbish.ui_financial_manager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.activity_sign_in_sign_up.SignInActivity;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Account_Deactivated;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Contact_Us;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Profile;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Settings;
import com.creative.share.apps.rubbish.general_fragments.Fragment_Terms_Condition;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_financial_manager.fragments.Financial_Fragment_Home;
import com.creative.share.apps.rubbish.ui_financial_manager.fragments.Financial_Fragment_Search;
import com.creative.share.apps.rubbish.ui_financial_manager.fragments.Fragment_Financial_User_Data;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FinancialHomeActivity extends AppCompatActivity {

    private int fragment_count = 0;
    private FragmentManager fragmentManager;
    private Financial_Fragment_Home financial_fragment_home;
    private Fragment_Profile fragment_profile;
    private Financial_Fragment_Search financial_fragment_search;
    private Fragment_Settings fragment_settings;
    private Fragment_Terms_Condition fragment_terms_condition;
    private Fragment_Contact_Us fragment_contact_us;
    private Fragment_Financial_User_Data fragment_financial_user_data;
    private Fragment_Account_Deactivated fragment_account_deactivated;

    ///////////////////////////////////////////
    private View root;
    private ImageView image_close;
    private BottomSheetBehavior behavior;
    private TextView tv_salary, tv_bonus, tv_total;


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

        initView();
    }

    private void initView() {
        tv_total = findViewById(R.id.tv_total);
        tv_salary = findViewById(R.id.tv_salary);
        tv_bonus = findViewById(R.id.tv_bonus);
        image_close = findViewById(R.id.image_close);
        root = findViewById(R.id.root);
        behavior =BottomSheetBehavior.from(root);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_DRAGGING)
                {
                    openSheet();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSheet();
            }
        });


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


    public void updateSalary(double salary,double bonus,double total)
    {
        tv_salary.setText(String.valueOf(salary));
        tv_bonus.setText(String.valueOf(bonus));
        tv_total.setText(String.valueOf(total));
    }

    public void openSheet()

    {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    public void closeSheet()

    {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    public void DisplayFragmentHome() {

        fragment_count += 1;

        if (financial_fragment_home == null) {
            financial_fragment_home = Financial_Fragment_Home.newInstance();
        }

        if (financial_fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(financial_fragment_home).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, financial_fragment_home, "supervisor_fragment_home").addToBackStack("supervisor_fragment_home").commit();
        }

    }

    public void DisplayFragmentProfile() {


        if (financial_fragment_search != null && financial_fragment_search.isAdded()) {
            fragmentManager.beginTransaction().hide(financial_fragment_search).commit();
        }

        if (fragment_profile == null) {
            fragment_profile = Fragment_Profile.newInstance();
        }


        if (fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

        }
        if (financial_fragment_home != null && financial_fragment_home.isAdded()) {
            financial_fragment_home.UpdateAHBottomNavigationPosition(0);
        }

    }

    public void DisplayFragmentSearch() {


        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }
        if (financial_fragment_search == null) {
            financial_fragment_search = Financial_Fragment_Search.newInstance();
        }

        if (financial_fragment_search.isAdded()) {
            fragmentManager.beginTransaction().show(financial_fragment_search).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, financial_fragment_search, "financial_fragment_search").addToBackStack("financial_fragment_search").commit();

        }
        if (financial_fragment_home != null && financial_fragment_home.isAdded()) {
            financial_fragment_home.UpdateAHBottomNavigationPosition(1);
        }

    }

    public void DisplayFragmentUserData(UserModel userModel) {

        fragment_count += 1;

        fragment_financial_user_data = Fragment_Financial_User_Data.newInstance(userModel);


        if (fragment_financial_user_data.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_financial_user_data).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_financial_user_data, "fragment_financial_user_data").addToBackStack("fragment_financial_user_data").commit();

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
        if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
        {
            closeSheet();
        }else
            {
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


    }

    public void Logout() {

        mAuth.signOut();
        preference.create_update_session(this, Tags.SESSION_LOGOUT);
        NavigateToSignInActivity(true);
    }
}
