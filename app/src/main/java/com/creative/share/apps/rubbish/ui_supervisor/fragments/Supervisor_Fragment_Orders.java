package com.creative.share.apps.rubbish.ui_supervisor.fragments;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.adapters.SupervisorOrderAdapter;
import com.creative.share.apps.rubbish.models.OrderModel;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Supervisor_Fragment_Orders extends Fragment {

    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private SupervisorHomeActivity activity;
    private SupervisorOrderAdapter adapter;
    private List<OrderModel> orderModelList;
    private LinearLayout ll_order;
    private int lastSelectedItem = -1;
    private DatabaseReference dRef;
    private List<String> emp_ids_list;
    private OrderModel orderModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supervisor_fragment_order, container, false);
        initView(view);
        return view;
    }


    public static Supervisor_Fragment_Orders newInstance() {
        return new Supervisor_Fragment_Orders();
    }

    private void initView(View view) {

        emp_ids_list = new ArrayList<>();
        dRef = FirebaseDatabase.getInstance().getReference();
        orderModelList = new ArrayList<>();
        activity = (SupervisorHomeActivity) getActivity();

        ll_order = view.findViewById(R.id.ll_order);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);



        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        adapter = new SupervisorOrderAdapter(orderModelList,activity,this);
        recView.setAdapter(adapter);
        getOrders();

    }

    private void getOrders()
    {

        dRef.child("Supervisor_Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orderModelList.clear();
                if (dataSnapshot.getValue()!=null)
                {
                    ll_order.setVisibility(View.GONE);
                    progBar.setVisibility(View.GONE);

                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if (ds.getValue()!=null)
                        {
                            OrderModel orderModel = ds.getValue(OrderModel.class);
                            orderModelList.add(orderModel);
                        }
                    }

                    adapter.notifyDataSetChanged();

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

    public void setItemData(OrderModel orderModel, int position) {
        if (orderModel.getOrder_is_sent()==0)
        {
            lastSelectedItem = position;
            this.orderModel = orderModel;
            getAllEmployeeIds();
        }
    }



    private void getAllEmployeeIds() {

        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

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
                                SendOrder(emp_ids_list,dialog);
                            } else {
                                dialog.dismiss();
                                Toast.makeText(activity, getString(R.string.no_emp_to_send), Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.no_emp_to_send), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void SendOrder(List<String> emp_ids_list, ProgressDialog dialog) {

        Calendar calendar = Calendar.getInstance();

        orderModel.setOrder_is_sent(1);
        orderModel.setOrder_time(calendar.getTimeInMillis());

        for (String emp_id:emp_ids_list)
        {
            dRef.child("Employee_Orders").child(emp_id).child(orderModel.getOrder_id())
                    .setValue(orderModel);
        }

        dRef.child("Supervisor_Orders")
                .child(orderModel.getOrder_id())
                .setValue(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    orderModelList.set(lastSelectedItem, orderModel);
                    adapter.notifyDataSetChanged();
                    lastSelectedItem= -1;
                    orderModel = null;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        dialog.dismiss();
    }


}
