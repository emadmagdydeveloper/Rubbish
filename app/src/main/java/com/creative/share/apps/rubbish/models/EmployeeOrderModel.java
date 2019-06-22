package com.creative.share.apps.rubbish.models;

import java.io.Serializable;

public class EmployeeOrderModel implements Serializable {

    private String from_id;
    private String from_name;
    private String order_id;
    private long order_time;
    private String supervisor_id;

    public EmployeeOrderModel() {
    }

    public EmployeeOrderModel(String from_id, String from_name, String order_id, long order_time, String supervisor_id) {
        this.from_id = from_id;
        this.from_name = from_name;
        this.order_id = order_id;
        this.order_time = order_time;
        this.supervisor_id = supervisor_id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public long getOrder_time() {
        return order_time;
    }

    public void setOrder_time(long order_time) {
        this.order_time = order_time;
    }

    public String getSupervisor_id() {
        return supervisor_id;
    }

    public void setSupervisor_id(String supervisor_id) {
        this.supervisor_id = supervisor_id;
    }
}
