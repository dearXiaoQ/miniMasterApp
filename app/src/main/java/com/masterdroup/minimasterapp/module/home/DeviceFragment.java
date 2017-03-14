package com.masterdroup.minimasterapp.module.home;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.view.SlideListView2;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 11473 on 2016/12/20.
 */

public class DeviceFragment extends Fragment {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

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
     * The slv FoundDevices
     */
    private SlideListView2 slvFoundDevices;

    /**
     * The slv OfflineDevices
     */
    private SlideListView2 slvOfflineDevices;

    Context mContext;

    View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_device, container, false);
        ButterKnife.bind(view);
        initView(view);
        mContext = view.getContext();
        mView = view;
        return view;
    }

    void initView(View view) {

        icBoundDevices = view.findViewById(R.id.icBoundDevices);
        icFoundDevices = view.findViewById(R.id.icFoundDevices);
        icOfflineDevices = view.findViewById(R.id.icOfflineDevices);

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

        //设置ActionBar
//        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
//        setHasOptionsMenu(true);//需要额外调用

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.devicelist_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_QR_code:
                Snackbar.make(mView, " action_QR_code ", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_addDevice:
                Snackbar.make(mView, " action_addDevice ", Snackbar.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
