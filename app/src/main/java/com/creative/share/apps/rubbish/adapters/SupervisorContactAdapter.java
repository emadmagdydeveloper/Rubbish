package com.creative.share.apps.rubbish.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.ContactModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SupervisorContactAdapter extends RecyclerView.Adapter<SupervisorContactAdapter.MyHolder> {

    private List<ContactModel> contactModelList;
    private Context context;

    public SupervisorContactAdapter(List<ContactModel> contactModelList, Context context) {
        this.contactModelList = contactModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.supervisor_contact_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        ContactModel contactModel = contactModelList.get(position);
        holder.BindData(contactModel);

    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_date,tv_name,tv_email,tv_phone,tv_subject;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_subject = itemView.findViewById(R.id.tv_subject);

        }

        public void BindData(ContactModel contactModel) {

            tv_name.setText(contactModel.getFrom_name());
            tv_email.setText(contactModel.getFrom_email());
            tv_phone.setText(contactModel.getFrom_phone());
            tv_subject.setText(contactModel.getSubject());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.ENGLISH);
            String date = dateFormat.format(new Date(contactModel.getDate()));
            tv_date.setText(date);
        }
    }
}
