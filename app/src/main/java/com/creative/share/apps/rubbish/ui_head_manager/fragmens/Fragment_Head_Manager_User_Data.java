package com.creative.share.apps.rubbish.ui_head_manager.fragmens;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_head_manager.HeadMangerHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class Fragment_Head_Manager_User_Data extends Fragment {
    private static final String TAG ="DATA";
    private ImageView image,arrow,image_salary_edit,image_bonus_edit,image_job_edit;
    private LinearLayout ll_back;
    private EditText edt_salary,edt_bonus;
    private Button btn_update_salary,btn_update_bonus,btn_update_job,btn_delete;
    private TextView tv_name,tv_email,tv_phone,tv_address,tv_job;
    private HeadMangerHomeActivity activity;
    private UserModel userModel;
    private String current_language;
    private AppBarLayout app_bar;
    private List<String> jobList;
    private int selected_job=0;
    private DatabaseReference dRef;
    private FirebaseAuth mAuth;

    public static Fragment_Head_Manager_User_Data newInstance(UserModel userModel) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,userModel);
        Fragment_Head_Manager_User_Data fragment_supervisor_user_data = new Fragment_Head_Manager_User_Data();;
        fragment_supervisor_user_data.setArguments(bundle);
        return fragment_supervisor_user_data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_data, container, false);
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        mAuth = FirebaseAuth.getInstance();

        dRef = FirebaseDatabase.getInstance().getReference();
        activity = (HeadMangerHomeActivity) getActivity();
        jobList = Arrays.asList(getResources().getStringArray(R.array.job_array));

        current_language = Locale.getDefault().getLanguage();

        arrow = view.findViewById(R.id.arrow);
        if (current_language.equals("ar"))
        {
            arrow.setRotation(180.0f);
        }

        app_bar = view.findViewById(R.id.app_bar);
        image = view.findViewById(R.id.image);
        ll_back = view.findViewById(R.id.ll_back);


        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_address = view.findViewById(R.id.tv_address);

        edt_salary = view.findViewById(R.id.edt_salary);
        btn_update_salary = view.findViewById(R.id.btn_update_salary);
        image_salary_edit = view.findViewById(R.id.image_salary_edit);

        edt_bonus = view.findViewById(R.id.edt_bonus);
        btn_update_bonus = view.findViewById(R.id.btn_update_bonus);
        image_bonus_edit = view.findViewById(R.id.image_bonus_edit);


        tv_job = view.findViewById(R.id.tv_job);
        btn_update_job = view.findViewById(R.id.btn_update_job);
        image_job_edit = view.findViewById(R.id.image_job_edit);

        btn_delete = view.findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDeleteDialog();
            }
        });

        edt_salary.setEnabled(false);
        edt_bonus.setEnabled(false);

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
        image_salary_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_salary.setEnabled(true);
                edt_salary.requestFocus();
                btn_update_salary.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt_salary,InputMethodManager.SHOW_IMPLICIT);
            }
        });

        image_bonus_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_bonus.setEnabled(true);
                edt_bonus.requestFocus();
                btn_update_bonus.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt_bonus,InputMethodManager.SHOW_IMPLICIT);

            }
        });

        image_job_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateJobDialog();

            }
        });

        btn_update_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String salary = edt_salary.getText().toString().trim();
                if (!TextUtils.isEmpty(salary)&&Double.parseDouble(salary)!=userModel.getSalary())
                {
                    edt_salary.setError(null);
                    Common.CloseKeyBoard(activity,edt_salary);
                    updateSalary(salary);
                }else
                    {
                        edt_salary.setError(getString(R.string.field_req));
                    }
            }
        });

        btn_update_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bonus = edt_bonus.getText().toString().trim();
                if (!TextUtils.isEmpty(bonus)&&Double.parseDouble(bonus)!=userModel.getSalary())
                {
                    edt_salary.setError(null);
                    Common.CloseKeyBoard(activity,edt_salary);
                    updateBonus(bonus);
                }else
                {
                    edt_salary.setError(getString(R.string.field_req));
                }

            }
        });
        btn_update_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateJob(selected_job);
            }
        });


    }
    private void updateUi(UserModel userModel)
    {

        if (userModel!=null)
        {
            tv_name.setText(userModel.getName());
            tv_email.setText(userModel.getEmail());
            tv_address.setText(userModel.getAddress());
            tv_phone.setText(userModel.getPhone());
            edt_salary.setText(String.valueOf(userModel.getSalary()));
            edt_bonus.setText(String.valueOf(userModel.getBonus()));

            if (userModel.getUser_type() == Tags.USER_TYPE_HEAD_MANAGER)
            {
                btn_delete.setVisibility(View.GONE);
            }

            if (userModel.getUser_type()== Tags.USER_TYPE_EMPLOYEE)
            {
                tv_job.setText(jobList.get(0));
            }else if (userModel.getUser_type()== Tags.USER_TYPE_SUPERVISOR)
            {
                tv_job.setText(jobList.get(1));

            }
            else if (userModel.getUser_type()== Tags.USER_TYPE_FINANCIAL_MANAGER)
            {
                tv_job.setText(jobList.get(2));

            }
            else if (userModel.getUser_type()== Tags.USER_TYPE_HEAD_MANAGER)
            {
                tv_job.setText(jobList.get(3));

            }
        }
    }
    private void CreateJobDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_jobs,null);
        Button btn_select = view.findViewById(R.id.btn_select);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        final RadioButton rb1 = view.findViewById(R.id.rb1);
        final RadioButton rb2 = view.findViewById(R.id.rb2);
        final RadioButton rb3 = view.findViewById(R.id.rb3);
        final RadioButton rb4 = view.findViewById(R.id.rb4);


        if (userModel.getUser_type() == Tags.USER_TYPE_EMPLOYEE)
        {
            rb1.setChecked(true);
        }else  if (userModel.getUser_type() == Tags.USER_TYPE_SUPERVISOR)
        {
            rb2.setChecked(true);

        }
        else  if (userModel.getUser_type() == Tags.USER_TYPE_FINANCIAL_MANAGER)
        {
            rb3.setChecked(true);

        }
        else  if (userModel.getUser_type() == Tags.USER_TYPE_HEAD_MANAGER)
        {
            rb4.setChecked(true);

        }


        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rb1.isChecked()&&userModel.getUser_type() != Tags.USER_TYPE_EMPLOYEE)
                {
                    tv_job.setText(jobList.get(0));
                    btn_update_job.setVisibility(View.VISIBLE);
                    selected_job = Tags.USER_TYPE_EMPLOYEE;

                }else if (rb2.isChecked()&&userModel.getUser_type() != Tags.USER_TYPE_SUPERVISOR)
                {
                    tv_job.setText(jobList.get(1));
                    btn_update_job.setVisibility(View.VISIBLE);
                    selected_job = Tags.USER_TYPE_SUPERVISOR;
                }
                else if (rb3.isChecked()&&userModel.getUser_type() != Tags.USER_TYPE_FINANCIAL_MANAGER)
                {
                    tv_job.setText(jobList.get(2));
                    btn_update_job.setVisibility(View.VISIBLE);
                    selected_job = Tags.USER_TYPE_FINANCIAL_MANAGER;
                }
                else if (rb4.isChecked()&&userModel.getUser_type() != Tags.USER_TYPE_HEAD_MANAGER)
                {
                    tv_job.setText(jobList.get(3));
                    btn_update_job.setVisibility(View.VISIBLE);
                    selected_job = Tags.USER_TYPE_HEAD_MANAGER;
                }
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }
    private void updateSalary(final String salary)
    {

        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        dRef.child("Users").child(userModel.getUser_id())
                .child("salary")
                .setValue(Double.parseDouble(salary))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            edt_salary.setEnabled(false);
                            btn_update_salary.setVisibility(View.GONE);
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            userModel.setSalary(Double.parseDouble(salary));
                            activity.UpdateEmployeeData(userModel);

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
    private void updateBonus(final String bonus)
    {

        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        dRef.child("Users").child(userModel.getUser_id())
                .child("bonus")
                .setValue(Integer.parseInt(bonus))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            btn_update_bonus.setVisibility(View.GONE);
                            edt_bonus.setEnabled(false);
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            userModel.setBonus(Integer.parseInt(bonus));
                            activity.UpdateEmployeeData(userModel);

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
    private void updateJob(final int selected_job)
    {

        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        dRef.child("Users").child(userModel.getUser_id())
                .child("user_type")
                .setValue(selected_job)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            btn_update_job.setVisibility(View.GONE);

                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            userModel.setUser_type(selected_job);
                            activity.UpdateEmployeeData(userModel);


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

    private void CreateDeleteDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_delete,null);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToDeletedUsers();
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }


    private void addToDeletedUsers() {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        dRef.child("Deleted_Users")
                .child(userModel.getUser_id())
                .setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            ChangeUserActivationState(dialog);
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

    private void ChangeUserActivationState(final ProgressDialog dialog) {

        userModel.setActive(0);
        dRef.child("Users").child(userModel.getUser_id())
                .setValue(userModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            dialog.dismiss();
                            activity.UserDeletedListener();
                            btn_delete.setVisibility(View.GONE);
                            activity.Back();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}
