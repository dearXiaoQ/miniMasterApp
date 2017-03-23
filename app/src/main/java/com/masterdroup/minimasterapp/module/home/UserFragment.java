package com.masterdroup.minimasterapp.module.home;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.User;
import com.masterdroup.minimasterapp.module.device.DeviceListActivity;
import com.masterdroup.minimasterapp.module.menu.MyMenuListActivity;
import com.masterdroup.minimasterapp.module.settings.SettingsActivity;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 11473 on 2016/12/20.
 */

public class UserFragment extends Fragment implements Contract.UserView {

    Contract.Presenter mHomePresenter;

    @Bind(R.id.iv_user_head)
    ImageView ivUserHead;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;

    @Bind(R.id.iv_settings)
    ImageView mIvSettings;
    @Bind(R.id.tv_out)
    TextView mTvOut;
    @Bind(R.id.rl_mymenu)
    RelativeLayout mRlMymenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HomePresenter(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_user, container, false);
        ButterKnife.bind(this, view);

        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        if (!Utils.isLogin())
            return;
        //请求用户信息
        mHomePresenter.getUserInfo();
    }

    @Override
    public Context ongetContext() {
        return this.getActivity();
    }

    @Override
    public void onShowUserInfo(User user) {
        User.UserBean userBean = user.getUser();
        tvUserName.setText(userBean.getName());
        ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + userBean.getHeadUrl(), ivUserHead, tvUserName.getContext(), true);
    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mHomePresenter = Utils.checkNotNull(presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_settings, R.id.tv_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_settings:

                startActivity(new Intent(this.getActivity(), SettingsActivity.class));
                startActivity(new Intent(this.getActivity(), DeviceListActivity.class));
                break;
            case R.id.tv_out:
                mHomePresenter.outLogin();

                break;

        }

    }


    @OnClick(R.id.rl_mymenu)
    public void onClick() {
        startActivity(new Intent(getActivity(), MyMenuListActivity.class));

    }
}
