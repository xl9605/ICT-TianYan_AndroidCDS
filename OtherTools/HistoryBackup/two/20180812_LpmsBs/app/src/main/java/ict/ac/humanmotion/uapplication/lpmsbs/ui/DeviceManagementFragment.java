package ict.ac.humanmotion.uapplication.lpmsbs.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import android.view.View.OnClickListener;


import ict.ac.humanmotion.uapplication.lpmsbs.lpms.ImuStatus;
import ict.ac.humanmotion.uapplication.lpmsbs.lpms.LpmsBData;
import ict.ac.humanmotion.uapplication.lpmsbs.lpms.LpmsBThread;



/**
 * Created by Carson_Ho on 16/5/23.
 */
public class DeviceManagementFragment extends Fragment implements OnClickListener {

    Button button_discover,button_connect,button_disconnect;
    BluetoothAdapter btAdapter;
    Handler updateFragmentsHandler = new Handler();
    LpmsBData imuData = new LpmsBData();
    private int updateRate = 25;
    private boolean getImage = true;
    LpmsBThread lpmsB;
    boolean stopPollThread = false;
    final int FRAGMENT_TAG = 0;
    public static final String ARG_SECTION_NUMBER = "section_number";
    View view;
    private static final String TAG = "ConnectionFragment";
    OnConnectListener connectListener;
    String currentLpms = "";
    String currentConnectedLpms;
    final ArrayList<String> dcLpms = new ArrayList<>();
    ArrayAdapter dcAdapter;
    ListView btList;
    boolean firstDc = true;
    final ArrayList<String> connectedDevicesLpms = new ArrayList<>();
    ArrayAdapter connectedDevicesAdapter;
    ListView connectedDevicesList;
    boolean firstConnectedDevice = true;
    TextView loggingStateText;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device == null) return;
                synchronized (dcLpms) {
                    if ((device.getName() != null) && (device.getName().length() > 0)) {
                        if (device.getName().contains("LPMSB2")) {
                            for (String dcLpm : dcLpms) {
                                if (device.getAddress().equals(dcLpm)) return;
                            }
                            if (firstDc) {
                                Log.d(TAG, "onReceive: ACTION_FOUND");
                                dcLpms.clear();
                              //  btList.getChildAt(0).setBackgroundColor(Color.rgb(128, 64, 85));
                                currentLpms = device.getAddress();
                                firstDc = false;
                            }
                            dcLpms.add(device.getAddress());
                            dcAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            }
        }
    };
    private Runnable mUpdateFragmentsTask = new Runnable() {
        public void run() {
            synchronized (imuData) {
            //    updateFragment(imuData, imuStatus);
            }
            updateFragmentsHandler.postDelayed(mUpdateFragmentsTask, updateRate);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.devicemanagement, null);
//-----        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        //initializeViews();
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    10086);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this.getActivity(), "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
            }
        }
       button_discover = (Button)view.findViewById(R.id.button_discover);
       button_discover.setOnClickListener(this);
       button_connect = (Button) view.findViewById(R.id.button_connect);
       button_connect.setOnClickListener(this);
       button_disconnect = (Button) view.findViewById(R.id.button_disconnect);
       button_discover.setOnClickListener(this);
       loggingStateText = view.findViewById(R.id.logging_status);
       prepareDiscoveredDevicesList();
       prepareConnectedDevicesList();
       Log.e("lpms", "Initializing..");
       return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_discover:
                startBtDiscovery();
                break;
            case R.id.button_connect:
                startBtConnect();
                break;
            case R.id.button_disconnect:
               // onDisconnect();
                break;
        }
    } //开始扫描
    public void startBtDiscovery() {
        Toast.makeText(getActivity(), "正在扫描手环设备..", Toast.LENGTH_SHORT).show();
        btAdapter.cancelDiscovery();
        btAdapter.startDiscovery();
    }
    //扫描蓝牙列表
    public void prepareDiscoveredDevicesList() {
        btList = view.findViewById(R.id.list);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        Objects.requireNonNull(getActivity()).registerReceiver(mReceiver, filter);
        dcAdapter = new ArrayAdapter(getActivity(), R.layout.list_view_text_item, dcLpms);
        btList.setAdapter(dcAdapter);
        dcLpms.add("请按下‘寻找设备’来发现手环设备..");
        dcAdapter.notifyDataSetChanged();
        firstDc = true;
        btList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (firstDc) return;
                for (int a = 0; a < parent.getChildCount(); a++)
                    parent.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                view.setBackgroundColor(Color.rgb(128, 64, 85));
                currentLpms = (String) btList.getItemAtPosition(position);
            }
        });
    }
    //连接监听器
    public interface OnConnectListener {
        void onConnect(String address);
        void onDisconnect();
    }

    //开始连接
    void startBtConnect() {
        btAdapter.cancelDiscovery();

        if (btAdapter != null && !currentLpms.isEmpty()) {
            Toast.makeText(getActivity(), "正在连接 " + currentLpms, Toast.LENGTH_SHORT).show();

            connectListener.onConnect(currentLpms);
        }
    }

    ///连接列表
    public void prepareConnectedDevicesList() {
        connectedDevicesList = view.findViewById(R.id.connected_devices_list);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        Objects.requireNonNull(getActivity()).registerReceiver(mReceiver, filter);
        connectedDevicesAdapter = new ArrayAdapter(getActivity(),R.layout.list_view_text_item, connectedDevicesLpms);
        connectedDevicesList.setAdapter(connectedDevicesAdapter);
        connectedDevicesLpms.add("按下‘设备连接’进行手环连接..");
        connectedDevicesAdapter.notifyDataSetChanged();
        firstConnectedDevice = true;

        connectedDevicesList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (firstConnectedDevice) return;

                for (int a = 0; a < parent.getChildCount(); a++) {
                    parent.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(Color.rgb(128, 64, 85));

                String itemValue = (String) connectedDevicesList.getItemAtPosition(position);
              //  ((MainActivity) getActivity()).onSensorSelectionChanged(itemValue);

                currentConnectedLpms = itemValue;
                Log.e("lpms", "[ConnectionFragment] Switched to pos " + position + " value " + itemValue);
            }
        });
    }
    //确认连接
    public void confirmConnected(BluetoothDevice device) {
        synchronized (connectedDevicesLpms) {
            Log.e("lpms", "[ConnectionFragment] Connecion callback to device: " + device.getAddress() + (device.getName()));

            if (device.getName().contains("LPMSB2")) {
                for (String connectedDevicesLpm : connectedDevicesLpms) {
                    if (device.getAddress().equals(connectedDevicesLpm)) {
                        Log.e("lpms", "[ConnectionFragment] Detected double device: " + device.getAddress());
                        return;
                    }
                }
                if (firstConnectedDevice) {
                    connectedDevicesLpms.clear();
                    connectedDevicesList.getChildAt(0).setBackgroundColor(Color.rgb(128, 64, 85));
                    //((MainActivity) Objects.requireNonNull(getActivity())).onSensorSelectionChanged(device.getName());
                    firstConnectedDevice = false;
                }

                connectedDevicesLpms.add(device.getAddress() /* + device.getImuId()*/);
                connectedDevicesAdapter.notifyDataSetChanged();

                for (int a = 0; a < connectedDevicesList.getChildCount(); a++) {
                    connectedDevicesList.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                }
                connectedDevicesList.getChildAt(0).setBackgroundColor(Color.rgb(128, 64, 85));
                currentConnectedLpms = (String) btList.getItemAtPosition(0);
                //((MainActivity) getActivity()).onSensorSelectionChanged(currentConnectedLpms);

                Log.e("lpms", "[ConnectionFragment] After connect selected: " + currentConnectedLpms);
            }
        }
    }

    //断开连接
    void onDisconnect() {
        connectListener.onDisconnect();

        synchronized (connectedDevicesLpms) {
            Log.e("lpms", "[ConnectionFragment] Remove from list: " + currentConnectedLpms);
            if (connectedDevicesLpms.remove(currentConnectedLpms)) {
                connectedDevicesAdapter.notifyDataSetChanged();

                if (connectedDevicesLpms.size() == 0) {
                    connectedDevicesLpms.add("Press connect button to connect to device..");
                    connectedDevicesAdapter.notifyDataSetChanged();

                    firstConnectedDevice = true;

                    connectedDevicesList.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
                } else {
                    for (int a = 0; a < connectedDevicesList.getChildCount(); a++) {
                        connectedDevicesList.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                    }
                    connectedDevicesList.getChildAt(0).setBackgroundColor(Color.rgb(128, 64, 85));
                    currentConnectedLpms = (String) connectedDevicesList.getItemAtPosition(0);
                   // ((MainActivity) Objects.requireNonNull(getActivity())).onSensorSelectionChanged(currentConnectedLpms);

                    Log.e("lpms", "[ConnectionFragment] After disconnect selected: " + currentConnectedLpms);
                }
            }
        }
    }



}

