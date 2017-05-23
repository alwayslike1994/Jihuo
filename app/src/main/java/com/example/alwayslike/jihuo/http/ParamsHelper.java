package com.example.alwayslike.jihuo.http;

import com.example.alwayslike.jihuo.bean.ActivateReqBean;
import com.example.alwayslike.jihuo.bean.LoginReqBean;
import com.example.alwayslike.jihuo.bean.VST5ReqBean;
import com.google.gson.Gson;

/**
 * Created by alwayslike on 2017/5/9.
 */

public class ParamsHelper {
    /**
     * 获取登录参数
     */
    public static String getLoginParams(LoginReqBean loginReqBean) {
        StringBuilder result = new StringBuilder();

        try {
            String json = new Gson().toJson(loginReqBean);
            result.append(json);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    /**
     * 获取VST5参数
     */
    public static String getVST5Params(VST5ReqBean vst5ReqBean) {
        StringBuilder result = new StringBuilder();

        try {
            String json = new Gson().toJson(vst5ReqBean);
            result.append(json);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    /**
     * 获取激活状态请求
     */
    public static String getActivateParams(ActivateReqBean activateReqBean) {
        StringBuilder result = new StringBuilder();

        try {

            String json = new Gson().toJson(activateReqBean);
            result.append(json);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
