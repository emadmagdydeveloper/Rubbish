package com.creative.share.apps.rubbish.ui_supervisor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Locale;


public class Fragment_Supervisor_User_Data extends Fragment {
    private static final String TAG ="DATA";
    private ImageView image,arrow;
    private LinearLayout ll_back;
    private TextView tv_name,tv_email,tv_phone,tv_address;
    private Button btn_report;
    private SupervisorHomeActivity activity;
    private UserModel userModel;
    private String current_language;
    private AppBarLayout app_bar;
    public static Fragment_Supervisor_User_Data newInstance(UserModel userModel) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,userModel);
        Fragment_Supervisor_User_Data fragment_supervisor_user_data = new Fragment_Supervisor_User_Data();;
        fragment_supervisor_user_data.setArguments(bundle);
        return fragment_supervisor_user_data;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supervisor_user_data, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (SupervisorHomeActivity) getActivity();
        current_language = Locale.getDefault().getLanguage();

        arrow = view.findViewById(R.id.arrow);
        if (current_language.equals("ar"))
        {
            arrow.setRotation(180.0f);
        }

        app_bar = view.findViewById(R.id.app_bar);
        btn_report = view.findViewById(R.id.btn_report);
        image = view.findViewById(R.id.image);
        ll_back = view.findViewById(R.id.ll_back);

        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_address = view.findViewById(R.id.tv_address);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            userModel = (UserModel) bundle.getSerializable(TAG);
            updateUi(userModel);

        }




        app_bar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int total_range = appBarLayout.getTotalScrollRange();
                if ((total_range+i)<60)
                {
                    tv_name.setVisibility(View.GONE);
                    image.setVisibility(View.GONE);
                }else
                    {
                        tv_name.setVisibility(View.VISIBLE);
                        image.setVisibility(View.VISIBLE);


                    }
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentSendReport(userModel);
            }
        });
    }

    private void updateUi(UserModel userModel) {

        if (userModel!=null)
        {
            tv_name.setText(userModel.getName());
            tv_email.setText(userModel.getEmail());
            tv_address.setText(userModel.getAddress());
            tv_phone.setText(userModel.getPhone());

        }
    }

}
