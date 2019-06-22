package com.creative.share.apps.rubbish.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {

    private String notification_id;
    private String title;
    private String content;
    private long date;


    public NotificationModel() {
    }

    public NotificationModel(String notification_id, String title, String content, long date) {
        this.notification_id = notification_id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
