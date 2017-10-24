package com.example.gin.bluetooth.ADAPTERS;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.gin.bluetooth.R;

import java.util.List;

/**
 * Created by Gin on 10/25/2017.
 */

public class pairedDeviceListAdapter extends BaseAdapter {

    private Context context;

    private List<BluetoothDevice> mData;
    private OnPairButtonClickListener mListener;

    public pairedDeviceListAdapter(Context context){
        this.context = context;
    }

    public void setData(List<BluetoothDevice> data) {
        mData = data;
    }

    public void setListener(OnPairButtonClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getCount() {
        return (mData == null) ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View layout;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.paired_device_item, null);

        Button btnUnpair = (Button) layout.findViewById(R.id.btnUnpair);
        TextView txtDeviceName = (TextView) layout.findViewById(R.id.txtDeviceName);
        TextView txtDeviceAddress = (TextView) layout.findViewById(R.id.txtDeviceAddress);

        BluetoothDevice device	= mData.get(position);

        txtDeviceName.setText(device.getName());
        txtDeviceAddress.setText(device.getAddress());

        btnUnpair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onPairButtonClick(position);
                    mData.remove(position);
                }
            }
        });

        return layout;
    }

    public interface OnPairButtonClickListener {
        public abstract void onPairButtonClick(int position);
    }
}
