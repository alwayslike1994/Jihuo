package com.example.alwayslike.jihuo.bean;

/**
 * Created by alwayslike on 2017/5/16.
 */

public class BaseBean {
    private String code;
    private String token;
    private String data;

    public BaseBean(String code, String msg, String data)
    {
        this.code = code;
        this.token = msg;
        this.data = data;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "BaseBean{" +
                "code='" + code + '\'' +
                ", token='" + token + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
