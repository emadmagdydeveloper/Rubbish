package com.creative.share.apps.rubbish.ui_head_manager.fragmens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.ui_head_manager.HeadMangerHomeActivity;


public class Head_Manager_Fragment_Home extends Fragment {
    private AHBottomNavigation bottomNavigationView;
    private HeadMangerHomeActivity activity;
    private TextView tv_title;
    private UserModel userModel;
    private Preference preferences;

    public static Head_Manager_Fragment_Home newInstance() {
        return new Head_Manager_Fragment_Home();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.head_manager_fragment_home, container, false);

        initView(view);

        setUpBottomNav();

        return view;
    }

    private void initView(View view) {
        preferences = Preference.newInstance();
        activity = (HeadMangerHomeActivity) getActivity();
        userModel = preferences.getUserData(activity);

        tv_title = view.findViewById(R.id.tv_title);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        tv_title.setText(getString(R.string.profile));


    }


    private void setUpBottomNav() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.profile), R.drawable.ic_user, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.search3), R.drawable.ic_search, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.report), R.drawable.ic_report, R.color.colorPrimary);

        bottomNavigationView.addItem(item1);
        bottomNavigationView.addItem(item2);
        bottomNavigationView.addItem(item3);


        bottomNavigationView.setAccentColor(ContextCompat.getColor(activity, R.color.yellow));
        bottomNavigationView.setDefaultBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        bottomNavigationView.setInactiveColor(ContextCompat.getColor(activity, R.color.black));
        bottomNavigationView.setForceTint(true);
        bottomNavigationView.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigationView.setColored(false);
        bottomNavigationView.setTitleTextSizeInSp(13, 12);
        bottomNavigationView.setCurrentItem(0);


        bottomNavigationView.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        activity.DisplayFragmentProfile();
                        break;
                    case 1:
                        activity.DisplayFragmentSearch();

                        break;
                    case 2:
                        activity.DisplayFragmentReports();

                        break;




                }
                return false;
            }
        });
    }

    public void UpdateAHBottomNavigationPosition(int pos) {

        if (pos == 0) {
            tv_title.setText(getString(R.string.profile));
        } else if (pos == 1) {
            tv_title.setText(getString(R.string.search3));

        }
        else if (pos == 2) {
            tv_title.setText(getString(R.string.reports));

        }

        bottomNavigationView.setCurrentItem(pos, false);
    }
}