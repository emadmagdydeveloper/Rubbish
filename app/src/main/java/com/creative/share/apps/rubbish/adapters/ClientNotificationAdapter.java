package com.creative.share.apps.rubbish.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.NotificationModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClientNotificationAdapter extends RecyclerView.Adapter<ClientNotificationAdapter.MyHolder> {

    private List<NotificationModel> notificationModelList;
    private Context context;

    public ClientNotificationAdapter(List<NotificationModel> notificationModelList, Context context) {
        this.notificationModelList = notificationModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.client_notification_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        NotificationModel notificationModel = notificationModelList.get(position);
        holder.BindData(notificationModel);

    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_title,tv_content,tv_date;

        private MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);

        }

        private void BindData(NotificationModel notificationModel)
        {
            tv_title.setText(notificationModel.getTitle());
            tv_content.setText(notificationModel.getContent());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa",Locale.ENGLISH);
            String date = dateFormat.format(new Date(notificationModel.getDate()));
            tv_date.setText(date);
        }
    }
}
