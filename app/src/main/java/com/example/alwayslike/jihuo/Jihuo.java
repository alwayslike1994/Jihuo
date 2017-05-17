package com.example.alwayslike.jihuo;

import android.app.Application;
import android.content.Context;

import com.example.alwayslike.jihuo.util.Constant;
import com.example.alwayslike.jihuo.util.SharePreferenceHanler;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alwayslike on 2017/5/8.
 */

public class Jihuo extends Application {
    public static Context mContext;
    public static final boolean DEBUG = true;
    public static List<Byte> recBytes = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        setparameter();
    }

    //为了解决签到过一次后每次重装APP就默认该用户已登录。
    public void setparameter() {
        SharePreferenceHanler.writePreferences(Constant.Key.HAS_LOGIN_IN, false);
    }


}
