package com.mastergroup.smartcook.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.module.welcomeModule.WelcomeActivity;
import com.mastergroup.smartcook.util.DebugUtils;
import com.mastergroup.smartcook.util.Utils;
import com.mastergroup.smartcook.view.TipsDialog;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.bottomBar)
    BottomBar bottomBar;
    @Bind(R.id.fl_content)
    FrameLayout flContent;

    MenuFragment menuFragment;
    DeviceFragment deviceFragment;
    UserFragment userFragment;
    ShopFragment shopFragment;

    String menuFragment_tag = "tag1", deviceFragment_tag = "tag2", shopFragment_tag = "tag3", userFragment_tag = "tag4";

    Toast exitToast;
    View toastView;

    public static Context mContext;


    public static long ON_CRATE_TIME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mContext = this;
        init();
        initXFyun();

        Log.i("myTime", " 时间差 = " + (HomeActivity.ON_CRATE_TIME - System.currentTimeMillis()));
    }


    private void initXFyun() {
        // 将“12345678”替换成您申请的APPID，申请地址：http://open.voicecloud.cn
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID +"=5992afa0");
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {

        toastView = LayoutInflater.from(this).inflate(R.layout.exit_view_item, null);
        exitToast = new Toast(this);
        exitToast.setView(toastView);

        menuFragment = new MenuFragment();

        getFragmentManager().beginTransaction().add(R.id.fl_content, menuFragment, menuFragment_tag).commit();


        if (Utils.isLogin()) {
            deviceFragment = new DeviceFragment();
            userFragment = new UserFragment();
            shopFragment = new ShopFragment();

            getFragmentManager().beginTransaction().add(R.id.fl_content, deviceFragment, deviceFragment_tag).commit();
            getFragmentManager().beginTransaction().add(R.id.fl_content, shopFragment, shopFragment_tag).commit();
            getFragmentManager().beginTransaction().add(R.id.fl_content, userFragment, userFragment_tag).commit();
        }


        switch (Constant.HOME_INDEX) {
            case 0 :

                showMenuFragment();
                break;

            case 1:

                showDeviceFragment();
                break;

            case 2:

                showShopFragment();
                break;

            case  3:

                showUserFragment();
                break;
        }

        bottomBar.selectTabAtPosition(Constant.HOME_INDEX);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                hideAllFragment();
                if (tabId == R.id.tab_food) {
                    DebugUtils.d("TAB", "tab_food");
                    showMenuFragment();
                } else if (tabId == R.id.tab_device) {
                    DebugUtils.d("TAB", "tab_device");
                    if (!Utils.isLogin()) {
                        finish();
                        startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
                    } else
                        showDeviceFragment();
                } else if (tabId == R.id.tab_user) {

                    DebugUtils.d("TAB", "tab_user");
                    if (!Utils.isLogin()) {
                        finish();
                        startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
                    } else
                        showUserFragment();
                } else if(tabId == R.id.tab_shop) {
                    DebugUtils.d("TAB", "tab_shop");
                    if(!Utils.isLogin()) {
                        finish();
                        startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
                    } else
                        showShopFragment();
                }


            }
        });




    }

    void showShopFragment() {
        Constant.HOME_INDEX = 2;
        if (shopFragment == null)
            shopFragment = (ShopFragment) getFragmentManager().findFragmentByTag(shopFragment_tag);
        getFragmentManager().beginTransaction().show(shopFragment).commit();
    }

    void showMenuFragment() {
        Constant.HOME_INDEX = 0;
        if (menuFragment == null)
            menuFragment = (MenuFragment) getFragmentManager().findFragmentByTag(menuFragment_tag);
        getFragmentManager().beginTransaction().show(menuFragment).commit();
    }

    void showDeviceFragment() {
        Constant.HOME_INDEX = 1;
        if (deviceFragment == null)
            deviceFragment = (DeviceFragment) getFragmentManager().findFragmentByTag(deviceFragment_tag);
        getFragmentManager().beginTransaction().show(deviceFragment).commit();
    }

    void showUserFragment() {
        Constant.HOME_INDEX = 3;
        if (App.spUtils.contains(App.mContext.getString(R.string.key_token))) {

            if (userFragment == null)
                userFragment = (UserFragment) getFragmentManager().findFragmentByTag(userFragment_tag);
            getFragmentManager().beginTransaction().show(userFragment).commit();
        } else {
            finish();
        }
    }

    void hideAllFragment() {
        getFragmentManager().beginTransaction().hide(menuFragment).commit();

        if (null != deviceFragment)
            getFragmentManager().beginTransaction().hide(deviceFragment).commit();
        if (null != userFragment)
            getFragmentManager().beginTransaction().hide(userFragment).commit();
        if (null != shopFragment)
            getFragmentManager().beginTransaction().hide(shopFragment).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 666) {
            finish();
        } else if (resultCode == 98765) {
            TipsDialog dialog = new TipsDialog(HomeActivity.this,
                    getResources().getString(R.string.devicedisconnected));

            dialog.show();
        }
    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            //   LogUtils.d("onKeyDown", "按下了退出按钮");
            if (secondTime - firstTime > 3000) {
                firstTime = secondTime;
                exitToast.setDuration(Toast.LENGTH_LONG);
                exitToast.show();
                return true;
            } else {
                System.exit(0);
            }
        }

        return true;
    }
}
