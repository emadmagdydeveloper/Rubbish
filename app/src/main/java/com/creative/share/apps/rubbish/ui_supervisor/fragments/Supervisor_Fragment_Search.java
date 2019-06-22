package com.creative.share.apps.rubbish.ui_supervisor.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.adapters.EmployeeAdapter;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Supervisor_Fragment_Search extends Fragment {

    private EditText edt_search;
    private TextView tv_no_emp;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private ProgressBar progBar;
    private EmployeeAdapter adapter;
    private SupervisorHomeActivity activity;
    private DatabaseReference dRef;
    private List<String> emp_ids_list;
    private List<UserModel> main_emp_list;
    private List<UserModel> employeeModelList;


    public static Supervisor_Fragment_Search newInstance() {
        return new Supervisor_Fragment_Search();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supervisor_fragment_search, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        dRef = FirebaseDatabase.getInstance().getReference();
        employeeModelList = new ArrayList<>();
        main_emp_list = new ArrayList<>();
        emp_ids_list = new ArrayList<>();
        activity = (SupervisorHomeActivity) getActivity();
        tv_no_emp = view.findViewById(R.id.tv_no_emp);

        edt_search = view.findViewById(R.id.edt_search);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ActivityCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        adapter = new EmployeeAdapter(employeeModelList, activity, this);
        recView.setAdapter(adapter);

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    String emp_name = edt_search.getText().toString().trim();
                    if (!TextUtils.isEmpty(emp_name))
                    {
                        search(emp_name);
                    }
                }
                return false;
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String emp_name = edt_search.getText().toString().trim();
                if (TextUtils.isEmpty(emp_name))
                {
                    tv_no_emp.setVisibility(View.GONE);
                    employeeModelList.clear();
                    employeeModelList.addAll(main_emp_list);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        getEmployeesIds();
    }

    private void search(String emp_name) {

        for (UserModel userModel :main_emp_list)
        {
            if (userModel.getName().contains(emp_name))
            {
                employeeModelList.clear();
                employeeModelList.add(userModel);
            }
        }

        adapter.notifyDataSetChanged();

    }

    private void getEmployeesIds() {
        dRef.child("Employees")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            emp_ids_list.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String emp_id = ds.getKey();
                                emp_ids_list.add(emp_id);

                            }

                            if (emp_ids_list.size() > 0) {
                                tv_no_emp.setVisibility(View.GONE);
                                getEmployee(emp_ids_list);

                            } else {
                                progBar.setVisibility(View.GONE);
                                tv_no_emp.setVisibility(View.VISIBLE);

                            }
                        } else {
                            progBar.setVisibility(View.GONE);
                            tv_no_emp.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getEmployee(final List<String> emp_ids_list) {

        dRef.child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {

                            main_emp_list.clear();
                            employeeModelList.clear();

                            getEmployeeData(dataSnapshot,emp_ids_list);

                        } else {
                            progBar.setVisibility(View.GONE);
                            tv_no_emp.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void getEmployeeData(DataSnapshot dataSnapshot, List<String> emp_ids_list) {

        for (String emp_id : emp_ids_list) {
            if (dataSnapshot.child(emp_id).getValue() != null) {

                UserModel userModel = dataSnapshot.child(emp_id).getValue(UserModel.class);
                if (userModel != null && userModel.getActive() == 1) {
                    main_emp_list.add(userModel);
                    employeeModelList.add(userModel);
                }


            }

        }

        if (employeeModelList.size() == 0) {
            tv_no_emp.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
        progBar.setVisibility(View.GONE);
    }

    public void setItemData(UserModel userModel) {
        Log.e("dd","ssss");
        activity.DisplayFragmentUserData(userModel);
    }
}
