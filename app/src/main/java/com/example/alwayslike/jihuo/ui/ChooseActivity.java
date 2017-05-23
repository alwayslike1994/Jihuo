package com.example.alwayslike.jihuo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alwayslike.jihuo.R;
import com.example.alwayslike.jihuo.util.Constant;
import com.example.alwayslike.jihuo.util.SharePreferenceHanler;

/**
 * Created by alwayslike on 2017/5/9.
 */

public class ChooseActivity extends BaseActivity {
    private Button PhoneCheckBt;
    private Button DoubleCheckBt;
    private Button JihuoBt;
    private final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        initViews();
        initListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initViews() {
        PhoneCheckBt = (Button) findViewById(R.id.phonecheck);
        DoubleCheckBt = (Button) findViewById(R.id.doublecheck);
        JihuoBt = (Button) findViewById(R.id.jihuocheck);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(ChooseActivity.this, "请先手机认证", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    break;
            }
        }
    };

    public void initListeners() {
        PhoneCheckBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, PhoneCheckActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        DoubleCheckBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        JihuoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean has_phone_check = SharePreferenceHanler.readBoolean(Constant.Key.HAS_PHONE_CHECK);
                if (has_phone_check) {
                    Intent intent1 = new Intent(ChooseActivity.this, ActivateActivity.class);
                    startActivityForResult(intent1, 12345);
                } else {

                    handler.sendEmptyMessage(0);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1234) {
            SharePreferenceHanler.writePreferences(Constant.Key.HAS_PHONE_CHECK, true);
            PhoneCheckBt.setText("手机认证成功");
            PhoneCheckBt.setTextColor(getResources().getColor(R.color.carport_green));
        }
        if (resultCode == RESULT_OK && requestCode == 12345) {
            JihuoBt.setText("手机激活成功");
            JihuoBt.setTextColor(getResources().getColor(R.color.carport_green));
        }
    }
}
