package com.mastergroup.smartcook.module.home;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.module.device.AddDevSelectTypeActivity;
import com.mastergroup.smartcook.module.device.AddDevSettingWifiActivity;
import com.mastergroup.smartcook.module.device.DeviceControlActivity;
import com.mastergroup.smartcook.module.device.DeviceListActivity;
import com.mastergroup.smartcook.module.device.InductionTestActivity;
import com.mastergroup.smartcook.util.NetUtils;
import com.mastergroup.smartcook.util.Utils;
import com.mastergroup.smartcook.view.GosDeviceListAdapter;
import com.mastergroup.smartcook.view.GosMessageHandler;
import com.mastergroup.smartcook.view.SlideListView2;
import com.mastergroup.smartcook.view.VerticalSwipeRefreshLayout;
import com.yuyh.library.imgsel.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by 11473 on 2016/12/20.
 */

public class DeviceFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    /**
     * The sv ListGroup
     */
    private ScrollView svListGroup;
    /**
     * The ll NoDevice
     */
    private ScrollView llNoDevice;

    /**
     * The img NoDevice
     */
    private ImageView imgNoDevice;

    /**
     * The btn NoDevice
     */
    private Button btnNoDevice;
    /**
     * The ic BoundDevices
     */
    private View icBoundDevices;

    /**
     * The ic FoundDevices
     */
    private View icFoundDevices;

    /**
     * The ic OfflineDevices
     */
    private View icOfflineDevices;

    /**
     *  顶部菜单按钮
     */
    private Button moreBtn;

    /**
     * The tv BoundDevicesListTitle
     */
    private TextView tvBoundDevicesListTitle;

    /**
     * The tv FoundDevicesListTitle
     */
    private TextView tvFoundDevicesListTitle;

    /**
     * The tv OfflineDevicesListTitle
     */
    private TextView tvOfflineDevicesListTitle;

    /**
     * The ll NoBoundDevices
     */
    private LinearLayout llNoBoundDevices;

    /**
     * The ll NoFoundDevices
     */
    private LinearLayout llNoFoundDevices;

    /**
     * The ll NoOfflineDevices
     */
    private LinearLayout llNoOfflineDevices;

    /**
     * The slv BoundDevices
     */
    private SlideListView2 slvBoundDevices;

    /**
     * s
     * The slv FoundDevices
     */
    private SlideListView2 slvFoundDevices;

    /**
     * The slv OfflineDevices
     */
    private SlideListView2 slvOfflineDevices;

    /**
     * 适配器
     */
    GosDeviceListAdapter myadapter;

    /**
     * 设备列表分类
     */
    List<GizWifiDevice> boundDevicesList, foundDevicesList, offlineDevicesList;


    private VerticalSwipeRefreshLayout mSwipeLayout, mSwipeLayout1;
    View mView;

    List<GizWifiDevice> deviceslist = new ArrayList<>();

    /**
     * 判断用户登录状态 0：未登录 1：实名用户登录 2：匿名用户登录 3：匿名用户登录中 4：匿名用户登录中断
     */
    public static int loginStatus;

    /**
     * 获取设备列表
     */
    protected static final int GETLIST = 0;

    /**
     * 刷新设备列表
     */
    protected static final int UPDATALIST = 1;

    /**
     * 订阅成功前往控制页面
     */
    protected static final int TOCONTROL = 2;

    /**
     * 通知
     */
    protected static final int TOAST = 3;

    /**
     * 设备绑定
     */
    protected static final int BOUND = 9;

    /**
     * 设备解绑
     */
    protected static final int UNBOUND = 99;

    /**
     * 新设备提醒
     */
    protected static final int SHOWDIALOG = 999;

    private static final int PULL_TO_REFRESH = 888;

    String softssid, uid, token;
    /**
     * 与APP绑定的设备的ProductKey
     */
    private List<String> ProductKeyList;

    /**
     * 设备热点名称列表
     */
    ArrayList<String> softNameList = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_device, container, false);
        ButterKnife.bind(view);
        mView = view;


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        initData();
        initView();
        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Utils.isLogin())
            return;
        GizDeviceSharing.setListener(new GizDeviceSharingListener() {

            @Override
            public void didCheckDeviceSharingInfoByQRCode(GizWifiErrorCode result, String userName, String productName,
                                                          String deviceAlias, String expiredAt) {
                // TODO Auto-generated method stub
                super.didCheckDeviceSharingInfoByQRCode(result, userName, productName, deviceAlias, expiredAt);

                progressDialogCancel();
                int errorcode = result.ordinal();

                if (8041 <= errorcode && errorcode <= 8050 || errorcode == 8308) {
                    Toast.makeText(mView.getContext(), getResources().getString(R.string.sorry), Toast.LENGTH_SHORT).show();
                } else if (errorcode != 0) {
                    Toast.makeText(mView.getContext(), getResources().getString(R.string.verysorry), Toast.LENGTH_SHORT).show();
                }
            }

        });
        deviceslist = GizWifiSDK.sharedInstance().getDeviceList();
        UpdateUI();
        // TODO GosMessageHandler.getSingleInstance().SetHandler(handler);
        if (boundMessage.size() != 0) {
            progressDialog.show();
            if (boundMessage.size() == 2) {
                GizWifiSDK.sharedInstance().bindDevice(uid, token, boundMessage.get(0), boundMessage.get(1), null);
            } else if (boundMessage.size() == 1) {
                GizWifiSDK.sharedInstance().bindDeviceByQRCode(uid, token, boundMessage.get(0));
            } else if (boundMessage.size() == 3) {

                GizDeviceSharing.checkDeviceSharingInfoByQRCode(App.spUtils.getString("Token", ""), boundMessage.get(2));
            } else {
                Log.i("Apptest", "ListSize:" + boundMessage.size());
            }
        }
    }

    void initView() {
        mView.findViewById(R.id.iv_z).setVisibility(View.GONE);
        svListGroup = (ScrollView) mView.findViewById(R.id.svListGroup);
        llNoDevice = (ScrollView) mView.findViewById(R.id.llNoDevice);
        imgNoDevice = (ImageView) mView.findViewById(R.id.imgNoDevice);
        btnNoDevice = (Button) mView.findViewById(R.id.btnNoDevice);
        moreBtn = (Button) mView.findViewById(R.id.iv_more);
        moreBtn.setOnClickListener(this);


        icBoundDevices = mView.findViewById(R.id.icBoundDevices);
        icFoundDevices = mView.findViewById(R.id.icFoundDevices);
        icOfflineDevices = mView.findViewById(R.id.icOfflineDevices);

        slvBoundDevices = (SlideListView2) icBoundDevices.findViewById(R.id.slideListView1);
        slvFoundDevices = (SlideListView2) icFoundDevices.findViewById(R.id.slideListView1);
        slvOfflineDevices = (SlideListView2) icOfflineDevices.findViewById(R.id.slideListView1);

        llNoBoundDevices = (LinearLayout) icBoundDevices.findViewById(R.id.llHaveNotDevice);
        llNoFoundDevices = (LinearLayout) icFoundDevices.findViewById(R.id.llHaveNotDevice);
        llNoOfflineDevices = (LinearLayout) icOfflineDevices.findViewById(R.id.llHaveNotDevice);

        tvBoundDevicesListTitle = (TextView) icBoundDevices.findViewById(R.id.tvListViewTitle);
        tvFoundDevicesListTitle = (TextView) icFoundDevices.findViewById(R.id.tvListViewTitle);
        tvOfflineDevicesListTitle = (TextView) icOfflineDevices.findViewById(R.id.tvListViewTitle);

        String boundDevicesListTitle = (String) getText(R.string.bound_divices);
        tvBoundDevicesListTitle.setText(boundDevicesListTitle);
        String foundDevicesListTitle = (String) getText(R.string.found_devices);
        tvFoundDevicesListTitle.setText(foundDevicesListTitle);
        String offlineDevicesListTitle = (String) getText(R.string.offline_devices);
        tvOfflineDevicesListTitle.setText(offlineDevicesListTitle);


        // 下拉刷新

        mSwipeLayout = (VerticalSwipeRefreshLayout) mView.findViewById(R.id.id_swipe_ly);

        mSwipeLayout.setOnRefreshListener(this);
//        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mSwipeLayout1 = (VerticalSwipeRefreshLayout) mView.findViewById(R.id.id_swipe_ly1);
        mSwipeLayout1.setOnRefreshListener(this);
//        mSwipeLayout1.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        // 初始化
        setProgressDialog();
    }

    private void initEvent() {

        imgNoDevice.setOnClickListener(this);
        btnNoDevice.setOnClickListener(this);

        slvFoundDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                progressDialog.show();
                slvFoundDevices.setEnabled(false);
                slvFoundDevices.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slvFoundDevices.setEnabled(true);
                    }
                }, 3000);
                GizWifiDevice device = foundDevicesList.get(position);
                device.setListener(gizWifiDeviceListener);

                String productKey = device.getProductKey();

                //订阅设备
                device.setSubscribe(productKey, true);

            }
        });

        slvBoundDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//                progressDialog.show();
//                slvBoundDevices.setEnabled(false);
//                slvBoundDevices.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        slvBoundDevices.setEnabled(true);
//                    }
//                }, 3000);
//                GizWifiDevice device = boundDevicesList.get(position);
//                device.setListener(gizWifiDeviceListener);
//                String productKey = device.getProductKey();
//
//                //订阅设备
//                device.setSubscribe(productKey, true);

            }
        });

        slvBoundDevices.initSlideMode(SlideListView2.MOD_RIGHT);
        slvOfflineDevices.initSlideMode(SlideListView2.MOD_RIGHT);
    }


    private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

        public void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> list) {
            // 提示错误原因
            if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                LogUtils.e("GizWifiSDK", "result: " + result.name());
            } else {
                // 显示设备列表
                LogUtils.d("GizWifiSDK", "discovered deviceList: " + list);
                deviceslist = list;
                UpdateUI();
            }

        }

        /** 用于设备解绑 */
        public void didUnbindDevice(GizWifiErrorCode result, String did) {
            progressDialogCancel();
        }

        /** 用于设备绑定 */
        public void didBindDevice(GizWifiErrorCode result, String did) {
            //progressDialogCancel();
            if(result == GizWifiErrorCode.GIZ_SDK_SUCCESS) { //绑定成功
                Log.i("GizDev", " 绑定设备成功！");
            } else {
                Log.i("GizDev", " 绑定设备失败！ ");
            }
        }

        /** 用于设备绑定（旧） */
        public void didBindDevice(int error, String errorMessage, String did) {
            progressDialogCancel();
        }


        /** 用于绑定推送 */
        public void didChannelIDBind(GizWifiErrorCode result) {
            progressDialogCancel();
        }

    };


    /**
     * 设备监听
     */
    protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        @Override
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            progressDialog.cancel();
            Message msg = new Message();
            if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
                msg.what = TOCONTROL;
                msg.obj = device;
            } else {
                if (device.isBind()) {
                    msg.what = TOAST;
                    // String setSubscribeFail = (String)
                    // getText(R.string.setsubscribe_failed);
                    msg.obj = result;// setSubscribeFail + "\n" + arg0;
                }
            }
            handler.sendMessage(msg);
        }

    };

    public static List<String> boundMessage;

    void initData() {
        boundMessage = new ArrayList<String>();
//        ProductKeyList = GosDeploy.setProductKeyList();

        try {
            uid = App.spUtils.getString(App.mContext.getString(R.string.giz_uid));
            token = App.spUtils.getString(App.mContext.getString(R.string.giz_token));
            if (uid.isEmpty() && token.isEmpty()) {
                loginStatus = 0;
            }

            GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
            handler.sendEmptyMessage(GETLIST);//发送获取设备列表请求
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void UpdateUI() {

        boundDevicesList = new ArrayList<GizWifiDevice>();
        foundDevicesList = new ArrayList<GizWifiDevice>();
        offlineDevicesList = new ArrayList<GizWifiDevice>();

        for (GizWifiDevice gizWifiDevice : deviceslist) {
            if (GizWifiDeviceNetStatus.GizDeviceOnline == gizWifiDevice.getNetStatus()
                    || GizWifiDeviceNetStatus.GizDeviceControlled == gizWifiDevice.getNetStatus()) {
                if (gizWifiDevice.isBind()) {
                    boundDevicesList.add(gizWifiDevice);
                } else {
                    foundDevicesList.add(gizWifiDevice);
                }
            } else {
                offlineDevicesList.add(gizWifiDevice);
            }
        }

        if (boundDevicesList.isEmpty()) {
            slvBoundDevices.setVisibility(View.GONE);
            llNoBoundDevices.setVisibility(View.VISIBLE);
        } else {
            myadapter = new GosDeviceListAdapter(mView.getContext(), boundDevicesList);
            myadapter.setHandler(handler);
            slvBoundDevices.setAdapter(myadapter);
            llNoBoundDevices.setVisibility(View.GONE);
            slvBoundDevices.setVisibility(View.VISIBLE);
        }

        if (foundDevicesList.isEmpty()) {
            slvFoundDevices.setVisibility(View.GONE);
            llNoFoundDevices.setVisibility(View.VISIBLE);
        } else {
            myadapter = new GosDeviceListAdapter(mView.getContext(), foundDevicesList);
            slvFoundDevices.setAdapter(myadapter);
            llNoFoundDevices.setVisibility(View.GONE);
            slvFoundDevices.setVisibility(View.VISIBLE);
        }

        if (offlineDevicesList.isEmpty()) {
            slvOfflineDevices.setVisibility(View.GONE);
            llNoOfflineDevices.setVisibility(View.VISIBLE);
        } else {
            myadapter = new GosDeviceListAdapter(mView.getContext(), offlineDevicesList);
            myadapter.setHandler(handler);
            slvOfflineDevices.setAdapter(myadapter);
            llNoOfflineDevices.setVisibility(View.GONE);
            slvOfflineDevices.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 等待框
     */
    public ProgressDialog progressDialog;

    Handler handler = new Handler() {
        private AlertDialog myDialog;
        private TextView dialog_name;

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETLIST:

                    if (!uid.isEmpty() && !token.isEmpty()) {
                        GizWifiSDK.sharedInstance().getBoundDevices(uid, token, ProductKeyList);
                    }

                    break;

                case UPDATALIST:
                    progressDialogCancel();
                    UpdateUI();
                    break;

                case BOUND:

                    break;

                case UNBOUND:
                    progressDialog.show();
                    GizWifiSDK.sharedInstance().unbindDevice(uid, token, msg.obj.toString());
                    break;

                case TOCONTROL:     //本来是注释的
                    Intent intent = new Intent(mView.getContext(), DeviceControlActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("GizWifiDevice", (GizWifiDevice) msg.obj);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    break;

                case TOAST:

                    Toast.makeText(mView.getContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;

                case PULL_TO_REFRESH:
                    handler.sendEmptyMessage(GETLIST);
                    mSwipeLayout.setRefreshing(false);
                    mSwipeLayout1.setRefreshing(false);

                    break;

                case SHOWDIALOG:

                    if (!softNameList.toString()
                            .contains(GosMessageHandler.getSingleInstance().getNewDeviceList().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext());
                        View view = View.inflate(mView.getContext(), R.layout.alert_gos_new_device, null);
                        Button diss = (Button) view.findViewById(R.id.diss);
                        Button ok = (Button) view.findViewById(R.id.ok);
                        dialog_name = (TextView) view.findViewById(R.id.dialog_name);
                        String foundOneDevice, foundManyDevices;
                        foundOneDevice = (String) getText(R.string.not_text);
                        foundManyDevices = (String) getText(R.string.found_many_devices);
                        if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() < 1) {
                            return;
                        }
                        if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() == 1) {
                            String ssid = GosMessageHandler.getSingleInstance().getNewDeviceList().get(0);
                            if (!TextUtils.isEmpty(ssid)
                                    && ssid.equalsIgnoreCase(NetUtils.getCurentWifiSSID(mView.getContext()))) {
                                return;
                            }
                            if (softNameList.toString().contains(ssid)) {
                                return;
                            }
                            softNameList.add(ssid);
                            dialog_name.setText(ssid + foundOneDevice);
                            softssid = ssid;
                        } else {
                            for (String s : GosMessageHandler.getSingleInstance().getNewDeviceList()) {
                                if (!softNameList.toString().contains(s)) {
                                    softNameList.add(s);
                                }
                            }
                            dialog_name.setText(foundManyDevices);
                        }
                        myDialog = builder.create();
                        Window window = myDialog.getWindow();
                        myDialog.setView(view);
                        myDialog.show();
                        window.setGravity(Gravity.BOTTOM);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() == 1) {
                                    Intent intent = new Intent(mView.getContext(),
                                            DeviceListActivity.class);
                                    intent.putExtra("softssid", softssid);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(mView.getContext(),
                                            DeviceListActivity.class);
                                    startActivity(intent);
                                }
                                myDialog.cancel();
                            }
                        });
                        diss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.cancel();
                            }
                        });
                    }
                    break;
            }
        }


    };

    /**
     * 设置ProgressDialog
     */
    public void setProgressDialog() {
        progressDialog = new ProgressDialog(mView.getContext());
        String loadingText = getString(R.string.loadingtext);
        progressDialog.setMessage(loadingText);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(PULL_TO_REFRESH, Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgNoDevice:
            case R.id.btnNoDevice:
                if (!NetUtils.checkNetwork(mView.getContext())) {
                    Toast.makeText(mView.getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(mView.getContext(), DeviceListActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_more:
                startActivity(new Intent(moreBtn.getContext(), AddDevSelectTypeActivity.class));

                break;

            default:
                break;
        }
    }

    public void progressDialogCancel() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();

    }
}
