package com.example.alwayslike.jihuo.bean;

/**
 * Created by alwayslike on 2017/5/16.
 */

public class BaseBean {
    private String Result;
    private String Code;
    private String Msg;
    private String Data;

    public BaseBean(String result, String code, String msg, String data) {
        this.Result = result;
        this.Code = code;
        this.Msg = msg;
        this.Data = data;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        this.Result = result;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        this.Msg = msg;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        this.Data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "Result='" + Result + '\'' +
                ", Code='" + Code + '\'' +
                ", Msg='" + Msg + '\'' +
                ", Data='" + Data + '\'' +
                '}';
    }
}
