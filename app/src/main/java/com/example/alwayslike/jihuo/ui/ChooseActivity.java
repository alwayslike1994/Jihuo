package com.example.alwayslike.jihuo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    public void initViews() {
        PhoneCheckBt = (Button) findViewById(R.id.phonecheck);
        DoubleCheckBt = (Button) findViewById(R.id.doublecheck);
        JihuoBt = (Button) findViewById(R.id.jihuocheck);

    }

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

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1234) {
            SharePreferenceHanler.writePreferences(Constant.Key.HAS_PHONE_CHECK, true);
        }
    }
}
