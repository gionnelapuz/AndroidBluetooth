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
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button btnEnable, btnDisable;
    private Button btnPair;
    private Button btnScan;
    private Button btnSetVisibility;

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEnable = (Button) findViewById(R.id.btnEnable);
        btnDisable = (Button) findViewById(R.id.btnDisable);
        btnPair = (Button) findViewById(R.id.btnViewPaired);
        btnScan = (Button) findViewById(R.id.btnScan);
        btnSetVisibility = (Button) findViewById(R.id.btnSetVisibility);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled())
        {
            bluetoothIsOn();
        }
        else {
            bluetoothIsOff();
        }

        btnEnable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               Intent ON = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
               startActivityForResult(ON, 0);
                }
            });
        btnDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mBluetoothAdapter.disable();

               bluetoothIsOff();
               Toast.makeText(MainActivity.this, "Bluetooth Disabled", Toast.LENGTH_SHORT).show();
            }
        });
       btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, searchDeviceActivity.class);
                startActivity(intent);
            }
        });
        btnPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, pairedDeviceActivity.class);
                startActivity(intent);
            }
        });
        btnSetVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent visible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(visible, 0);
            }
        });

        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                  bluetoothIsOn();
                  Toast.makeText(MainActivity.this, "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void bluetoothIsOn()
    {
        btnEnable.setVisibility(View.GONE);
        btnDisable.setVisibility(View.VISIBLE);

        btnPair.setEnabled(true);
        btnScan.setEnabled(true);
        btnSetVisibility.setEnabled(true);
    }
    public void bluetoothIsOff()
    {
        btnEnable.setVisibility(View.VISIBLE);
        btnDisable.setVisibility(View.GONE);

        btnPair.setEnabled(false);
        btnScan.setEnabled(false);
        btnSetVisibility.setEnabled(false);
    }

}
