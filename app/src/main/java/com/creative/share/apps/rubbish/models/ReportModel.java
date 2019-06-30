package com.creative.share.apps.rubbish.models;

import java.io.Serializable;

public class ReportModel implements Serializable {

    private String report_id;
    private String supervisor_id;
    private String supervisor_name;
    private String supervisor_image;
    private String employee_id;
    private String employee_name;
    private String report;
    private long date;

    public ReportModel() {
    }


    public ReportModel(String report_id, String supervisor_id, String supervisor_name, String supervisor_image, String employee_id, String employee_name, String report, long date) {
        this.report_id = report_id;
        this.supervisor_id = supervisor_id;
        this.supervisor_name = supervisor_name;
        this.supervisor_image = supervisor_image;
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.report = report;
        this.date = date;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getSupervisor_id() {
        return supervisor_id;
    }

    public void setSupervisor_id(String supervisor_id) {
        this.supervisor_id = supervisor_id;
    }

    public String getSupervisor_name() {
        return supervisor_name;
    }

    public void setSupervisor_name(String supervisor_name) {
        this.supervisor_name = supervisor_name;
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

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getSupervisor_image() {
        return supervisor_image;
    }

    public void setSupervisor_image(String supervisor_image) {
        this.supervisor_image = supervisor_image;
    }
}
