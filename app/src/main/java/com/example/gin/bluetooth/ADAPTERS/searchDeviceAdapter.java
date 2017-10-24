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

public class searchDeviceAdapter extends BaseAdapter {

    private Context context;

    private List<BluetoothDevice> mData;
    private OnPairButtonClickListener mListener;

    public searchDeviceAdapter(Context context)
    {
        this.context = context;
    }

    public void setData(List<BluetoothDevice> data)
    {
        mData = data;
    }

    public void setListener(OnPairButtonClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getCount()
    {
        return (mData == null) ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View layout;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.search_device_item, null);

        Button btnPair = (Button) layout.findViewById(R.id.btnPair);
        TextView txtDeviceName = (TextView) layout.findViewById(R.id.txtDeviceName);
        TextView txtDeviceAddress = (TextView) layout.findViewById(R.id.txtDeviceAddress);
        TextView txtAlreadyPaired = (TextView) layout.findViewById(R.id.txtAlreadyPaired);

        BluetoothDevice device	= mData.get(position);

        txtDeviceName.setText(device.getName());
        txtDeviceAddress.setText(device.getAddress());

        if (device.getBondState() == BluetoothDevice.BOND_BONDED){
            btnPair.setVisibility(View.GONE);
            txtAlreadyPaired.setVisibility(View.VISIBLE);
        }

        btnPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onPairButtonClick(position);
                }
            }
        });

        return layout;
    }

    public interface OnPairButtonClickListener {
        public abstract void onPairButtonClick(int position);
    }
}
