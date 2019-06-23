package com.creative.share.apps.rubbish.ui_employee.fragments;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.adapters.EmployeeOrderAdapter;
import com.creative.share.apps.rubbish.models.OrderModel;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.ui_employee.EmployeeHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Employee_Fragment_Orders extends Fragment {

    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private EmployeeHomeActivity activity;
    private List<OrderModel> orderModelList,reverseList;
    private EmployeeOrderAdapter adapter;
    private LinearLayout ll_order;
    private UserModel userModel;
    private Preference preferences;
    private TextView tv_clear;
    private int lastSelectedItem = -1;
    private OrderModel orderModel;
    private String current_language;
    private DatabaseReference dRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employee_fragment_order, container, false);
        initView(view);
        return view;
    }


    public static Employee_Fragment_Orders newInstance() {
        return new Employee_Fragment_Orders();
    }

    private void initView(View view) {
        dRef = FirebaseDatabase.getInstance().getReference();
        reverseList = new ArrayList<>();
        orderModelList = new ArrayList<>();
        activity = (EmployeeHomeActivity) getActivity();
        current_language = Locale.getDefault().getLanguage();
        preferences = Preference.newInstance();
        userModel = preferences.getUserData(activity);
        tv_clear = view.findViewById(R.id.tv_clear);

        ll_order = view.findViewById(R.id.ll_order);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);

        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        adapter = new EmployeeOrderAdapter(orderModelList,activity,this);
        recView.setAdapter(adapter);

        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
                dialog.setCancelable(true);
                dialog.show();
                dRef.child("Employee_Orders")
                        .child(userModel.getUser_id())
                        .removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                orderModelList.clear();
                                adapter.notifyDataSetChanged();
                                tv_clear.setVisibility(View.GONE);
                                ll_order.setVisibility(View.VISIBLE);
                                dialog.dismiss();

                            }
                        });
            }
        });

        getOrders();

    }

    private void getOrders() {

        dRef.child("Employee_Orders").child(userModel.getUser_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                reverseList.clear();
                if (dataSnapshot.getValue()!=null)
                {
                    ll_order.setVisibility(View.GONE);
                    progBar.setVisibility(View.GONE);

                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if (ds.getValue()!=null)
                        {
                            OrderModel orderModel = ds.getValue(OrderModel.class);
                            //orderModelList.add(orderModel);
                            reverseList.add(orderModel);

                        }
                    }

                    if (reverseList.size()>0)
                    {
                        ReverseList(reverseList);
                    }

                    //adapter.notifyDataSetChanged();

                }else
                {
                    ll_order.setVisibility(View.VISIBLE);
                    progBar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ReverseList(List<OrderModel> reverseList) {

        orderModelList.clear();
        for (int i = reverseList.size()-1;i>=0;i--)
        {
            orderModelList.add(reverseList.get(i));
        }
        adapter.notifyDataSetChanged();
    }


    public void setItemData(OrderModel orderModel, int position) {

        this.lastSelectedItem =position;
        this.orderModel = orderModel;
        activity.DisplayFragmentOrderDetails(orderModel);
    }
}
