package com.creative.share.apps.rubbish.ui_clients.fragments;

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
import com.creative.share.apps.rubbish.adapters.ClientNotificationAdapter;
import com.creative.share.apps.rubbish.models.NotificationModel;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Client_Fragment_Notifications extends Fragment {

    private ProgressBar progBar;
    private TextView tv_clear;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ClientHomeActivity activity;
    private List<NotificationModel> notificationModelList,reverseList;
    private ClientNotificationAdapter adapter;
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
        View view = inflater.inflate(R.layout.client_fragment_notifications, container, false);
        initView(view);
        return view;
    }


    public static Client_Fragment_Notifications newInstance() {
        return new Client_Fragment_Notifications();
    }

    private void initView(View view) {

        reverseList = new ArrayList<>();
        notificationModelList = new ArrayList<>();
        dRef = FirebaseDatabase.getInstance().getReference();
        activity = (ClientHomeActivity) getActivity();
        preferences = Preference.newInstance();
        userModel = preferences.getUserData(activity);
        ll_not = view.findViewById(R.id.ll_not);
        tv_clear = view.findViewById(R.id.tv_clear);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        adapter = new ClientNotificationAdapter(notificationModelList,activity);
        recView.setAdapter(adapter);
        getNotification();

        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
                dialog.setCancelable(true);
                dialog.show();
                dRef.child("Notification_Client")
                        .child(userModel.getUser_id())
                        .removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                notificationModelList.clear();
                                adapter.notifyDataSetChanged();
                                tv_clear.setVisibility(View.GONE);
                                ll_not.setVisibility(View.VISIBLE);
                                dialog.dismiss();

                            }
                        });
            }
        });

    }

    private void getNotification() {

        dRef.child("Notification_Client")
                .child(userModel.getUser_id())
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
                                NotificationModel notificationModel = ds.getValue(NotificationModel.class);
                                reverseList.add(notificationModel);
                            }

                            if (reverseList.size()>0)
                            {
                                ReverseList(reverseList);
                                tv_clear.setVisibility(View.VISIBLE);

                            }else
                                {
                                    tv_clear.setVisibility(View.GONE);
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

    private void ReverseList(List<NotificationModel> reverseList) {
        this.notificationModelList.clear();
        for (int index = reverseList.size()-1;index>=0;index--)
        {
            this.notificationModelList.add(reverseList.get(index));

        }
        adapter.notifyDataSetChanged();
    }

}
