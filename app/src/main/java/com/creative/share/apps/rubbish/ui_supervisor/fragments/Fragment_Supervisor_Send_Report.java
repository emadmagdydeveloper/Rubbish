package com.creative.share.apps.rubbish.ui_supervisor.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.ReportModel;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class Fragment_Supervisor_Send_Report extends Fragment {
    private static final String TAG = "DATA";
    private ImageView image_back;
    private EditText edt_supervisor_name,edt_employee_name,edt_report;
    private Button btn_send;
    private String current_language;
    private SupervisorHomeActivity activity;
    private UserModel employeeModel,supervisorModel;
    private Preference preference;
    private DatabaseReference dRef;


    public static Fragment_Supervisor_Send_Report newInstance(UserModel employeeModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,employeeModel);

        Fragment_Supervisor_Send_Report fragment_supervisor_send_report = new Fragment_Supervisor_Send_Report();
        fragment_supervisor_send_report.setArguments(bundle);
        return fragment_supervisor_send_report ;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_supervisor_send_reports,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        dRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        preference =Preference.newInstance();
        activity = (SupervisorHomeActivity) getActivity();
        supervisorModel = preference.getUserData(activity);

        current_language = Locale.getDefault().getLanguage();
        image_back = view.findViewById(R.id.image_back);

        if (current_language.equals("ar"))
        {
           image_back.setRotation(180.0f);
        }

        image_back = view.findViewById(R.id.image_back);
        edt_supervisor_name = view.findViewById(R.id.edt_supervisor_name);
        edt_employee_name = view.findViewById(R.id.edt_employee_name);
        edt_report = view.findViewById(R.id.edt_report);
        btn_send = view.findViewById(R.id.btn_send);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            employeeModel = (UserModel) bundle.getSerializable(TAG);
            updateUI(employeeModel);


        }

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });





    }

    private void updateUI(UserModel employeeModel) {

        edt_supervisor_name.setText(supervisorModel.getName());
        edt_employee_name.setText(employeeModel.getName());
    }

    private void CheckData() {
        String m_supervisor_name = edt_supervisor_name.getText().toString().trim();
        String m_employee_name = edt_employee_name.getText().toString().trim();
        String m_report = edt_report.getText().toString().trim();


        if (!TextUtils.isEmpty(m_supervisor_name)&&
                !TextUtils.isEmpty(m_employee_name)&&
                !TextUtils.isEmpty(m_report)
        )
        {
            edt_employee_name.setError(null);
            edt_supervisor_name.setError(null);
            edt_report.setError(null);
            Common.CloseKeyBoard(activity,edt_supervisor_name);
            Send(m_supervisor_name,m_employee_name,m_report);
        }else
            {
                if (TextUtils.isEmpty(m_supervisor_name))
                {
                    edt_supervisor_name.setError(getString(R.string.field_req));
                }else
                    {
                        edt_supervisor_name.setError(null);

                    }

                if (TextUtils.isEmpty(m_employee_name))
                {
                    edt_employee_name.setError(getString(R.string.field_req));
                }else
                {
                    edt_employee_name.setError(null);

                }


                if (TextUtils.isEmpty(m_report))
                {
                    edt_report.setError(getString(R.string.field_req));
                }else
                {
                    edt_report.setError(null);

                }

            }
    }

    private void Send(String m_supervisor_name, String m_employee_name, String m_report) {


        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        String report_id = dRef.push().getKey();

        Calendar calendar = Calendar.getInstance();

        ReportModel reportModel = new ReportModel(report_id,supervisorModel.getUser_id(),m_supervisor_name,employeeModel.getUser_id(),m_employee_name,m_report,calendar.getTimeInMillis());

        dRef.child(report_id)
                .setValue(reportModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            activity.Back();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}
