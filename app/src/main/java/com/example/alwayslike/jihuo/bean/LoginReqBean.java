package com.example.alwayslike.jihuo.bean;

/**
 * Created by alwayslike on 2017/5/9.
 */

public class LoginReqBean {
    private String method;
    private String username;
    private String passwd;

    public LoginReqBean(String method, String username, String passwd) {
        this.method = method;
        this.username = username;
        this.passwd = passwd;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
