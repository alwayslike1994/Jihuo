package com.example.alwayslike.jihuo.bean;

/**
 * Created by alwayslike on 2017/5/9.
 */

public class LoginReqBean {
    private String phonenumber;
    private String passwd;

    public LoginReqBean(String phonenumber, String passwd) {
        this.phonenumber = phonenumber;
        this.passwd = passwd;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
