package com.example.alwayslike.jihuo.bean;

/**
 * Created by alwayslike on 2017/5/9.
 */

public class VST5RspBean {
    private String status;
    private String activite;

    public VST5RspBean(String status, String aActivite) {
        this.status = status;
        activite = aActivite;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }
}
