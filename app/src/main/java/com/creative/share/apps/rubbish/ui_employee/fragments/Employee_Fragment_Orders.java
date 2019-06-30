package com.creative.share.apps.rubbish.ui_employee.fragments;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.adapters.EmployeeOrderAdapter;
import com.creative.share.apps.rubbish.adapters.RecyclerviewItemTouch;
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

public class Employee_Fragment_Orders extends Fragment implements RecyclerviewItemTouch.SwipeListener {

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
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerviewItemTouch(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recView);

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

        activity.DisplayFragmentOrderDetails(orderModel);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        OrderModel orderModel = orderModelList.get(position);
        CreateDeleteAlertDialog(orderModel,position);
    }

    private void CreateDeleteAlertDialog(final OrderModel orderModel, final int position) {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_delete,null);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                adapter.notifyDataSetChanged();
                DeleteOrder(orderModel,position);

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    private void DeleteOrder(OrderModel orderModel, final int position) {

        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        dRef.child("Employee_Orders").child(userModel.getUser_id()).child(orderModel.getOrder_id())
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                        orderModelList.remove(position);
                        adapter.notifyItemRemoved(position);
                        new Handler()
                                .postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                },500);
                    }
                });
    }
}
