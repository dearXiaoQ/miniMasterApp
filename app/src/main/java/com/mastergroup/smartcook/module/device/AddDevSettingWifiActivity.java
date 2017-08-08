package com.mastergroup.smartcook.module.device;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
    @Bind(R.id.config_tv)
    TextView configTV;
    @Bind(R.id.cancel_tv)
    TextView cancelTv;
    @Bind(R.id.wifi_tv)
    TextView wifiTv;
    @Bind(R.id.wifi_pwd_et)
    EditText wifiPwdEt;

    private AlertDialog create;

    private ArrayList<ScanResult> wifiList;




    /**
     * 配置用参数
     */
    private String workSSID, workSSIDPsw;

    /**
     * this mContext
     */
    private Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_setting_item);
        ButterKnife.bind(this);
        this.mContext = this;
        initData();
    }


    private void initData() {
        workSSID = spf.getString("workSSID", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //预设workSSID && workSSIDPsw
        if (!TextUtils.isEmpty(workSSID)) {
            wifiTv.setText(mContext.getString(R.string.select_wifi) + workSSID);
            if (checkworkSSIDUsed(workSSID)) {
                if (!TextUtils.isEmpty(spf.getString("workSSIDPsw", ""))) {
                    wifiPwdEt.setText(spf.getString("workSSIDPsw", ""));
                }
            }
        } else {
            wifiTv.setText(NetUtils.getCurentWifiSSID(this));
        }

    }



    @OnClick({R.id.config_tv, R.id.change_wifi_tv, R.id.cancel_tv})
    public void OnClick(View view){
        switch (view.getId()) {
            case R.id.config_tv:

                toAirlinkReady();
                break;


            case R.id.change_wifi_tv:

                showSelectWiifDialog();
                break;

            case R.id.cancel_tv:

                this.finish();
                break;

        }
    }


    private void showSelectWiifDialog() {
        AlertDialog.Builder dia = new AlertDialog.Builder(mContext);
        View mView = View.inflate(mContext, R.layout.alert_gos_wifi_list, null);
        ListView listview = (ListView) mView.findViewById(R.id.wifi_list);
        List<ScanResult> rsList = NetUtils.getCurrentWifiScanResult(this);
        List<String> localList = new ArrayList<String>();
        localList.clear();
        wifiList = new ArrayList<>();
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
                wifiTv.setText(mContext.getString(R.string.select_wifi) + sSID);
                wifiPwdEt.setText("");
                create.dismiss();
            }
        });
        dia.setView(mView);
        create = dia.create();
        create.show();

    }



    private void toAirlinkReady() {

        workSSID = wifiTv.getText().toString().replace(mContext.getString(R.string.select_wifi),"");
        workSSIDPsw = wifiPwdEt.getText().toString();

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
        } else { //跳转到下一步
            spf.edit().putString("workSSID", workSSID).commit();
            spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();

            Intent intent = new Intent(this, AddDevSetDevStateActivity.class);
            startActivity(intent);
            finish();
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


    /** wifi列表Adapter */
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
                view = LayoutInflater.from(mContext)
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
