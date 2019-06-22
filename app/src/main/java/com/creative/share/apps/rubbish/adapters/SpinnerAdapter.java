package com.creative.share.apps.rubbish.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.CityModel;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private List<CityModel> cityModelList;
    private Context context;

    public SpinnerAdapter(List<CityModel> cityModelList, Context context) {
        this.cityModelList = cityModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cityModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_row,parent,false);

        }

        TextView tv_name = convertView.findViewById(R.id.tv_name);

        CityModel cityModel = cityModelList.get(position);
        tv_name.setText(cityModel.getCity_name());

        return convertView;
    }
}
