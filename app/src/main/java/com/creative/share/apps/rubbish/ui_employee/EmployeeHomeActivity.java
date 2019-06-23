package com.creative.share.apps.rubbish.ui_employee;

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
import com.creative.share.apps.rubbish.models.OrderModel;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_employee.fragments.Employee_Fragment_Home;
import com.creative.share.apps.rubbish.ui_employee.fragments.Employee_Fragment_Orders;
import com.creative.share.apps.rubbish.ui_employee.fragments.Fragment_Employee_Order_Details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeHomeActivity extends AppCompatActivity {

    private int fragment_count = 0;
    private FragmentManager fragmentManager;
    private Employee_Fragment_Home employee_fragment_home;
    private Fragment_Profile fragment_profile;
    private Employee_Fragment_Orders employee_fragment_orders;
    private Fragment_Settings fragment_settings;
    private Fragment_Terms_Condition fragment_terms_condition;
    private Fragment_Contact_Us fragment_contact_us;
    private Fragment_Employee_Order_Details fragment_employee_order_details;
    private Fragment_Account_Deactivated fragment_account_deactivated;
    private Fragment_Edit_Profile fragment_edit_profile;


    private UserModel userModel;
    private Preference preference;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
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

        if (employee_fragment_home == null) {
            employee_fragment_home = Employee_Fragment_Home.newInstance();
        }

        if (employee_fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(employee_fragment_home).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, employee_fragment_home, "employee_fragment_home").addToBackStack("employee_fragment_home").commit();
        }

    }

    public void DisplayFragmentProfile()
    {

        if (employee_fragment_orders != null && employee_fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(employee_fragment_orders).commit();
        }

        if (fragment_profile == null) {
            fragment_profile = Fragment_Profile.newInstance();
        }

        if (fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

        }
        if (employee_fragment_home != null && employee_fragment_home.isAdded()) {
            employee_fragment_home.UpdateAHBottomNavigationPosition(0);
        }

    }

    public void DisplayFragmentOrder()
    {

        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }

        if (employee_fragment_orders == null) {
            employee_fragment_orders = Employee_Fragment_Orders.newInstance();
        }

        if (employee_fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().show(employee_fragment_orders).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, employee_fragment_orders, "employee_fragment_orders").addToBackStack("employee_fragment_orders").commit();

        }
        if (employee_fragment_home != null && employee_fragment_home.isAdded()) {
            employee_fragment_home.UpdateAHBottomNavigationPosition(1);
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

    public void DisplayFragmentOrderDetails(OrderModel orderModel)
    {

        fragment_count +=1;

        fragment_employee_order_details = Fragment_Employee_Order_Details.newInstance(orderModel);


        if (fragment_employee_order_details.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_employee_order_details).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_employee_order_details, "fragment_employee_order_details").addToBackStack("fragment_employee_order_details").commit();

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
            if (fragment_account_deactivated!=null&&fragment_account_deactivated.isAdded()&&fragment_account_deactivated.isVisible())
            {

            }else
                {
                    super.onBackPressed();
                    fragment_count -= 1;
                }

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
        preference.create_update_session(this, Tags.SESSION_LOGOUT);
        NavigateToSignInActivity(true);
    }
}
