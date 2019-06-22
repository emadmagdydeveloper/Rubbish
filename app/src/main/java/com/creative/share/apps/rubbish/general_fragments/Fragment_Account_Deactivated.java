package com.creative.share.apps.rubbish.general_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_employee.EmployeeHomeActivity;
import com.creative.share.apps.rubbish.ui_financial_manager.FinancialHomeActivity;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment_Account_Deactivated extends Fragment {

    private Button btn_delete;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;
    private Preference preference;
    private UserModel userModel;
    private AppCompatActivity activity;


    public static Fragment_Account_Deactivated newInstance() {
        return new Fragment_Account_Deactivated();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account_deactivated, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mAuth = FirebaseAuth.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference();
        activity = (AppCompatActivity) getActivity();
        preference = Preference.newInstance();
        userModel = preference.getUserData(activity);
        btn_delete = view.findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccount();
            }
        });

    }

    private void DeleteAccount() {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        DeleteData(dialog);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    dialog.dismiss();
                }
            });
        }
    }

    private void DeleteData(final ProgressDialog dialog) {
        dRef.child("Users").child(userModel.getUser_id())
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        dRef.child("Deleted_Users")
                                .child(userModel.getUser_id())
                                .removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        dialog.dismiss();

                                        if (activity instanceof EmployeeHomeActivity) {
                                            EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
                                            preference.create_update_session(employeeHomeActivity, Tags.SESSION_LOGOUT);
                                            employeeHomeActivity.NavigateToSignInActivity(true);
                                        }else if (activity instanceof SupervisorHomeActivity)
                                        {
                                            SupervisorHomeActivity supervisorHomeActivity = (SupervisorHomeActivity) activity;
                                            preference.create_update_session(supervisorHomeActivity, Tags.SESSION_LOGOUT);
                                            supervisorHomeActivity.NavigateToSignInActivity(true);
                                        }
                                        else if (activity instanceof FinancialHomeActivity)
                                        {
                                            FinancialHomeActivity financialHomeActivity = (FinancialHomeActivity) activity;
                                            preference.create_update_session(financialHomeActivity, Tags.SESSION_LOGOUT);
                                            financialHomeActivity.NavigateToSignInActivity(true);
                                        }

                                    }
                                });
                    }
                });
    }
}
