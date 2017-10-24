package com.example.gin.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gin.bluetooth.ADAPTERS.searchDeviceAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class searchDeviceActivity extends AppCompatActivity {

    private ListView mListView;

    private searchDeviceAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;

    private BluetoothAdapter mBluetoothAdapter;

    private ProgressDialog searching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_device);

        searching = ProgressDialog.show(this, "SCANNING FOR DEVICES IN RANGE", "Scanning...", false, false);
        searching.show();

        mListView = (ListView) findViewById(R.id.listViewSearch);
        mListView.setEmptyView(findViewById(R.id.emptyView));

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
        mAdapter = new searchDeviceAdapter(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mSearchReceiver, filter);
    }


    private final BroadcastReceiver mSearchReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(searchDeviceActivity.this, "Paired", Toast.LENGTH_SHORT).show();
                }
                mAdapter.notifyDataSetChanged();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                searching.dismiss();
                Toast.makeText(searchDeviceActivity.this, "NO MORE DEVICES" ,Toast.LENGTH_LONG).show();

                mBluetoothAdapter.cancelDiscovery();

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDeviceList.add(device);
                mAdapter.setData(mDeviceList);
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                mAdapter.setListener(new searchDeviceAdapter.OnPairButtonClickListener() {
                    @Override
                    public void onPairButtonClick(int position) {
                        BluetoothDevice device = mDeviceList.get(position);
                            Toast.makeText(searchDeviceActivity.this, "PAIRING DEVICES",Toast.LENGTH_SHORT).show();
                            pairDevice(device);
                    }
                });
            }
        }
    };
    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        mBluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mSearchReceiver);
        finish();
    }
}
