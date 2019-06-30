package com.creative.share.apps.rubbish.activity_sign_in_sign_up.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.activity_sign_in_sign_up.SignInActivity;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.creative.share.apps.rubbish.ui_employee.EmployeeHomeActivity;
import com.creative.share.apps.rubbish.ui_financial_manager.FinancialHomeActivity;
import com.creative.share.apps.rubbish.ui_head_manager.HeadMangerHomeActivity;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_Login extends Fragment {
    private Button btn_sign_in,btn_sign_up;
    private TextView tv_skip,tv_forget_password;
    private EditText edt_email, edt_password;
    private SignInActivity activity;
    private Preference preferences;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;


    public static Fragment_Login newInstance() {

        return new Fragment_Login();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initView(view);
        return view;
    }

    private void initView(final View view) {
        preferences = Preference.newInstance();
        mAuth = FirebaseAuth.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference().child("Users");

        activity = (SignInActivity) getActivity();
        btn_sign_in = view.findViewById(R.id.btn_sign_in);
        btn_sign_up = view.findViewById(R.id.btn_sign_up);
        tv_skip = view.findViewById(R.id.tv_skip);
        tv_forget_password = view.findViewById(R.id.tv_forget_password);

        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentSignUp();
            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.NavigateToHomeActivity(0);
            }
        });

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentForgetPassword();
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
    }

    private void checkData() {
        String m_email = edt_email.getText().toString().trim();
        String m_password = edt_password.getText().toString().trim();

        if (!TextUtils.isEmpty(m_email) &&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches()&&
                !TextUtils.isEmpty(m_password)
        ) {
            edt_email.setError(null);
            edt_password.setError(null);
            Common.CloseKeyBoard(activity, edt_email);
            Login(m_email, m_password);
        } else {


            if (TextUtils.isEmpty(m_email)) {
                edt_email.setError(getString(R.string.field_req));

            }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches()){
                edt_email.setError(getString(R.string.inv_email));
            }else {
                edt_email.setError(null);

            }


            if (TextUtils.isEmpty(m_password)) {
                edt_password.setError(getString(R.string.field_req));
            } else {
                edt_password.setError(null);

            }
        }
    }

    private void Login(String m_email, String m_password) {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        mAuth.signInWithEmailAndPassword(m_email, m_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            String user_id = task.getResult().getUser().getUid();
                            getUserData(user_id,dialog);
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

    private void getUserData(String user_id, final ProgressDialog dialog) {

        dRef.child(user_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null)
                        {

                            dialog.dismiss();


                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            preferences.create_update_user_data(activity,userModel);

                            if (userModel.getUser_type() == Tags.USER_TYPE_CLIENT)
                            {
                                Intent intent = new Intent(activity, ClientHomeActivity.class);
                                activity.startActivity(intent);
                                activity.finish();
                            }else if (userModel.getUser_type() == Tags.USER_TYPE_EMPLOYEE)
                            {
                                Intent intent = new Intent(activity, EmployeeHomeActivity.class);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                            else if (userModel.getUser_type() == Tags.USER_TYPE_SUPERVISOR)
                            {
                                Intent intent = new Intent(activity, SupervisorHomeActivity.class);
                                activity.startActivity(intent);
                                activity.finish();
                            }

                            else if (userModel.getUser_type() == Tags.USER_TYPE_FINANCIAL_MANAGER)
                            {
                                Intent intent = new Intent(activity, FinancialHomeActivity.class);
                                startActivity(intent);
                                activity.finish();
                            }
                            else if (userModel.getUser_type() == Tags.USER_TYPE_HEAD_MANAGER)
                            {
                                Intent intent = new Intent(activity, HeadMangerHomeActivity.class);
                                startActivity(intent);
                                activity.finish();
                            }

                        }else
                            {
                                dialog.dismiss();
                                Toast.makeText(activity, "No users founded", Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


}
