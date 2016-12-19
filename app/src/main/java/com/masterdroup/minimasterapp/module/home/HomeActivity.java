package com.masterdroup.minimasterapp.module.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.RelativeLayout;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.util.DebugUtils;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends Activity {

    @Bind(R.id.bottomBar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();


    }

    private void init() {

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_food) {
                    DebugUtils.d("TAB", "tab_food");

                } else if (tabId == R.id.tab_device)
                    DebugUtils.d("TAB", "tab_device");
                else if (tabId == R.id.tab_user)
                    DebugUtils.d("TAB", "tab_user");
            }
        });
    }
}
