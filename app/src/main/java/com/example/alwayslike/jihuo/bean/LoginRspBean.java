package com.example.alwayslike.jihuo.bean;

/**
 * Created by alwayslike on 2017/5/9.
 */

public class LoginRspBean {
    private String status;
    private String BST5;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBST5() {
        return BST5;
    }

    public void setBST5(String BST5) {
        this.BST5 = BST5;
    }

    @Override
    public String toString() {
        return "LoginRspBean{" +
                "status='" + status + '\'' +
                ", BST5='" + BST5 + '\'' +
                '}';
    }
}
