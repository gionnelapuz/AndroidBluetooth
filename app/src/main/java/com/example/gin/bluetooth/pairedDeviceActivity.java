package com.example.gin.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gin.bluetooth.ADAPTERS.pairedDeviceListAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class pairedDeviceActivity extends AppCompatActivity {

    private TextView txtEmpty;

    private ListView mListView;
    private pairedDeviceListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_device);

        txtEmpty = (TextView) findViewById(R.id.txtEmpty);
        mListView = (ListView) findViewById(R.id.listViewPaired);
        mListView.setEmptyView(findViewById(R.id.emptyView));

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() == 0)
        {
            txtEmpty.setVisibility(View.VISIBLE);
        }

        ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();
        list.addAll(pairedDevices);

        mDeviceList = list;
        mAdapter = new pairedDeviceListAdapter(this);
        mAdapter.setData(mDeviceList);
        mAdapter.setListener(new pairedDeviceListAdapter.OnPairButtonClickListener() {
            @Override
            public void onPairButtonClick(int position) {

                BluetoothDevice device = mDeviceList.get(position);
                unpairDevice(device);

            }
        });
        mListView.setAdapter(mAdapter);

        registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
                if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(pairedDeviceActivity.this, "Unpaired", Toast.LENGTH_SHORT).show();
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        unregisterReceiver(mPairReceiver);
        super.onDestroy();
    }
}
