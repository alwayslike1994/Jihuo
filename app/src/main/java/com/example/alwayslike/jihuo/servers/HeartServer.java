package com.example.alwayslike.jihuo.servers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.alwayslike.jihuo.receiver.AlarmReceiver;
import com.example.alwayslike.jihuo.util.Constant;
import com.example.alwayslike.jihuo.util.SharePreferenceHanler;

/**
 * Created by alwayslike on 2017/5/12.
 */

public class HeartServer extends Service {
    private String TAG = "PhoneCheckServer__VST5";
    private PendingIntent heartBeatIntent;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String VST5 = SharePreferenceHanler.readString(Constant.Key.VST5);
                String TC_response = SharePreferenceHanler.readString(Constant.Key.TC_response);

                if (VST5.equals("1") && !TC_response.equals("1")) {
                    Intent intent0 = new Intent();
                    intent0.setAction("ACTION_GETRESPONSE_BROADCAST");
                    intent0.putExtra("TC_response", TC_response);
                    Log.i(TAG, TC_response);
                    SharePreferenceHanler.writePreferences(Constant.Key.TC_response, "1");
                    sendBroadcast(intent0);
                } else {
                    if (!VST5.equals("1")&& TC_response.equals("1")) {
                        Intent intent1 = new Intent();
                        intent1.setAction("ACTION_GETBYTES_BROADCAST");
                        intent1.putExtra("VST5", VST5);
                        Log.i(TAG, VST5);
                        SharePreferenceHanler.writePreferences(Constant.Key.VST5, "1");
                        sendBroadcast(intent1);

                    }
                }

            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int aminute = 5 * 1000; // 这是5秒的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + aminute;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelHeartbeatTimer();
    }

    /**
     * 取消心跳
     */
    private void cancelHeartbeatTimer() {

        if (heartBeatIntent == null) {
            return;
        }
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(heartBeatIntent);
    }

}
