package com.example.alwayslike.jihuo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.alwayslike.jihuo.servers.HeartServer;
/**
 * Created by alwayslike on 2017/5/19.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, HeartServer.class);
        context.startService(i);
    }
}