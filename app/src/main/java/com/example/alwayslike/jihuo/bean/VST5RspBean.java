package com.example.alwayslike.jihuo.bean;

/**
 * Created by alwayslike on 2017/5/9.
 */

public class VST5RspBean {
    private String status;
    private String Double;

    public VST5RspBean(String status, String aDouble) {
        this.status = status;
        Double = aDouble;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDouble() {
        return Double;
    }

    public void setDouble(String aDouble) {
        Double = aDouble;
    }
}
