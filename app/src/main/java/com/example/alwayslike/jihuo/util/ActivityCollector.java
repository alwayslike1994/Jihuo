package com.example.alwayslike.jihuo.util;

import android.app.Activity;
import android.content.IntentFilter;

import com.example.alwayslike.jihuo.Jihuo;
//import com.example.alwayslike.jihuo.servers.NetworkChangeReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alwayslike on 2017/5/8.
 */

public class ActivityCollector {private static List<Activity> activities = new ArrayList<Activity>();

//    private static NetworkChangeReceiver mReceiver;

    /**
     * 添加Activity
     *
     * @param activity 要加入的Activity
     */
    public static void addActivity(Activity activity) {
//
//        if (activities.size() == 0) {
//            mReceiver = new NetworkChangeReceiver();
//            IntentFilter intentFilter = new IntentFilter(NetworkChangeReceiver.NETWORK_CHANGE_ACTION);
//            Jihuo.mContext.registerReceiver(mReceiver, intentFilter);
//        }

        activities.add(activity);

    }

    /**
     * 删除Activity
     *
     * @param activity 要删除的Activity
     */
    public static void removeActivity(Activity activity) {

        activities.remove(activity);

    }

//    /**
//     * 将所有的activity finish掉，退出应用程序
//     */
//    public static void exitApp() {
//
//        for (Activity activity : activities) {
//
//            if (!activity.isFinishing())
//                activity.finish();
//
//        }
//
//        activities.clear();
//
//        if (mReceiver != null) {
//            Jihuo.mContext.unregisterReceiver(mReceiver);
//        }
//
//        mReceiver = null;

//        //DSRC模块上电
//        try {
//            CMD_Manager cmd_manager = CMD_Manager.getCMDManager();
//            cmd_manager.Power_on();
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.getInstance().saveException2File(e.toString());
//        }

//        WifiManager wifiManager = (WifiManager) Jicha.mContext.getSystemService(Context.WIFI_SERVICE);
//        if (wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(false);
//        }
//    }

    /**
     * 获得当前界面显示的Activity
     *
     * @return
     */
    public static Activity getCurrentActivity() {
        if (activities.size() > 0)
            return activities.get(activities.size() - 1);
        else
            return null;
    }
}
