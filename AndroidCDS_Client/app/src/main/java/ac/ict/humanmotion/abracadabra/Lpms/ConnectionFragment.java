package ac.ict.humanmotion.abracadabra.Lpms;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ac.ict.humanmotion.abracadabra.Bean.Info;
import ac.ict.humanmotion.abracadabra.Bean.Robot;
import ac.ict.humanmotion.abracadabra.Interface.CloudAPI;
import ac.ict.humanmotion.abracadabra.Interface.URLAPI;
import ac.ict.humanmotion.abracadabra.MainActivity;
import ac.ict.humanmotion.abracadabra.R;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConnectionFragment extends MyFragment implements OnClickListener{
    final int FRAGMENT_TAG = 0;
    public static final String ARG_SECTION_NUMBER = "section_number";
    View rootView;
    int updateFlag = 0;
    URLAPI url = new URLAPI();
    private static final String TAG = "ConnectionFragment";

    BluetoothAdapter btAdapter;
  //  OnConnectListener connectListener;
    String currentLpms = "";
    String currentConnectedLpms;
    EditText inputCabinetNumber;

    final ArrayList<String> dcLpms = new ArrayList<>();
    ArrayAdapter dcAdapter;
    ListView btList;
    boolean firstDc = true;

    final ArrayList<String> connectedDevicesLpms = new ArrayList<>();
    ArrayAdapter connectedDevicesAdapter;
    ListView connectedDevicesList;
    boolean firstConnectedDevice = true;

    TextView loggingStateText;
    String device_code="";
    //接收
   /* BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            onDisconnect();

        }
    };*/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lmps_connect_screen, container, false);
        Bundle args = getArguments();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        //注册
      /*  IntentFilter filter = new IntentFilter();
        filter.addAction("com");
        getContext().registerReceiver(broadcastReceiver, filter);*/

        prepareButtons();
        prepareDiscoveredDevicesList();
        prepareConnectedDevicesList();



        startBtDiscovery();
        rootView.setFocusable(true);
        rootView.setFocusableInTouchMode(true);
        //然后在写这个监听器
        rootView.setOnKeyListener(backlistener);
        return rootView;
    }
    private View.OnKeyListener backlistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                //这边判断,如果是back的按键被点击了   就自己拦截实现掉
                if (i == KeyEvent.KEYCODE_BACK) {
                    return true;//表示处理了
                }
            }
            return false;
        }
    };
    void prepareButtons() {

        Button b=rootView.findViewById(R.id.button_start_operate);
        b.setOnClickListener(this);
        // loggingStateText = rootView.findViewById(R.id.logging_status);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start_operate:
                inputCabinetNumber = new EditText(getContext());
                new AlertDialog.Builder(this.getContext()).setTitle("请输入柜子编号：")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(inputCabinetNumber)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件
                                device_code=inputCabinetNumber.getText().toString();
                                updateJson();
                            }
                        }).setNegativeButton("取消",new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {

                              }
                          }).show();

               // updateJson();
              /*  if(updateFlag == 1){
                    new AlertDialog.Builder(this.getContext()).setTitle("警告")//设置对话框标题
                            .setMessage("机器人正在移动，请注意！")//设置显示的内容
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("确定",new  DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 执行点击确定按钮的业务逻辑
                                    startBtConnect();
                                }
                            }).show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);//在按键响应事件中显示此对话框
                    updateFlag = 0;
                }*/
                break;
        }
    }

    //
    void updateJson(){

        Robot robot=new Robot();
        //String now_device = inputCabinetNumber.getText().toString();
        String device = inputCabinetNumber.getText().toString();
       // String device=robot.selectNumber(device_code);

        if(device!=""){


            //存储
            SharedPreferences sharedPreferences=getContext().getSharedPreferences("work",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("device_code",device_code);
            //editor.putString("now_device_code",now_device);
            editor.commit();

            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=new Date(System.currentTimeMillis());
            String time=simpleDateFormat.format(date);
            //假设柜门为"403"---后续需要改动，获得ocr
            // String device_code="403";
            int cmd=0;
            Info info=new Info(0,device,time);

            Gson gson=new Gson();
            String obj=gson.toJson(info);
            Toast.makeText(getContext(),obj,Toast.LENGTH_LONG).show();

            //SharedPreferences sharedPreference = getContext().getSharedPreferences("work", Context.MODE_PRIVATE);
            String name=sharedPreferences.getString("filename","");



            Retrofit retrofit=new Retrofit.Builder().baseUrl(url.getRoboturl()).
                    client(new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).build())
                    .build();
            CloudAPI cloudAPI=retrofit.create(CloudAPI.class);
            final RequestBody requestBody=RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),obj);
            retrofit2.Call<ResponseBody> data=cloudAPI.postJson(name,requestBody);

            //Toast.makeText(getContext(),""+requestBody,Toast.LENGTH_LONG).show();
            data.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        // updateFlag = 1;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String result=jsonObject.getString("result");
                        if(result.equals("success")) {

                            new AlertDialog.Builder(getContext()).setTitle("警告")//设置对话框标题
                                    .setMessage("机器人正在移动，请注意！")//设置显示的内容
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 执行点击确定按钮的业务逻辑
                                            startBtConnect();
                                        }
                                    }).show().getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);//在按键响应事件中显示此对话框


                            //Toast.makeText(getContext(),"result是：" + jsonObject.getString("result") + ",Values是："+jsonObject.getString("value")+"。",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: --ok--" + response.body().string());
                        }
                        else {
                            Toast.makeText(getContext(),"数据传送失败",Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
             /*   String str=response.body().toString();
                Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();*/
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
                }
            });


        }else {
            Toast.makeText(getContext(),"该柜门号无效！",Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public int getMyFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public void setMyFragmentTag(int i) {

    }

    OnConnectListener connectListener;
    public interface OnConnectListener {
        void onConnect(String address);
        void onDisconnect();
    }

    void startBtConnect() {

        btAdapter.cancelDiscovery();

        if (btAdapter != null && !currentLpms.isEmpty()) {
            Toast.makeText(getActivity(), "正在连接 " + currentLpms, Toast.LENGTH_SHORT).show();

            connectListener.onConnect(currentLpms);
        }else {
            Toast.makeText(getActivity(), "未发现手环设备.请开启你的手环设备.." + currentLpms, Toast.LENGTH_SHORT).show();
            startBtDiscovery();
        }
    }


    void onDisconnect() {
        connectListener.onDisconnect();

        synchronized (connectedDevicesLpms) {
            Log.e("lpms", "[ConnectionFragment] Remove from list: " + currentConnectedLpms);
            if (connectedDevicesLpms.remove(currentConnectedLpms)) {
                connectedDevicesAdapter.notifyDataSetChanged();

                if (connectedDevicesLpms.size() == 0) {
                    connectedDevicesLpms.add("按下‘设备连接’按钮来进行手环设备的连接..");
                    connectedDevicesAdapter.notifyDataSetChanged();

                    firstConnectedDevice = true;

                    connectedDevicesList.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
                } else {
                    for (int a = 0; a < connectedDevicesList.getChildCount(); a++) {
                        connectedDevicesList.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                    }
                    connectedDevicesList.getChildAt(0).setBackgroundColor(Color.rgb(128, 64, 85));
                    currentConnectedLpms = (String) connectedDevicesList.getItemAtPosition(0);
                    ((MainActivity) Objects.requireNonNull(getActivity())).onSensorSelectionChanged(currentConnectedLpms);

                    Log.e("lpms", "[ConnectionFragment] After disconnect selected: " + currentConnectedLpms);
                }
            }
        }
    }

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
                                btList.getChildAt(0).setBackgroundColor(Color.rgb(128, 64, 85));
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

    private void prepareDiscoveredDevicesList() {
        btList = rootView.findViewById(R.id.list);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        Objects.requireNonNull(getActivity()).registerReceiver(mReceiver, filter);
        dcAdapter = new ArrayAdapter(getActivity(), R.layout.list_view_text_item, dcLpms);
        btList.setAdapter(dcAdapter);
        dcLpms.add("按下‘寻找设备’按钮来进行扫描手环设备..");
        dcAdapter.notifyDataSetChanged();
        firstDc = true;

        btList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (firstDc) return;

                for (int a = 0; a < parent.getChildCount(); a++) {
                    parent.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(Color.rgb(128, 64, 85));

                currentLpms = (String) btList.getItemAtPosition(position);
            }
        });
    }

    private void prepareConnectedDevicesList() {
        connectedDevicesList = rootView.findViewById(R.id.connected_devices_list);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        Objects.requireNonNull(getActivity()).registerReceiver(mReceiver, filter);
        connectedDevicesAdapter = new ArrayAdapter(getActivity(), R.layout.list_view_text_item, connectedDevicesLpms);
        connectedDevicesList.setAdapter(connectedDevicesAdapter);
        connectedDevicesLpms.add("按下‘设备连接’按钮来进行手环设备的连接..");
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
                ((MainActivity) getActivity()).onSensorSelectionChanged(itemValue);

                currentConnectedLpms = itemValue;
                Log.e("lpms", "[ConnectionFragment] Switched to pos " + position + " value " + itemValue);
            }
        });
    }

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
                    ((MainActivity) Objects.requireNonNull(getActivity())).onSensorSelectionChanged(device.getName());
                    firstConnectedDevice = false;
                }

                connectedDevicesLpms.add(device.getAddress() /* + device.getImuId()*/);
                connectedDevicesAdapter.notifyDataSetChanged();

                for (int a = 0; a < connectedDevicesList.getChildCount(); a++) {
                    connectedDevicesList.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                }
                connectedDevicesList.getChildAt(0).setBackgroundColor(Color.rgb(128, 64, 85));
                currentConnectedLpms = (String) btList.getItemAtPosition(0);
                ((MainActivity) getActivity()).onSensorSelectionChanged(currentConnectedLpms);

                Log.e("lpms", "[ConnectionFragment] After connect selected: " + currentConnectedLpms);
            }
        }
    }

    public void startBtDiscovery() {
        Toast.makeText(getActivity(), "开始扫描设备..", Toast.LENGTH_SHORT).show();

        btAdapter.cancelDiscovery();
        btAdapter.startDiscovery();
        // startBtConnect();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            connectListener = (OnConnectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void updateView(LpmsBData d, ImuStatus s) {
//        Log.d("Connect", d.getAcc()[0] + " " + d.getLinAcc()[0]);
    }
}