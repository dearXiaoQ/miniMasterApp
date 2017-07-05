package com.mastergroup.smartcook.module.device;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mastergroup.smartcook.R;

import java.util.ArrayList;
import java.util.List;

public class GizModeListActivity extends GizConfigModuleBaseActivity {

    /** The lv Mode */
    ListView lvMode;

    /** The data */
    List<String> modeList;

    /** The Adapter */
    ModeListAdapter modeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giz_mode_list);

        initData();
        initView();
        initEvent();

    }

    private void initEvent() {
        lvMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                AddDevSettingWifiActivity.modeNum = arg2;
                finish();
            }
        });
    }

    private void initView() {
        lvMode = (ListView) findViewById(R.id.lvMode);

        lvMode.setAdapter(modeListAdapter);// 初始化
    }

    private void initData() {
        modeList = new ArrayList<String>();
        String[] modes = this.getResources().getStringArray(R.array.mode);
        for (String string : modes) {
            modeList.add(string);
        }
        modeListAdapter = new ModeListAdapter(this, modeList);
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

}
