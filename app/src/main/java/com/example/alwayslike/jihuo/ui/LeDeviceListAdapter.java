package com.example.alwayslike.jihuo.ui;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.alwayslike.jihuo.R;
import com.example.alwayslike.jihuo.bean.iBeaconClass.iBeacon;

import java.util.ArrayList;

/**
 * Created by alwayslike on 2017/5/12.
 */

public class LeDeviceListAdapter extends BaseAdapter {
    // Adapter for holding devices found through scanning.

    private ArrayList<iBeacon> mLeDevices;
    private LayoutInflater mInflator;
    private Activity mContext;

    public LeDeviceListAdapter(Activity c) {
        super();
        mContext = c;
        mLeDevices = new ArrayList<iBeacon>();
        mInflator = mContext.getLayoutInflater();
    }

    public void addDevice(iBeacon device) {
        if (device == null)
            return;

        for (int i = 0; i < mLeDevices.size(); i++) {
            String btAddress = mLeDevices.get(i).bluetoothAddress;
            if (btAddress.equals(device.bluetoothAddress)) {
                mLeDevices.add(i + 1, device);
                mLeDevices.remove(i);
                return;
            }
        }
        mLeDevices.add(device);

    }

    public iBeacon getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view
                    .findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view
                    .findViewById(R.id.device_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        iBeacon device = mLeDevices.get(i);
        final String deviceName = device.name;
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText(R.string.unknown_device);
        if (device.isIbeacon) {
            viewHolder.deviceName.append(" [iBeaconClass]");
        }

        viewHolder.deviceAddress.setText(device.bluetoothAddress);
        if (i % 2 == 0)// set color
        {
            view.setBackgroundColor(Color.argb(25, 255, 0, 0));
        } else {
            view.setBackgroundColor(Color.argb(25, 0, 255, 0));
        }
        return view;
    }

    class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }
}
