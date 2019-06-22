package com.creative.share.apps.rubbish.models;

import java.io.Serializable;

public class ContactModel implements Serializable {

    private String from_id;
    private String from_name;
    private String from_email;
    private String from_phone;
    private String subject;
    private long date;



    public ContactModel() {
    }


    public ContactModel(String from_id, String from_name, String from_email, String from_phone, String subject, long date) {
        this.from_id = from_id;
        this.from_name = from_name;
        this.from_email = from_email;
        this.from_phone = from_phone;
        this.subject = subject;
        this.date = date;
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

    public String getFrom_email() {
        return from_email;
    }

    public void setFrom_email(String from_email) {
        this.from_email = from_email;
    }

    public String getFrom_phone() {
        return from_phone;
    }

    public void setFrom_phone(String from_phone) {
        this.from_phone = from_phone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
