package com.creative.share.apps.rubbish.general_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.ContactModel;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.creative.share.apps.rubbish.ui_employee.EmployeeHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class Fragment_Contact_Us extends Fragment {
    private ImageView image_back;
    private EditText edt_name,edt_email,edt_msg;
    private Button btn_send;
    private String current_language;
    private AppCompatActivity activity;
    private Preference preferences;
    private UserModel userModel;
    private DatabaseReference dRef;


    public static Fragment_Contact_Us newInstance()
    {
        return new Fragment_Contact_Us() ;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_us,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        dRef = FirebaseDatabase.getInstance().getReference().child("Supervisor_Messages");

        activity = (AppCompatActivity) getActivity();
        preferences = Preference.newInstance();
        userModel = preferences.getUserData(activity);
        current_language = Locale.getDefault().getLanguage();
        image_back = view.findViewById(R.id.image_back);

        if (current_language.equals("ar"))
        {
           image_back.setRotation(180.0f);
        }

        image_back = view.findViewById(R.id.image_back);
        edt_name = view.findViewById(R.id.edt_name);
        edt_email = view.findViewById(R.id.edt_email);
        edt_msg = view.findViewById(R.id.edt_msg);
        btn_send = view.findViewById(R.id.btn_send);

        updateUI();

        image_back.setOnClickListener(new View.OnClickListener() {
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


            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });





    }

    private void updateUI() {

        if (userModel!=null)
        {
            edt_name.setText(userModel.getName());
            edt_email.setText(userModel.getEmail());

        }
    }

    private void CheckData() {
        String m_name = edt_name.getText().toString().trim();
        String m_email = edt_email.getText().toString().trim();
        String m_msg = edt_msg.getText().toString().trim();


        if (!TextUtils.isEmpty(m_name)&&
                !TextUtils.isEmpty(m_email)&&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches()&&
                !TextUtils.isEmpty(m_msg)
        )
        {
            edt_name.setError(null);
            edt_email.setError(null);
            edt_msg.setError(null);
            Common.CloseKeyBoard(activity,edt_name);
            Send(m_name,m_email,m_msg);
        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(getString(R.string.field_req));
                }else
                    {
                        edt_name.setError(null);

                    }

                if (TextUtils.isEmpty(m_email))
                {
                    edt_email.setError(getString(R.string.field_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError(getString(R.string.inv_email));

                }

                else
                {
                    edt_email.setError(null);

                }

                if (TextUtils.isEmpty(m_msg))
                {
                    edt_msg.setError(getString(R.string.field_req));
                }else
                {
                    edt_msg.setError(null);

                }

            }
    }

    private void Send(String m_name, String m_email, String m_msg) {


        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        Calendar calendar = Calendar.getInstance();

        ContactModel contactModel = new ContactModel(userModel.getUser_id(),m_name,m_email,userModel.getPhone(),m_msg,calendar.getTimeInMillis());
        dRef.push().setValue(contactModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                            if (activity instanceof ClientHomeActivity)
                            {
                                ClientHomeActivity clientHomeActivity = (ClientHomeActivity) activity;
                                clientHomeActivity.Back();

                            }else if (activity instanceof EmployeeHomeActivity)
                            {
                                EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
                                employeeHomeActivity.Back();


                            }
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
