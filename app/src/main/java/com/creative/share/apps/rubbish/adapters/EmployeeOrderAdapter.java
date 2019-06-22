package com.creative.share.apps.rubbish.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.OrderModel;
import com.creative.share.apps.rubbish.ui_employee.fragments.Employee_Fragment_Orders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmployeeOrderAdapter extends RecyclerView.Adapter<EmployeeOrderAdapter.MyHolder> {

    private List<OrderModel> orderModelList;
    private Context context;
    private Employee_Fragment_Orders fragment_orders;

    public EmployeeOrderAdapter(List<OrderModel> orderModelList, Context context, Employee_Fragment_Orders fragment_orders) {
        this.orderModelList = orderModelList;
        this.context = context;
        this.fragment_orders = fragment_orders;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_order_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        OrderModel orderModel = orderModelList.get(position);
        holder.BindData(orderModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderModel orderModel = orderModelList.get(holder.getAdapterPosition());
                fragment_orders.setItemData(orderModel,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_client_name,tv_order_state,tv_order_date;

        private MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_client_name = itemView.findViewById(R.id.tv_client_name);
            tv_order_state = itemView.findViewById(R.id.tv_order_state);
            tv_order_date = itemView.findViewById(R.id.tv_order_date);

        }

        private void BindData(OrderModel orderModel)
        {
            tv_client_name.setText(orderModel.getFrom_name());
            tv_order_state.setText(context.getString(R.string.new_order));
            tv_order_state.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));



            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa",Locale.ENGLISH);
            String date = dateFormat.format(new Date(orderModel.getOrder_time()));
            tv_order_date.setText(date);
        }
    }
}