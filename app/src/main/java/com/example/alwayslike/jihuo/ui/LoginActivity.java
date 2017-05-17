package com.example.alwayslike.jihuo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alwayslike.jihuo.R;
import com.example.alwayslike.jihuo.bean.BaseBean;
import com.example.alwayslike.jihuo.bean.LoginReqBean;
import com.example.alwayslike.jihuo.bean.LoginRspBean;
import com.example.alwayslike.jihuo.http.HttpClient;
import com.example.alwayslike.jihuo.http.ParamsHelper;
import com.example.alwayslike.jihuo.util.Constant;
import com.example.alwayslike.jihuo.util.SharePreferenceHanler;
import com.google.gson.Gson;

/**
 * Created by alwayslike on 2017/5/8.
 */

public class LoginActivity extends BaseActivity {
    private EditText phonenumber;
    private EditText passwd;
    private Button loginBt;

    private String phoneNumber;
    private String passWd;
    private LoginRspBean loginRspBean;
    private String BST5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initListenners();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissProgress();
            switch (msg.what) {
                case 1:
                    BST5 = loginRspBean.getBST5();
                    SharePreferenceHanler.writePreferences(Constant.Key.BST5, BST5);
                    SharePreferenceHanler.writePreferences(Constant.Key.phonenumber, phoneNumber);
                    SharePreferenceHanler.writePreferences(Constant.Key.passwd, passWd);
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG);
                    Intent intent = new Intent(LoginActivity.this, ConnectToOBUAcitivity.class);
                    startActivity(intent);
                    break;
                case 0:
                    Toast.makeText(LoginActivity.this, "手机号或密码错误", Toast.LENGTH_LONG);
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "系统错误", Toast.LENGTH_LONG);
                    break;
                case 3:
                    Toast.makeText(LoginActivity.this, "获取网络参数错误", Toast.LENGTH_LONG);
                    break;
            }
        }
    };

    public void initViews() {
        phonenumber = (EditText) findViewById(R.id.login_workerid);
        passwd = (EditText) findViewById(R.id.login_password);
        loginBt = (Button) findViewById(R.id.login_btn);
        String phonenumberStr = SharePreferenceHanler.readString(Constant.Key.phonenumber);
        if (phonenumberStr != null) {
            phonenumber.setText(phonenumberStr);
            passwd.requestFocus();
        }
    }

    public void initListenners() {
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = phonenumber.getText().toString();
                passWd = passwd.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(LoginActivity.this, "手机号不能为空！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passWd)) {
                    Toast.makeText(LoginActivity.this, "密码不能为空！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgress("正在登录···");
                loginToServer();
            }
        });
    }

    /**
     * 发送登录报文
     */
    private void loginToServer() {
        final LoginReqBean loginReqBean = new LoginReqBean(phoneNumber, passWd);

        new HttpClient().sendRequest(ParamsHelper.getLoginParams(loginReqBean), new HttpClient.HttpListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                    loginRspBean = new Gson().fromJson(baseBean.getData(), LoginRspBean.class);

                    if (loginRspBean != null) {
                        String status = loginRspBean.getStatus();
                        if (status.equals("1")) {
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    }
                } catch (Exception e) {
                    handler.sendEmptyMessage(2);
                }
            }

            @Override
            public void onFail(Exception e) {
                handler.sendEmptyMessage(3);
            }
        });

    }
}
