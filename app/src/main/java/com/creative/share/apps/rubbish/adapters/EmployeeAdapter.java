package com.creative.share.apps.rubbish.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.ui_financial_manager.fragments.Financial_Fragment_Search;
import com.creative.share.apps.rubbish.ui_head_manager.fragmens.Head_Manager_Fragment_Search;
import com.creative.share.apps.rubbish.ui_supervisor.fragments.Supervisor_Fragment_Search;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyHolder> {

    private List<UserModel> userModelList;
    private Context context;
    private Fragment fragment;

    public EmployeeAdapter(List<UserModel> userModelList, Context context, Fragment fragment) {
        this.userModelList = userModelList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        UserModel userModel = userModelList.get(position);
        holder.BindData(userModel);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = userModelList.get(holder.getAdapterPosition());
                if (fragment instanceof Financial_Fragment_Search)
                {
                    Financial_Fragment_Search financial_fragment_search = (Financial_Fragment_Search) fragment;
                    financial_fragment_search.setItemData(userModel);

                }else if (fragment instanceof Head_Manager_Fragment_Search)
                {
                    Head_Manager_Fragment_Search head_manager_fragment_search = (Head_Manager_Fragment_Search) fragment;
                    head_manager_fragment_search.setItemData(userModel,position);

                }else  if (fragment instanceof Supervisor_Fragment_Search)
                {
                    Supervisor_Fragment_Search supervisor_fragment_search = (Supervisor_Fragment_Search) fragment;
                    supervisor_fragment_search.setItemData(userModel);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);

        }

        public void BindData(UserModel userModel) {
            tv_name.setText(userModel.getName());

        }
    }
}
