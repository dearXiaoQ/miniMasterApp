package com.mastergroup.smartcook.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.module.welcomeModule.WelcomeActivity;
import com.mastergroup.smartcook.util.DebugUtils;
import com.mastergroup.smartcook.util.Utils;
import com.mastergroup.smartcook.view.TipsDialog;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.yuyh.library.imgsel.utils.LogUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();
        showMenuFragment();
    }

/*    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try {
            Object service = getSystemService("statusbar");
            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
            Method test = statusbarManager.getMethod("collapse");
            test.invoke(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
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
        if (shopFragment == null)
            shopFragment = (ShopFragment) getFragmentManager().findFragmentByTag(shopFragment_tag);
        getFragmentManager().beginTransaction().show(shopFragment).commit();
    }

    void showMenuFragment() {

        if (menuFragment == null)
            menuFragment = (MenuFragment) getFragmentManager().findFragmentByTag(menuFragment_tag);
        getFragmentManager().beginTransaction().show(menuFragment).commit();
    }

    void showDeviceFragment() {

        if (deviceFragment == null)
            deviceFragment = (DeviceFragment) getFragmentManager().findFragmentByTag(deviceFragment_tag);
        getFragmentManager().beginTransaction().show(deviceFragment).commit();
    }

    void showUserFragment() {

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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK ) {
            LogUtils.d("onKeyDown", "按下了退出按钮");
            exitToast.setDuration(Toast.LENGTH_LONG);
            exitToast.show();
        }
        return true;
    }
}
