package com.creative.share.apps.rubbish.ui_head_manager.fragmens;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.adapters.EmployeeAdapter;
import com.creative.share.apps.rubbish.adapters.SpinnerAdapter;
import com.creative.share.apps.rubbish.models.CityModel;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_head_manager.HeadMangerHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Head_Manager_Fragment_Search extends Fragment {

    private EditText edt_search;
    private TextView tv_no_emp;
    private Spinner spinner_city;
    private SpinnerAdapter city_adapter;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private ProgressBar progBar;
    private EmployeeAdapter adapter;
    private HeadMangerHomeActivity activity;
    private DatabaseReference dRef;
    private List<CityModel> cityModelList;

    private List<UserModel> employeeModelList,mainEmployeeList;
    private int lastSelectedPos=-1;



    public static Head_Manager_Fragment_Search newInstance() {
        return new Head_Manager_Fragment_Search();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.financial_fragment_search, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        dRef = FirebaseDatabase.getInstance().getReference();
        cityModelList = new ArrayList<>();
        cityModelList.add(new CityModel(0,"إختر المحافظة"));
        mainEmployeeList = new ArrayList<>();
        employeeModelList = new ArrayList<>();
        activity = (HeadMangerHomeActivity) getActivity();
        tv_no_emp = view.findViewById(R.id.tv_no_emp);



        spinner_city = view.findViewById(R.id.spinner_city);

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
                    employeeModelList.addAll(mainEmployeeList);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        city_adapter = new SpinnerAdapter(cityModelList,activity);
        spinner_city.setAdapter(city_adapter);

        getCity();

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0)
                {


                    int city_id = cityModelList.get(position).getCity_id();
                    getEmployee(city_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


    private void getCity() {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();
        dRef.child("City")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null)
                        {
                            for (DataSnapshot ds :dataSnapshot.getChildren())
                            {
                                CityModel cityModel = ds.getValue(CityModel.class);
                                cityModelList.add(cityModel);

                            }


                            if (cityModelList.size()>0)
                            {
                                city_adapter.notifyDataSetChanged();
                            }
                            dialog.dismiss();

                        }else
                            {
                                dialog.dismiss();
                            }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });
    }

    private void search(String emp_name) {
        employeeModelList.clear();
        for (UserModel userModel :mainEmployeeList)
        {
            if (userModel.getName().contains(emp_name))
            {
                employeeModelList.add(userModel);

            }
        }

        if (employeeModelList.size()>0)
        {
            adapter.notifyDataSetChanged();
            tv_no_emp.setVisibility(View.GONE);

        }else
        {
            tv_no_emp.setVisibility(View.VISIBLE);



        }
    }



    private void getEmployee(final int city_id) {
        employeeModelList.clear();
        tv_no_emp.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
        progBar.setVisibility(View.VISIBLE);
        dRef.child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {

                            employeeModelList.clear();
                            mainEmployeeList.clear();
                            for (DataSnapshot ds:dataSnapshot.getChildren())
                            {
                                UserModel userMode = ds.getValue(UserModel.class);

                                if (userMode!=null&&userMode.getActive()==1)
                                {
                                    if (userMode.getUser_type()== Tags.USER_TYPE_EMPLOYEE ||userMode.getUser_type()== Tags.USER_TYPE_SUPERVISOR ||userMode.getUser_type()== Tags.USER_TYPE_FINANCIAL_MANAGER||userMode.getUser_type()== Tags.USER_TYPE_HEAD_MANAGER)
                                    {
                                        if (userMode.getCity_id()==city_id)
                                        {


                                            employeeModelList.add(userMode);
                                            mainEmployeeList.add(userMode);

                                        }
                                    }
                                }



                            }

                            if (employeeModelList.size()>0)
                            {
                                tv_no_emp.setVisibility(View.GONE);

                                adapter.notifyDataSetChanged();


                            }else
                                {
                                    tv_no_emp.setVisibility(View.VISIBLE);
                                }

                            progBar.setVisibility(View.GONE);


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



    public void setItemData(UserModel userModel, int position) {
        lastSelectedPos = position;
        activity.DisplayFragmentUserData(userModel);
    }

    public void updateUserData(UserModel userModel) {
        employeeModelList.set(lastSelectedPos,userModel);
        adapter.notifyDataSetChanged();

    }

    public void deleteUser() {

        employeeModelList.remove(lastSelectedPos);
        adapter.notifyDataSetChanged();
    }
}
