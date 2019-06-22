package com.creative.share.apps.rubbish.models;

import java.io.Serializable;

public class AcceptanceOrderModel implements Serializable {

    private String employee_id;
    private String employee_name;
    private String order_id;

    private int order_state;

    public AcceptanceOrderModel() {
    }

    public AcceptanceOrderModel(String employee_id, String employee_name, String order_id, int order_state) {
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.order_id = order_id;
        this.order_state = order_state;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }
}
