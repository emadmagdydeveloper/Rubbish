package com.creative.share.apps.rubbish.ui_clients.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.adapters.Slider_Adapter;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Client_Fragment_main extends Fragment {
    private ViewPager pager;
    private TabLayout tab;
    private ClientHomeActivity activity;
    private Slider_Adapter slider_adapter;
    private List<Integer> sliderImageList;
    private LinearLayout ll1,ll2,ll3,ll4,ll5,ll6;
    private TimerTask timerTask;
    private Timer timer;
    public static Client_Fragment_main newInstance() {
        return new Client_Fragment_main();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_fragment_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        sliderImageList = new ArrayList<>();
        activity = (ClientHomeActivity) getActivity();
        pager = view.findViewById(R.id.pager);
        tab = view.findViewById(R.id.tab);
        ll1 = view.findViewById(R.id.ll1);
        ll2 = view.findViewById(R.id.ll2);
        ll3 = view.findViewById(R.id.ll3);
        ll4 = view.findViewById(R.id.ll4);
        ll5 = view.findViewById(R.id.ll5);
        ll6 = view.findViewById(R.id.ll6);

        tab.setupWithViewPager(pager);
        updateUI();


        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setDataForSheet(getString(R.string.papers),getString(R.string.paper_content));
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setDataForSheet(getString(R.string.metal),getString(R.string.metal_content));

            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setDataForSheet(getString(R.string.organic),getString(R.string.organic_content));

            }
        });

        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setDataForSheet(getString(R.string.battery),getString(R.string.battery_content));

            }
        });
        ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setDataForSheet(getString(R.string.e_waste),getString(R.string.ewaste_content));

            }
        });
        ll6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setDataForSheet(getString(R.string.plastics),getString(R.string.plastic_content));

            }
        });

    }


    private void updateUI() {

        sliderImageList.add(R.drawable.logo);
        sliderImageList.add(R.drawable.logo);
        sliderImageList.add(R.drawable.logo);
        sliderImageList.add(R.drawable.logo);
        sliderImageList.add(R.drawable.logo);
        sliderImageList.add(R.drawable.logo);
        sliderImageList.add(R.drawable.logo);
        sliderImageList.add(R.drawable.logo);


        if (sliderImageList.size()>1)
        {
            slider_adapter = new Slider_Adapter(activity,sliderImageList);
            pager.setAdapter(slider_adapter);
            timerTask = new MyTimerTask();
            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask,6000,6000);

            for (int i = 0 ; i<sliderImageList.size()-1;i++)
            {
                View view = ((ViewGroup)tab.getChildAt(0)).getChildAt(i);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                params.setMargins(5,0,5,0);
                tab.requestLayout();

            }

        }else
            {
                slider_adapter = new Slider_Adapter(activity,sliderImageList);
                pager.setAdapter(slider_adapter);
            }
    }


    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pager.getCurrentItem()<pager.getAdapter().getCount()-1)
                    {
                        pager.setCurrentItem(pager.getCurrentItem()+1);
                    }else
                        {
                            pager.setCurrentItem(0);
                        }
                }
            });
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer!=null)
        {
            timer.purge();
            timer.cancel();
        }

        if (timerTask!=null)
        {
            timerTask.cancel();
        }


    }
}
