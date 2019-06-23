package com.creative.share.apps.rubbish.general_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.creative.share.apps.rubbish.ui_employee.EmployeeHomeActivity;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.ui_financial_manager.FinancialHomeActivity;
import com.creative.share.apps.rubbish.ui_head_manager.HeadMangerHomeActivity;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.util.Locale;


public class Fragment_Profile extends Fragment {
    private ImageView image,image_setting,image_cover;
    private TextView tv_name,tv_email,tv_phone,tv_address;
    private LinearLayout ll_logout;
    private AppCompatActivity activity;
    private Preference preferences;
    private UserModel userModel;
    private String current_language;
    private AppBarLayout app_bar;
    public static Fragment_Profile newInstance() {

        return new Fragment_Profile();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (AppCompatActivity) getActivity();
        current_language = Locale.getDefault().getLanguage();
        preferences = Preference.newInstance();
        userModel = preferences.getUserData(activity);




        app_bar = view.findViewById(R.id.app_bar);

        image_setting = view.findViewById(R.id.image_setting);

        image_cover = view.findViewById(R.id.image_cover);

        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_address = view.findViewById(R.id.tv_address);
        ll_logout = view.findViewById(R.id.ll_logout);



        image_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof ClientHomeActivity )
                {
                    ClientHomeActivity clientHomeActivity = (ClientHomeActivity) activity;
                    clientHomeActivity.DisplayFragmentSettings();



                }else if (activity instanceof EmployeeHomeActivity)
                {
                    EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
                    employeeHomeActivity.DisplayFragmentSettings();
                }else if (activity instanceof SupervisorHomeActivity)
                {
                    SupervisorHomeActivity supervisorHomeActivity = (SupervisorHomeActivity) activity;
                    supervisorHomeActivity.DisplayFragmentSettings();

                }
                else if (activity instanceof FinancialHomeActivity)
                {
                    FinancialHomeActivity financialHomeActivity = (FinancialHomeActivity) activity;
                    financialHomeActivity.DisplayFragmentSettings();

                }
                else if (activity instanceof HeadMangerHomeActivity)
                {
                    HeadMangerHomeActivity headMangerHomeActivity = (HeadMangerHomeActivity) activity;
                    headMangerHomeActivity.DisplayFragmentSettings();

                }
            }
        });
        updateUi(userModel);




        app_bar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int total_range = appBarLayout.getTotalScrollRange();
                if ((total_range+i)<60)
                {
                    tv_name.setVisibility(View.GONE);
                    image.setVisibility(View.GONE);
                    image_setting.setVisibility(View.GONE);
                }else
                    {
                        tv_name.setVisibility(View.VISIBLE);
                        image.setVisibility(View.VISIBLE);
                        image_setting.setVisibility(View.VISIBLE);


                    }
            }
        });

        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity instanceof ClientHomeActivity )
                {
                    ClientHomeActivity clientHomeActivity = (ClientHomeActivity) activity;
                    clientHomeActivity.Logout();



                }else if (activity instanceof EmployeeHomeActivity)
                {
                    EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
                    employeeHomeActivity.Logout();
                }else if (activity instanceof SupervisorHomeActivity)
                {
                    SupervisorHomeActivity supervisorHomeActivity = (SupervisorHomeActivity) activity;
                    supervisorHomeActivity.Logout();

                }
                else if (activity instanceof FinancialHomeActivity)
                {
                    FinancialHomeActivity financialHomeActivity = (FinancialHomeActivity) activity;
                    financialHomeActivity.Logout();

                }
                else if (activity instanceof HeadMangerHomeActivity)
                {
                    HeadMangerHomeActivity headMangerHomeActivity = (HeadMangerHomeActivity) activity;
                    headMangerHomeActivity.Logout();

                }
            }
        });

    }

    private void updateUi(UserModel userModel) {

        if (userModel!=null)
        {
            tv_name.setText(userModel.getName());
            tv_email.setText(userModel.getEmail());
            tv_address.setText(userModel.getAddress());
            tv_phone.setText(userModel.getPhone_code()+userModel.getPhone());


            if (userModel.getUser_type() == Tags.USER_TYPE_CLIENT)
            {
                Picasso.with(activity).load(R.drawable.user_cover).fit().into(image_cover);
                Picasso.with(activity).load(R.drawable.employee_user_image).fit().into(image);

            }else if (userModel.getUser_type() == Tags.USER_TYPE_EMPLOYEE)
            {
                Picasso.with(activity).load(R.drawable.employee_cover).fit().into(image_cover);
                Picasso.with(activity).load(R.drawable.employee_user_image).fit().into(image);

            }
            else if (userModel.getUser_type() == Tags.USER_TYPE_SUPERVISOR)
            {
                Picasso.with(activity).load(R.drawable.supervisor_cover).fit().into(image_cover);
                Picasso.with(activity).load(R.drawable.supervisor_image).fit().into(image);

            }
            else if (userModel.getUser_type() == Tags.USER_TYPE_FINANCIAL_MANAGER)
            {
                Picasso.with(activity).load(R.drawable.financial_cover).fit().into(image_cover);
                Picasso.with(activity).load(R.drawable.financial_image).fit().into(image);

            }
            else if (userModel.getUser_type() == Tags.USER_TYPE_HEAD_MANAGER)
            {
                Picasso.with(activity).load(R.drawable.head_cover).fit().into(image_cover);
                Picasso.with(activity).load(R.drawable.head_image).fit().into(image);

            }

        }
    }

    public void updateUserData(UserModel userModel) {
        updateUi(userModel);
    }
}
