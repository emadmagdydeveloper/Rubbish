package com.creative.share.apps.rubbish.general_fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.creative.share.apps.rubbish.ui_employee.EmployeeHomeActivity;
import com.creative.share.apps.rubbish.ui_financial_manager.FinancialHomeActivity;
import com.creative.share.apps.rubbish.ui_head_manager.HeadMangerHomeActivity;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;

import java.util.Locale;


public class Fragment_Settings extends Fragment {
    private AppCompatActivity activity;
    private LinearLayout ll_back;
    private ConstraintLayout cons_terms, cons_rate, cons_about, cons_contact,cons_edit;
    private ImageView arrow1, arrow2, arrow3, arrow4,arrow5;
    private String current_language;
    private View view1;
    private Preference preference;
    private UserModel userModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        return view;
    }

    public static Fragment_Settings newInstance() {
        return new Fragment_Settings();
    }

    private void initView(View view) {
        activity = (AppCompatActivity) getActivity();
        preference = Preference.newInstance();
        userModel = preference.getUserData(activity);
        current_language = Locale.getDefault().getLanguage();

        ll_back = view.findViewById(R.id.ll_back);

        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);
        arrow5 = view.findViewById(R.id.arrow5);


        if (!current_language.equals("ar")) {

            arrow1.setRotation(180.0f);
            arrow2.setRotation(180.0f);
            arrow3.setRotation(180.0f);
            arrow4.setRotation(180.0f);
            arrow5.setRotation(180.0f);


        }



        view1 = view.findViewById(R.id.view);

        cons_terms = view.findViewById(R.id.cons_terms);
        cons_rate = view.findViewById(R.id.cons_rate);
        cons_about = view.findViewById(R.id.cons_about);
        cons_contact = view.findViewById(R.id.cons_contact);
        cons_edit = view.findViewById(R.id.cons_edit);


        cons_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));
                }
            }
        });

        cons_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof ClientHomeActivity)
                {
                    ClientHomeActivity clientHomeActivity = (ClientHomeActivity) activity;
                    clientHomeActivity.DisplayFragmentTerms_AboutUs(1);

                }else if (activity instanceof EmployeeHomeActivity)
                {
                    EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
                    employeeHomeActivity.DisplayFragmentTerms_AboutUs(1);

                }
                else if (activity instanceof SupervisorHomeActivity)
                {
                    SupervisorHomeActivity supervisorHomeActivity = (SupervisorHomeActivity) activity;
                    supervisorHomeActivity.DisplayFragmentTerms_AboutUs(1);

                }

                else if (activity instanceof FinancialHomeActivity)
                {
                    FinancialHomeActivity financialHomeActivity = (FinancialHomeActivity) activity;
                    financialHomeActivity.DisplayFragmentTerms_AboutUs(1);

                }
                else if (activity instanceof HeadMangerHomeActivity)
                {
                    HeadMangerHomeActivity headMangerHomeActivity = (HeadMangerHomeActivity) activity;
                    headMangerHomeActivity.DisplayFragmentTerms_AboutUs(1);

                }
            }

        });

        cons_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof ClientHomeActivity)
                {
                    ClientHomeActivity clientHomeActivity = (ClientHomeActivity) activity;
                    clientHomeActivity.DisplayFragmentContact();

                }else if (activity instanceof EmployeeHomeActivity)
                {
                    EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
                    employeeHomeActivity.DisplayFragmentContact();


                }else if (activity instanceof SupervisorHomeActivity)
                {
                    SupervisorHomeActivity supervisorHomeActivity = (SupervisorHomeActivity) activity;
                    supervisorHomeActivity.DisplayFragmentContact();

                }
                else if (activity instanceof FinancialHomeActivity)
                {
                    FinancialHomeActivity financialHomeActivity = (FinancialHomeActivity) activity;
                    financialHomeActivity.DisplayFragmentContact();

                } else if (activity instanceof HeadMangerHomeActivity)
                {
                    HeadMangerHomeActivity headMangerHomeActivity = (HeadMangerHomeActivity) activity;
                    headMangerHomeActivity.DisplayFragmentContact();

                }

            }
        });

        cons_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof ClientHomeActivity)
                {
                    ClientHomeActivity clientHomeActivity = (ClientHomeActivity) activity;
                    clientHomeActivity.DisplayFragmentTerms_AboutUs(2);

                }else if (activity instanceof EmployeeHomeActivity)
                {
                    EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
                    employeeHomeActivity.DisplayFragmentTerms_AboutUs(2);


                }else if (activity instanceof SupervisorHomeActivity)
                {
                    SupervisorHomeActivity supervisorHomeActivity = (SupervisorHomeActivity) activity;
                    supervisorHomeActivity.DisplayFragmentTerms_AboutUs(2);

                }
                else if (activity instanceof FinancialHomeActivity)
                {
                    FinancialHomeActivity financialHomeActivity = (FinancialHomeActivity) activity;
                    financialHomeActivity.DisplayFragmentTerms_AboutUs(2);

                }
                else if (activity instanceof HeadMangerHomeActivity)
                {
                    HeadMangerHomeActivity headMangerHomeActivity = (HeadMangerHomeActivity) activity;
                    headMangerHomeActivity.DisplayFragmentTerms_AboutUs(2);

                }

            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof ClientHomeActivity)
                {
                    ClientHomeActivity clientHomeActivity = (ClientHomeActivity) activity;
                    clientHomeActivity.Back();

                }else if (activity instanceof EmployeeHomeActivity)
                {
                    EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
                    employeeHomeActivity.Back();


                }else if (activity instanceof SupervisorHomeActivity)
                {
                    SupervisorHomeActivity supervisorHomeActivity = (SupervisorHomeActivity) activity;
                    supervisorHomeActivity.Back();

                }
                else if (activity instanceof FinancialHomeActivity)
                {
                    FinancialHomeActivity financialHomeActivity = (FinancialHomeActivity) activity;
                    financialHomeActivity.Back();
                }
                else if (activity instanceof HeadMangerHomeActivity)
                {
                    HeadMangerHomeActivity headMangerHomeActivity = (HeadMangerHomeActivity) activity;
                    headMangerHomeActivity.Back();

                }
            }
        });


        cons_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof ClientHomeActivity)
                {
                    ClientHomeActivity clientHomeActivity = (ClientHomeActivity) activity;
                    clientHomeActivity.DisplayFragmentEditProfile();

                }else if (activity instanceof EmployeeHomeActivity)
                {
                    EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
                    employeeHomeActivity.DisplayFragmentEditProfile();

                }
                else if (activity instanceof SupervisorHomeActivity)
                {
                    SupervisorHomeActivity supervisorHomeActivity = (SupervisorHomeActivity) activity;
                    supervisorHomeActivity.DisplayFragmentEditProfile();

                }

                else if (activity instanceof FinancialHomeActivity)
                {
                    FinancialHomeActivity financialHomeActivity = (FinancialHomeActivity) activity;
                    financialHomeActivity.DisplayFragmentEditProfile();

                }
                else if (activity instanceof HeadMangerHomeActivity)
                {
                    HeadMangerHomeActivity headMangerHomeActivity = (HeadMangerHomeActivity) activity;
                    headMangerHomeActivity.DisplayFragmentEditProfile();

                }
            }
        });

        arrow5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof ClientHomeActivity)
                {
                    ClientHomeActivity clientHomeActivity = (ClientHomeActivity) activity;
                    clientHomeActivity.Back();

                }else if (activity instanceof EmployeeHomeActivity)
                {
                    EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
                    employeeHomeActivity.Back();

                }
                else if (activity instanceof SupervisorHomeActivity)
                {
                    SupervisorHomeActivity supervisorHomeActivity = (SupervisorHomeActivity) activity;
                    supervisorHomeActivity.Back();

                }

                else if (activity instanceof FinancialHomeActivity)
                {
                    FinancialHomeActivity financialHomeActivity = (FinancialHomeActivity) activity;
                    financialHomeActivity.Back();

                }
                else if (activity instanceof HeadMangerHomeActivity)
                {
                    HeadMangerHomeActivity headMangerHomeActivity = (HeadMangerHomeActivity) activity;
                    headMangerHomeActivity.Back();

                }
            }
        });

        updateUI();

    }

    private void updateUI() {
        if (userModel.getUser_type() == Tags.USER_TYPE_CLIENT||userModel.getUser_type() == Tags.USER_TYPE_EMPLOYEE)
        {
            cons_contact.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
        }else
            {
                cons_contact.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
            }
    }


}
