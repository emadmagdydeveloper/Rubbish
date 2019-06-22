package com.creative.share.apps.rubbish.models;

import java.io.Serializable;

public class OrderModel implements Serializable {

    private String from_id;
    private String from_name;
    private String from_address;
    private double from_latitude;
    private double from_longitude;
    private int order_is_sent;
    private String order_id;
    private long order_time;

    public OrderModel() {
    }

    public OrderModel(String from_id, String from_name, String from_address, double from_latitude, double from_longitude, int order_is_sent, String order_id, long order_time) {
        this.from_id = from_id;
        this.from_name = from_name;
        this.from_address = from_address;
        this.from_latitude = from_latitude;
        this.from_longitude = from_longitude;
        this.order_is_sent = order_is_sent;
        this.order_id = order_id;
        this.order_time = order_time;
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

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public double getFrom_latitude() {
        return from_latitude;
    }

    public void setFrom_latitude(double from_latitude) {
        this.from_latitude = from_latitude;
    }

    public double getFrom_longitude() {
        return from_longitude;
    }

    public void setFrom_longitude(double from_longitude) {
        this.from_longitude = from_longitude;
    }

    public int getOrder_is_sent() {
        return order_is_sent;
    }

    public void setOrder_is_sent(int order_is_sent) {
        this.order_is_sent = order_is_sent;
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
}
