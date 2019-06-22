package com.creative.share.apps.rubbish.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.creative.share.apps.rubbish.R;

import java.util.List;


public class Slider_Adapter extends PagerAdapter {


    private List<Integer> sliderModelList;
    private Context context;


    public Slider_Adapter(Context context, List<Integer> sliderModelList) {
        this.context = context;
        this.sliderModelList = sliderModelList;
    }


    @Override
    public int getCount() {
        return sliderModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.slider_row,container,false);

        int img_id = sliderModelList.get(position);
        final ImageView image = view.findViewById(R.id.image);

        image.setImageResource(img_id);

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
