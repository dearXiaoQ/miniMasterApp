package com.masterdroup.minimasterapp.module.welcomeModule;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.util.Utils;

public class WelcomeActivity extends Activity implements View.OnClickListener, Contract.MainView {


    //    List<View> mListViews;
//    View view1, view2;
//
    Button btn_login, btn_registered;
    ImageButton ib_return;
    ImageView iv_return_view;
    View view1;//注册与登录按钮
    LoginFragment loginFragment;
    RegisteredFragment registeredFragment;
    Contract.Presenter mPresenter;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;

    int duration = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        fragmentManager = getFragmentManager();

        loginFragment = new LoginFragment();
        registeredFragment = new RegisteredFragment();
        new WelcomePresenter(this);
        init();
        showMainViewAnimation();
    }

    private void init() {

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        ib_return = (ImageButton) findViewById(R.id.ib_return);
        ib_return.setOnClickListener(this);
        iv_return_view = (ImageView) findViewById(R.id.ib_return_view);
        frameLayout = (FrameLayout) findViewById(R.id.fl_content);
        view1 = findViewById(R.id.view1);

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
            case R.id.ib_return:
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
        fragmentManager.beginTransaction()
                .remove(loginFragment)
                .remove(registeredFragment)
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
