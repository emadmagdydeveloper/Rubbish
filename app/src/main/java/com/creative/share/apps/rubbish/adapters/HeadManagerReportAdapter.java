package com.creative.share.apps.rubbish.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.ReportModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HeadManagerReportAdapter extends RecyclerView.Adapter<HeadManagerReportAdapter.MyHolder> {

    private List<ReportModel> reportModelList;
    private Context context;

    public HeadManagerReportAdapter(List<ReportModel> reportModelList, Context context) {
        this.reportModelList = reportModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.head_manager_report_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        ReportModel reportModel = reportModelList.get(position);
        holder.BindData(reportModel);

    }

    @Override
    public int getItemCount() {
        return reportModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView tv_date,tv_name,tv_supervisor_name,tv_subject;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_name = itemView.findViewById(R.id.tv_name);

            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_supervisor_name = itemView.findViewById(R.id.tv_supervisor_name);
            image = itemView.findViewById(R.id.image);

        }

        public void BindData(ReportModel reportModel) {

            tv_name.setText(reportModel.getEmployee_name());
            tv_supervisor_name.setText(reportModel.getSupervisor_name());
            tv_subject.setText(reportModel.getReport());

            if (reportModel.getSupervisor_image()!=null&&!TextUtils.isEmpty(reportModel.getSupervisor_image()))
            {
                Picasso.with(context).load(Uri.parse(reportModel.getSupervisor_image())).placeholder(R.drawable.user_avatar).fit().into(image);

            }else
            {
                Picasso.with(context).load(R.drawable.user_avatar).fit().into(image);

            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.ENGLISH);
            String date = dateFormat.format(new Date(reportModel.getDate()));
            tv_date.setText(date);
        }
    }
}
