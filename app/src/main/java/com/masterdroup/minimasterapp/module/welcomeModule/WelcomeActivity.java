package com.masterdroup.minimasterapp.module.welcomeModule;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.module.CommonModule.GIZBaseActivity;
import com.masterdroup.minimasterapp.module.home.HomeActivity;
import com.masterdroup.minimasterapp.util.Utils;

public class WelcomeActivity extends GIZBaseActivity implements View.OnClickListener, Contract.MainView {

    Button btn_login, btn_registered;
    ImageView iv_return_view;
    View view1;//注册与登录按钮
    LoginFragment loginFragment;
    RegisteredFragment registeredFragment;
    RetrievePwdFragment retrievePwdFragment;
    Contract.Presenter mPresenter;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;

    TextView tv_skip;
    int duration = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new WelcomePresenter(this);

        if (mPresenter.isLogin()) {
            //直接跳转到home页面
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
            finish();
        } else
            init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        showMainViewAnimation();
    }

    private void init() {

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        iv_return_view = (ImageView) findViewById(R.id.ib_return_view);

        iv_return_view.setOnClickListener(this);
        frameLayout = (FrameLayout) findViewById(R.id.fl_content);
        view1 = findViewById(R.id.view1);
        tv_skip = (TextView) findViewById(R.id.tv_skip);

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                finish();
            }
        });


        fragmentManager = getFragmentManager();
        loginFragment = new LoginFragment();
        registeredFragment = new RegisteredFragment();
        retrievePwdFragment = new RetrievePwdFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_login:
                mPresenter.showLoginView();
                break;

            case R.id.btn_registered:
                mPresenter.showRegisteredView();
                break;

            case R.id.ib_return_view:
                mPresenter.showMainView();
                break;
        }


    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }

    @Override
    public void onShowLoginView() {
        hideMainViewAnimation();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_content, loginFragment)
                .commit();
    }

    @Override
    public void onRegisteredView() {
        hideMainViewAnimation();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_content, registeredFragment)
                .commit();
    }

    @Override
    public void onShowMainView() {
        showMainViewAnimation();
        iv_return_view.setVisibility(View.GONE);//隐藏返回按钮
        fragmentManager.beginTransaction()
                .remove(loginFragment)
                .remove(registeredFragment)
                .commit();


    }


    public void onRetrieveView() {
        hideMainViewAnimation();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_content, retrievePwdFragment)
                .commit();
    }


    private void showMainViewAnimation() {

        Animation scaleAnimation = new AlphaAnimation(0.0f, 1.0f);
        scaleAnimation.setDuration(duration);
        view1.startAnimation(scaleAnimation);


        scaleAnimation = new AlphaAnimation(1.0f, 0.0f);
        scaleAnimation.setDuration(duration);
        frameLayout.startAnimation(scaleAnimation);
        iv_return_view.startAnimation(scaleAnimation);


        view1.setVisibility(View.VISIBLE);
        iv_return_view.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);

    }

    private void hideMainViewAnimation() {

        Animation scaleAnimation = new AlphaAnimation(1.0f, 0.0f);
        scaleAnimation.setDuration(duration);
        view1.startAnimation(scaleAnimation);

        scaleAnimation = new AlphaAnimation(0.0f, 1.0f);
        scaleAnimation.setDuration(duration);
        frameLayout.startAnimation(scaleAnimation);
        iv_return_view.startAnimation(scaleAnimation);

        view1.setVisibility(View.GONE);
        iv_return_view.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
    }
}
