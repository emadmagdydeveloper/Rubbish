package com.creative.share.apps.rubbish.models;

import java.io.Serializable;

public class Terms_About_Model implements Serializable {
    private String ar_content;
    private String en_content;

    public Terms_About_Model() {
    }

    public Terms_About_Model(String ar_content, String en_content) {
        this.ar_content = ar_content;
        this.en_content = en_content;
    }

    public String getAr_content() {
        return ar_content;
    }

    public void setAr_content(String ar_content) {
        this.ar_content = ar_content;
    }

    public String getEn_content() {
        return en_content;
    }

    public void setEn_content(String en_content) {
        this.en_content = en_content;
    }
}
