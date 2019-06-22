package com.creative.share.apps.rubbish.general_fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.creative.share.apps.rubbish.models.Terms_About_Model;
import com.creative.share.apps.rubbish.ui_employee.EmployeeHomeActivity;
import com.creative.share.apps.rubbish.ui_financial_manager.FinancialHomeActivity;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Fragment_Terms_Condition extends Fragment {
    private static final String TAG="type";
    private TextView tv_title;
    private ImageView arrow;
    private TextView tv_content;
    private ProgressBar progBar;
    private String current_language;
    private AppCompatActivity activity;
    private DatabaseReference dRef;




    public static Fragment_Terms_Condition newInstance(int type)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG,type);
        Fragment_Terms_Condition fragment_terms_condition = new Fragment_Terms_Condition();
        fragment_terms_condition.setArguments(bundle);
        return fragment_terms_condition;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms_conditions,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        dRef = FirebaseDatabase.getInstance().getReference();
        activity = (AppCompatActivity) getActivity();
        current_language = Locale.getDefault().getLanguage();
        arrow = view.findViewById(R.id.arrow);

        if (current_language.equals("ar"))
        {
            arrow.setRotation(180.0f);
        }
        tv_title = view.findViewById(R.id.tv_title);

        tv_content = view.findViewById(R.id.tv_content);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ActivityCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        arrow.setOnClickListener(new View.OnClickListener() {
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

            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            int type = bundle.getInt(TAG);
            if (type == 1)
            {
                tv_title.setText(getString(R.string.terms_of_service));
                getTermsCondition();

            }else if (type == 2)
            {
                tv_title.setText(getString(R.string.about_tour));

                getAboutUs();
            }
        }
    }

    private void updateUI(Terms_About_Model terms_about_model) {

        if (current_language.equals("ar"))
        {
            tv_content.setText(terms_about_model.getAr_content());
        }else
            {
                tv_content.setText(terms_about_model.getEn_content());

            }
    }
    private void getTermsCondition()
    {


        dRef.child("terms_conditions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null)
                {
                    progBar.setVisibility(View.GONE);
                    Terms_About_Model terms_about_model = dataSnapshot.getValue(Terms_About_Model.class);
                    updateUI(terms_about_model);
                }else
                    {
                        progBar.setVisibility(View.GONE);

                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    private void getAboutUs()
    {

        dRef.child("about_tour").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null)
                {
                    progBar.setVisibility(View.GONE);

                    Terms_About_Model terms_about_model = dataSnapshot.getValue(Terms_About_Model.class);
                    updateUI(terms_about_model);
                }else
                    {
                        progBar.setVisibility(View.GONE);

                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
