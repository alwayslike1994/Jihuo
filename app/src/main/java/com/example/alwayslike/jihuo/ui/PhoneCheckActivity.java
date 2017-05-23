package com.example.alwayslike.jihuo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alwayslike.jihuo.R;
import com.example.alwayslike.jihuo.bean.VST5ReqBean;
import com.example.alwayslike.jihuo.bean.VST5RspBean;
import com.example.alwayslike.jihuo.http.HttpClient;
import com.example.alwayslike.jihuo.http.ParamsHelper;
import com.example.alwayslike.jihuo.util.Constant;
import com.example.alwayslike.jihuo.util.MessageHand;
import com.example.alwayslike.jihuo.util.SharePreferenceHanler;
import com.google.gson.Gson;

/**
 * Created by alwayslike on 2017/5/16.
 */

public class PhoneCheckActivity extends BaseActivity {

    private TextView textView;
    static String SendString = "0123";
    private VST5RspBean vst5RspBean;
    private String TAG = "VST5";
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonecheck);
        initView();

        sendDataToOBU();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_GETBYTES_BROADCAST");
        registerReceiver(receiver, intentFilter);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action .equals("ACTION_GETBYTES_BROADCAST") ) {

                str = intent.getStringExtra("VST5");
                Log.i(TAG, str);
                sendDataToServer();

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
                    SharePreferenceHanler.writePreferences(Constant.Key.ACTIVATE, vst5RspBean.getActivite());
                    Toast.makeText(PhoneCheckActivity.this, "手机认证成功", Toast.LENGTH_LONG).show();
                    textView.setText("手机认证成功");
                    setResult(RESULT_OK);
                    finish();
                    break;
                case 0:
                    Toast.makeText(PhoneCheckActivity.this, "手机认证失败", Toast.LENGTH_LONG).show();

                    break;
                case 2:
                    Toast.makeText(PhoneCheckActivity.this, "系统错误", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(PhoneCheckActivity.this, "获取参数错误", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            mhandler.sendEmptyMessage(2);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                break;
        }
        return true;

    }

    public void initView() {

        textView = (TextView) findViewById(R.id.phone_check_status);
        textView.setText("正在进行手机认证");
    }

    @Override
    public void onBackPressed() {

        unregisterReceiver(receiver);
        setResult(RESULT_CANCELED);
        finish();
    }


    public void sendDataToOBU() {
        byte[] bytes = new MessageHand().strToBytes(SharePreferenceHanler.readString(Constant.Key.BST5), (byte) 0x01);
        ConnectToOBUAcitivity.Send_Bytes(bytes);
    }


    public void sendDataToServer() {


        final VST5ReqBean vst5ReqBean = new VST5ReqBean("verify", str);
        new HttpClient().sendRequest(ParamsHelper.getVST5Params(vst5ReqBean), new HttpClient.HttpListener() {
            @Override
            public void onSuccess(String response) {

                vst5RspBean = new Gson().fromJson(response, VST5RspBean.class);
                if (vst5RspBean.getStatus() == "1") {
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
