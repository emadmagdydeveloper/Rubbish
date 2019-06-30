package com.creative.share.apps.rubbish.activity_sign_in_sign_up.fragments;


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
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.activity_sign_in_sign_up.SignInActivity;
import com.creative.share.apps.rubbish.share.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;


public class Fragment_Forget_Password extends Fragment {


    private ImageView image_back;
    private EditText edt_email;
    private Button btn_reset;

    private SignInActivity activity;
    private String current_language;
    private FirebaseAuth mAuth;


    public static Fragment_Forget_Password newInstance() {
        return new Fragment_Forget_Password();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mAuth = FirebaseAuth.getInstance();

        activity = (SignInActivity) getActivity();
        current_language = Locale.getDefault().getLanguage();

        image_back = view.findViewById(R.id.image_back);

        if (current_language.equals("ar")) {
            image_back.setRotation(180.0f);
        }

        edt_email = view.findViewById(R.id.edt_email);

        btn_reset = view.findViewById(R.id.btn_reset);


        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });


        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });


    }


    private void checkData() {

        String m_email = edt_email.getText().toString().trim();

        if (!TextUtils.isEmpty(m_email) &&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches()


        ) {
            Common.CloseKeyBoard(activity, edt_email);
            edt_email.setError(null);
            resetPassword(m_email);


        } else {


            if (TextUtils.isEmpty(m_email)) {
                edt_email.setError(getString(R.string.field_req));
            } else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches()) {
                edt_email.setError(getString(R.string.inv_email));
            } else {
                edt_email.setError(null);

            }


        }

    }

    private void resetPassword(String m_email) {
        final ProgressDialog dialog =Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        mAuth.sendPasswordResetEmail(m_email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
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
