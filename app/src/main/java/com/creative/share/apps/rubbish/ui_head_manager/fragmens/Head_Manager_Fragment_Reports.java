package com.creative.share.apps.rubbish.ui_head_manager.fragmens;

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
import com.creative.share.apps.rubbish.adapters.HeadManagerReportAdapter;
import com.creative.share.apps.rubbish.models.ReportModel;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.ui_head_manager.HeadMangerHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Head_Manager_Fragment_Reports extends Fragment {

    private ProgressBar progBar;
    private RecyclerView recView;
    private TextView tv_clear;
    private RecyclerView.LayoutManager manager;
    private HeadMangerHomeActivity activity;
    private List<ReportModel> reportModelList, reverseList;
    private HeadManagerReportAdapter adapter;
    private LinearLayout ll_no_report;
    private DatabaseReference dRef;



    @Override
    public void onStart() {
        super.onStart();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.head_manager_fragment_report, container, false);
        initView(view);
        return view;
    }


    public static Head_Manager_Fragment_Reports newInstance() {
        return new Head_Manager_Fragment_Reports();
    }

    private void initView(View view) {

        reverseList = new ArrayList<>();
        reportModelList = new ArrayList<>();
        dRef = FirebaseDatabase.getInstance().getReference();
        activity = (HeadMangerHomeActivity) getActivity();
        ll_no_report = view.findViewById(R.id.ll_no_report);
        tv_clear = view.findViewById(R.id.tv_clear);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        adapter = new HeadManagerReportAdapter(reportModelList,activity);
        recView.setAdapter(adapter);
        getReports();

        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAll();
            }
        });

    }

    private void DeleteAll() {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        dRef.child("Reports")
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        dialog.dismiss();
                        tv_clear.setVisibility(View.GONE);
                        reportModelList.clear();
                        adapter.notifyDataSetChanged();
                        ll_no_report.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void getReports() {

        dRef.child("Reports")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null)
                        {
                            progBar.setVisibility(View.GONE);
                            ll_no_report.setVisibility(View.GONE);

                            reverseList.clear();
                            for (DataSnapshot ds :dataSnapshot.getChildren())
                            {
                                ReportModel reportModel = ds.getValue(ReportModel.class);
                                //reportModelList.add(reportModel);
                                reverseList.add(reportModel);
                            }

                            if (reverseList.size()>0)
                            {
                                //adapter.notifyDataSetChanged();
                                tv_clear.setVisibility(View.VISIBLE);
                                ReverseList(reverseList);


                            }else
                                {
                                    progBar.setVisibility(View.GONE);
                                    ll_no_report.setVisibility(View.VISIBLE);
                                    tv_clear.setVisibility(View.GONE);

                                }

                        }else
                            {
                                progBar.setVisibility(View.GONE);
                                ll_no_report.setVisibility(View.VISIBLE);
                                tv_clear.setVisibility(View.GONE);

                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void ReverseList(List<ReportModel> reverseList) {

        reportModelList.clear();
        for (int i = reverseList.size()-1;i>=0;i--)
        {
            reportModelList.add(reverseList.get(i));
        }
        adapter.notifyDataSetChanged();
    }

}
