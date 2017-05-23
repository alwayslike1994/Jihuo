package com.example.alwayslike.jihuo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alwayslike.jihuo.R;
import com.example.alwayslike.jihuo.bean.ActivateReqBean;
import com.example.alwayslike.jihuo.bean.ActivateRspBean;
import com.example.alwayslike.jihuo.http.HttpClient;
import com.example.alwayslike.jihuo.http.ParamsHelper;
import com.example.alwayslike.jihuo.util.Constant;
import com.example.alwayslike.jihuo.util.MessageHand;
import com.example.alwayslike.jihuo.util.SharePreferenceHanler;
import com.google.gson.Gson;

/**
 * Created by alwayslike on 2017/5/22.
 */

public class ActivateActivity extends BaseActivity {
    private TextView textView;
    private ActivateRspBean activateRspBean;
    private String activate_response;
    private String TAG = "Activate_Response";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        initView();
        sendToOBU();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_GETRESPONSE_BROADCAST");
        registerReceiver(receiver, intentFilter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("ACTION_GETRESPONSE_BROADCAST")) {
                activate_response = intent.getStringExtra("TC_response");
                Log.i(TAG, activate_response);
                sendToServer();
            } else {
                mhandler.sendEmptyMessage(2);
            }
        }
    };
    public Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(ActivateActivity.this, "手机激活成功", Toast.LENGTH_LONG).show();
                    textView.setText("手机激活成功");
                    setResult(RESULT_OK);
                    finish();
                    break;
                case 0:
                    Toast.makeText(ActivateActivity.this, "手机激活失败", Toast.LENGTH_LONG).show();
                    setResult(RESULT_CANCELED);
                    break;
                case 2:
                    Toast.makeText(ActivateActivity.this, "系统错误", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(ActivateActivity.this, "获取参数错误", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        unregisterReceiver(receiver);
        setResult(RESULT_CANCELED);
        finish();
    }

    private void initView() {
        textView = (TextView) findViewById(R.id.activate_status);
        textView.setText("正在进行激活过程");
    }

    private void sendToOBU() {
        String request = SharePreferenceHanler.readString(Constant.Key.ACTIVATE);
        byte[] bytes = new MessageHand().strToBytes(request, (byte) 0x31);
        ConnectToOBUAcitivity.Send_Bytes(bytes);
    }

    private void sendToServer() {
        final ActivateReqBean activateReqBean = new ActivateReqBean("activate", activate_response);
        new HttpClient().sendRequest(ParamsHelper.getActivateParams(activateReqBean), new HttpClient.HttpListener() {
            @Override
            public void onSuccess(String response) {
                activateRspBean = new Gson().fromJson(response, ActivateRspBean.class);
                if (activateRspBean.getStatus().equals("1")) {
                    mhandler.sendEmptyMessage(1);
                } else {
                    mhandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFail(Exception e) {
                mhandler.sendEmptyMessage(3);
            }
        });
    }
}
