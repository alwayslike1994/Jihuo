package com.example.alwayslike.jihuo.ui;

/**
 * Created by alwayslike on 2017/5/8.
 */


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.alwayslike.jihuo.util.ActivityCollector;

import java.io.Serializable;

/**
 * 在原有Activity上添加在onCreate中将Activity加入到链表中。 在onDestory中将Activity从链表中移除
 *
 * @author Xingfeng
 */
public class BaseActivity extends AppCompatActivity implements Serializable {
    private boolean cancelable = true;
    public static final int DISABLE_EXPAND = 0x00010000;
    public static final int DISABLE_NOTIFICATION_ICONS = 0x00020000;
    public static final int DISABLE_NOTIFICATION_ALERTS = 0x00040000;
    public static final int DISABLE_NOTIFICATION_TICKER = 0x00080000;
    public static final int DISABLE_SYSTEM_INFO = 0x00100000;
    public static final int DISABLE_HOME = 0x00200000;
    public static final int DISABLE_RECENT = 0x01000000;
    public static final int DISABLE_BACK = 0x00400000;
    public static final int DISABLE_CLOCK = 0x00800000;
    public static final int DISABLE_SEARCH = 0x02000000;
    public static final int DISABLE_NONE = 0x00000000;

    private ProgressDialog progressDialog;

    public void showProgress(String message) {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);

        progressDialog.setMessage(message);
        progressDialog.show();
    }

    /**
     * 判断进度条是否显示
     *
     * @return
     */
    public boolean isShowing() {
        if (progressDialog == null)
            return false;
        return progressDialog.isShowing();
    }

    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ActivityCollector.addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }
}
