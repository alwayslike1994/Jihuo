package com.example.alwayslike.jihuo.ui;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alwayslike.jihuo.Jihuo;
import com.example.alwayslike.jihuo.R;
import com.example.alwayslike.jihuo.bean.iBeaconClass;
import com.example.alwayslike.jihuo.bean.iBeaconClass.iBeacon;
import com.example.alwayslike.jihuo.servers.HeartServer;
import com.example.alwayslike.jihuo.util.BluetoothLeClass;
import com.example.alwayslike.jihuo.util.Constant;
import com.example.alwayslike.jihuo.util.MessageHand;
import com.example.alwayslike.jihuo.util.SharePreferenceHanler;

import java.util.List;

/**
 * Created by alwayslike on 2017/5/12.
 */

public class ConnectToOBUAcitivity extends ListActivity {


    private String TAG = "PhoneCheckAc";
    private LeDeviceListAdapter mLeDeviceListAdapter = null;
    // 搜索BLE终端
    private BluetoothAdapter mBluetoothAdapter;
    // 读写BLE终端
    static private BluetoothLeClass mBLE;
    private boolean mScanning;
    private MyThread mythread = null;
    private static final long SCAN_PERIOD = 60 * 60 * 24 * 1;  //最长扫描1天
    public String bluetoothAddress;

    public static final int Connected = 12345;
    public static final int REFRESH = 0x000001;
    public static BluetoothGattCharacteristic target_character = null;
    public static BluetoothLeClass mBluetoothLeClass;

    private Handler mHandler = null;
    public static String UUID_CHAR6 = "0000fff6-0000-1000-8000-00805f9b34fb";
    static BluetoothGattCharacteristic gattCharacteristic_char6 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        getActionBar().setTitle("正在拼命扫描设备中");
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
                    .show();
            finish();
        } else {
            Log.i(TAG, "initialize Bluetooth, has BLE system");
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported,
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Log.i(TAG, "mBluetoothAdapter = " + mBluetoothAdapter);
        }

        // 打开蓝牙
        mBluetoothAdapter.enable();
        Log.i(TAG, "mBluetoothAdapter.enable");

        //初始化蓝牙并连接
        mBLE = new BluetoothLeClass(this);
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            finish();
        }
        Log.i(TAG, "mBLE = e" + mBLE);

        // 发现BLE终端的Service时回调
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);

        // 收到BLE终端数据交互的事件
        mBLE.setOnDataAvailableListener(mOnDataAvailable);

        mHandler = new Handler() {
            int count = 0;

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == REFRESH) {
                    count++;
                }
                if (msg.what == Connected) {
                    SharePreferenceHanler.writePreferences(Constant.Key.VST5, "1");
                    startService(new Intent(ConnectToOBUAcitivity.this, HeartServer.class));
                    Intent intent = new Intent(ConnectToOBUAcitivity.this, ChooseActivity.class);
                    startActivityForResult(intent, 123);
                }
                super.handleMessage(msg);
            }
        };

        new MyThread().start();
    }

    public static void Send_Bytes(byte bytes[]) {
        // 分包发送 每包最大18个字节
        int total_len = bytes.length;
        int cur_pos = 0;
        int i = 0;
        while (cur_pos < total_len) {
            int lenSub = 0;
            if (total_len - cur_pos > 18)
                lenSub = 18;
            else
                lenSub = total_len - cur_pos;

            byte[] bytes_sub = new byte[lenSub];

            for (i = 0; i < lenSub; i++) {
                bytes_sub[i] = bytes[cur_pos + i];
            }
            cur_pos += lenSub;
            ConnectToOBUAcitivity.writeChar6_in_bytes(bytes_sub);

            // 延时 一会
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final iBeacon device = mLeDeviceListAdapter.getDevice(position);
        if (device == null)
            return;

        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }

        Log.i(TAG, "mBluetoothAdapter.enable");
        bluetoothAddress = device.bluetoothAddress;
        boolean bRet = mBLE.connect(device.bluetoothAddress);

        Log.i(TAG, "connect bRet = " + bRet);

        Toast toast = Toast.makeText(getApplicationContext(), "正在连接设备并获取服务中",
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public class MyThread extends Thread {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {

                Message msg = new Message();
//                msg.what = REFRESH;
//                mHandler.sendMessage(msg);
                try {
                    Thread.sleep(200);//线程暂停0.2秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 字节发送
    static public void writeChar6_in_bytes(byte bytes[]) {

        if (gattCharacteristic_char6 != null) {
            boolean bRet = gattCharacteristic_char6.setValue(bytes);
            mBLE.writeCharacteristic(gattCharacteristic_char6);
        }
    }

    public void DisplayStop() {
        if (mythread != null) {
            // mythread.setThread(false);
            // delay(3000);
        }
        Log.i(TAG, "DisplayStop---");
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "---> onResume");
        super.onResume();
//        mBLE.close();
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(this);
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "---> onPause");
        super.onPause();
        // scanLeDevice(false);
        // mLeDeviceListAdapter.clear();
        // mBLE.disconnect();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "---> onStop");
        super.onStop();
        DisplayStop();
        // mBLE.close();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "---> onDestroy");
        super.onDestroy();


        Log.e(TAG, "start onDestroy~~~");
        scanLeDevice(false);
        mBLE.disconnect();
        mBLE.close();
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }


    // 设备扫描回调
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {

            final iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi,
                    scanRecord);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(ibeacon);

                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };
    /**
     * 搜索到BLE终端服务的事件
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {

        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattServices(mBLE.getSupportedGattServices());

            mHandler.sendEmptyMessage(Connected);
        }

    };

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;


        for (BluetoothGattService gattService : gattServices) {

            List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                    .getCharacteristics();
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {

                if (gattCharacteristic.getUuid().toString().equals(UUID_CHAR6)) {
                    // 把char1 保存起来�?以方便后面读写数据时使用
                    gattCharacteristic_char6 = gattCharacteristic;
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                    Log.i(TAG, "+++++++++UUID_CHAR6");
                }
            }
        }


    }

    /**
     * 收到BLE终端数据交互的事件
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] bytes = characteristic.getValue();
            int Length = Jihuo.recBytes.size();
            StringBuilder s = new StringBuilder();
            Byte b = new MessageHand().byteToStr(s, bytes);
            switch (b) {
                case (byte) 0x02:
                    SharePreferenceHanler.writePreferences(Constant.Key.VST5, s.toString());
                    break;
                case (byte) 0x32:
                    SharePreferenceHanler.writePreferences(Constant.Key.TC_response,s.toString());
                    break;

            }

        }
    };
}
