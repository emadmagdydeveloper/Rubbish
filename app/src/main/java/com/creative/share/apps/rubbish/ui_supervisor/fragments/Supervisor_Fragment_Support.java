package com.creative.share.apps.rubbish.ui_supervisor.fragments;

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
import com.creative.share.apps.rubbish.adapters.SupervisorContactAdapter;
import com.creative.share.apps.rubbish.models.ContactModel;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Supervisor_Fragment_Support extends Fragment {

    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private SupervisorHomeActivity activity;
    private List<ContactModel> contactModelList,reverseList;
    private TextView tv_clear;

    private SupervisorContactAdapter adapter;
    private LinearLayout ll_not;
    private UserModel userModel;
    private Preference preferences;
    private DatabaseReference dRef;



    @Override
    public void onStart() {
        super.onStart();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supervisor_fragment_support, container, false);
        initView(view);
        return view;
    }


    public static Supervisor_Fragment_Support newInstance() {
        return new Supervisor_Fragment_Support();
    }

    private void initView(View view) {

        reverseList = new ArrayList<>();
        contactModelList = new ArrayList<>();
        dRef = FirebaseDatabase.getInstance().getReference();
        activity = (SupervisorHomeActivity) getActivity();
        preferences = Preference.newInstance();
        userModel = preferences.getUserData(activity);
        ll_not = view.findViewById(R.id.ll_not);
        tv_clear = view.findViewById(R.id.tv_clear);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        adapter = new SupervisorContactAdapter(contactModelList,activity);
        recView.setAdapter(adapter);

        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
                dialog.setCancelable(true);
                dialog.show();
                dRef.child("Supervisor_Messages")
                        .child(userModel.getUser_id())
                        .removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                contactModelList.clear();
                                adapter.notifyDataSetChanged();
                                tv_clear.setVisibility(View.GONE);
                                ll_not.setVisibility(View.VISIBLE);
                                dialog.dismiss();

                            }
                        });
            }
        });

        getReports();



    }

    private void getReports() {

        dRef.child("Supervisor_Messages")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null)
                        {
                            progBar.setVisibility(View.GONE);
                            ll_not.setVisibility(View.GONE);

                            reverseList.clear();
                            for (DataSnapshot ds :dataSnapshot.getChildren())
                            {
                                ContactModel contactModel = ds.getValue(ContactModel.class);
                                reverseList.add(contactModel);
                            }

                            if (contactModelList.size()>0)
                            {
                                ReverseList(reverseList);
                                //adapter.notifyDataSetChanged();

                            }else
                                {
                                    progBar.setVisibility(View.GONE);
                                    ll_not.setVisibility(View.VISIBLE);
                                }

                        }else
                            {
                                progBar.setVisibility(View.GONE);
                                ll_not.setVisibility(View.VISIBLE);
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void ReverseList(List<ContactModel> reverseList) {

        contactModelList.clear();
        for (int i=reverseList.size()-1;i>=0;i--)
        {
            contactModelList.add(reverseList.get(i));
        }
        adapter.notifyDataSetChanged();

    }

}
