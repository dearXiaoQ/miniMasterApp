package com.mastergroup.smartcook.module.device;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.util.NetUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiaoQ on 2017/5/22.
 */

public class AddDevSettingWifiActivity extends WiFiConfigModuleBaseActivity {

    @Bind(R.id.title_Tv)
    TextView titleTv;
    @Bind(R.id.lock_iv)
    ImageView lockIv;
    @Bind(R.id.pwd_rl)
    RelativeLayout pwdRl;
    @Bind(R.id.change_wifi_tv)
    TextView changeWifiTv;
    @Bind(R.id.view)
    View view;
    @Bind(R.id.config_btn)
    TextView configBtn;
    private ImageView backIv, moreWifiIv;

    private Button visibleBtn;

    private EditText wifiNameEt, pwdEt;

    private AlertDialog create;

    private ArrayList<ScanResult> wifiList;

    /**
     * wifi信息
     */
    public WifiInfo wifiInfo;

    /**
     * The et SSID
     */
    private EditText etSSID;

    /**
     * The ll ChooseMode
     */
    private LinearLayout llChooseMode;

    /**
     * The et Psw
     */
    private EditText etPsw;

    private Button nextBtn;

    /**
     * The cb Laws
     */
    private CheckBox cbLaws;

    /**
     * The img WiFiList
     */
    private ImageView imgWiFiList;

    /**
     * The tv Mode
     */
    private TextView tvMode;

    /**
     * 配置用参数
     */
    private String softSSID, workSSID, workSSIDPsw;

    /**
     * The data
     */
    List<String> modeList;

    /**
     * The Adapter
     */
    ModeListAdapter modeListAdpter;

    /**
     * The modeNum
     */
    static int modeNum = 4;

    /**
     * this Context
     */
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_setting_item);
        this.context = this;

       /* initView();大佬
        initData();
        initEvent();*/

        showDialogView();

    }

    private void showDialogView() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.wifi_setting_item, null);
        Dialog mDialog = new Dialog(context);
        mDialog.setContentView(dialogView);
        mDialog.show();
        mDialog.setCancelable(false);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ButterKnife.bind(mDialog);
        configBtn = (TextView) dialogView.findViewById(R.id.config_btn);
        configBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddDevSetDevStateActivity.class));
            }
        });

        /** 按比例设置宽高 */
        /*DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        android.view.WindowManager.LayoutParams p = mDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.8);    //宽度设置为屏幕的0.5
        p.height = (int) (displayHeight * 0.4);    //宽度设置为屏幕的0.5
        mDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        mDialog.getWindow().setAttributes(p);     //设置生效*/


    }



    private void initEvent() {
      /*  imgWiFiList.setOnClickListener(this);
        llChooseMode.setOnClickListener(this);*/

        cbLaws.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String psw = etPsw.getText().toString();

                if (isChecked) {
                    etPsw.setInputType(0x90);
                } else {
                    etPsw.setInputType(0x81);
                }
                etPsw.setSelection(psw.length());
            }
        });
    }

    private void initData() {
        workSSID = spf.getString("workSSID", "");

        modeList = new ArrayList<String>();
        String[] modes = this.getResources().getStringArray(R.array.mode);
        for (String string : modes) {
            modeList.add(string);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

  /*      tvMode.setText(modeList.get(modeNum));
        //预设workSSID && workSSIDPsw
        if (!TextUtils.isEmpty(workSSID)) {
            etSSID.setText(workSSID);
            if (checkworkSSIDUsed(workSSID)) {
                if (!TextUtils.isEmpty(spf.getString("workSSIDPsw", ""))) {
                    etPsw.setText(spf.getString("workSSIDPsw", ""));
                }
            }
        } else {
            etSSID.setText(NetUtils.getCurentWifiSSID(this));
        }*/

    }


    private void initView() {
        nextBtn = (Button) findViewById(R.id.next_btn);
     //   nextBtn.setOnClickListener(this);

        llChooseMode = (LinearLayout) findViewById(R.id.llChooseMode);
        tvMode = (TextView) findViewById(R.id.tvMode);
        etSSID = (EditText) findViewById(R.id.etSSID);
        etPsw = (EditText) findViewById(R.id.etPsw);
        cbLaws = (CheckBox) findViewById(R.id.cbLaws);
        imgWiFiList = (ImageView) findViewById(R.id.imgWiFiList);


    }


/*
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back_iv:

                break;


            case R.id.next_btn:
                //   startActivity(new Intent(AddDevSettingWifiActivity.this, AddDevSetDevStateActivity.class));

                workSSID = etSSID.getText().toString();
                workSSIDPsw = etPsw.getText().toString();

                if (TextUtils.isEmpty(workSSID)) {
                    Toast.makeText(AddDevSettingWifiActivity.this, R.string.choose_wifi_list_title,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(workSSIDPsw)) {
                    final Dialog dialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    Window window = dialog.getWindow();
                    window.setContentView(R.layout.alert_gos_empty);

                    LinearLayout llNo, llSure;
                    llNo = (LinearLayout) window.findViewById(R.id.llNo);
                    llSure = (LinearLayout) window.findViewById(R.id.llSure);

                    llNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    llSure.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            toAirlinkReady();
                        }
                    });
                } else {
                    toAirlinkReady();
                }

                break;

            case R.id.imgWiFiList:

                AlertDialog.Builder dia = new AlertDialog.Builder(context);
                View mView = View.inflate(context, R.layout.alert_gos_wifi_list, null);
                ListView listview = (ListView) mView.findViewById(R.id.wifi_list);
                List<ScanResult> rsList = NetUtils.getCurrentWifiScanResult(this);
                List<String> localList = new ArrayList<String>();
                localList.clear();
                wifiList = new ArrayList<ScanResult>();
                wifiList.clear();
                for (ScanResult sss : rsList) {

                    if (sss.SSID.contains(SoftAP_Start)) {
                    } else {
                        if (localList.toString().contains(sss.SSID)) {
                        } else {
                            localList.add(sss.SSID);
                            wifiList.add(sss);
                        }
                    }
                }
                WifiListAdapter adapter = new WifiListAdapter(wifiList);
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        ScanResult sResult = wifiList.get(arg2);
                        String sSID = sResult.SSID;
                        etSSID.setText(sSID);
                        etPsw.setText("");
                        create.dismiss();
                    }
                });
                dia.setView(mView);
                create = dia.create();
                create.show();


                break;

            case R.id.llChooseMode:
                Intent intent = new Intent(this, GizModeListActivity.class);
                startActivity(intent);
                break;


            default:
                break;
        }
    }

*/

    private void toAirlinkReady() {
        // TODO
        spf.edit().putString("workSSID", workSSID).commit();
        spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();

        Intent intent = new Intent(this, AddDevSetDevStateActivity.class);
        startActivity(intent);
        finish();
    }

    class ModeListAdapter extends BaseAdapter {

        Context context;
        List<String> modeList;

        public ModeListAdapter(Context context, List<String> modeList) {
            super();
            this.context = context;
            this.modeList = modeList;
        }

        @Override
        public int getCount() {
            return modeList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(context, R.layout.item_gos_mode_list, null);
            }

            TextView tvModeText = (TextView) convertView.findViewById(R.id.tvModeText);

            String modeText = modeList.get(position);
            tvModeText.setText(modeText);

            ImageView ivChoosed = (ImageView) convertView.findViewById(R.id.ivChoosed);
            int i = AddDevSettingWifiActivity.modeNum;
            if (position == i) {
                ivChoosed.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

    }


    // 检查当前使用的WiFi是否曾经用过
    protected boolean checkworkSSIDUsed(String workSSID) {
        if (spf.contains("workSSID")) {
            if (spf.getString("workSSID", "").equals(workSSID)) {
                return true;
            }
        }
        return false;
    }


    class WifiListAdapter extends BaseAdapter {

        ArrayList<ScanResult> xpgList;

        public WifiListAdapter(ArrayList<ScanResult> list) {
            this.xpgList = list;
        }

        @Override
        public int getCount() {
            return xpgList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder;
            if (view == null) {
                view = LayoutInflater.from(context)
                        .inflate(R.layout.item_gos_wifi_list, null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            String ssid = xpgList.get(position).SSID;
            holder.getTextView().setText(ssid);

            return view;
        }

    }


    class Holder {
        View view;

        public Holder(View view) {
            this.view = view;
        }

        TextView textView;

        public TextView getTextView() {
            if (textView == null) {
                textView = (TextView) view.findViewById(R.id.SSID_text);
            }
            return textView;
        }

    }

}
