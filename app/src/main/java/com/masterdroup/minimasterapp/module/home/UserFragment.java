package com.masterdroup.minimasterapp.module.home;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.UserInfo;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 11473 on 2016/12/20.
 */

public class UserFragment extends Fragment implements Contract.UserView {

    Contract.Presenter mHomePresenter;

    @Bind(R.id.iv_user_head)
    ImageView ivUserHead;
    @Bind(R.id.iv_settings)
    ImageView ivSettings;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_user_follow)
    TextView tvUserFollow;
    @Bind(R.id.tv_user_fans)
    TextView tvUserFans;

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
        ImageLoader.getInstance().displayGlideImage("https://images.pexels.com/photos/208799/pexels-photo-208799.jpeg?h=350&auto=compress&cs=tinysrgb", ivUserHead, view.getContext(), true);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        //请求用户信息
        mHomePresenter.getUserInfo();
    }

    @Override
    public Context ongetContext() {
        return this.getActivity();
    }

    @Override
    public void onShowUserInfo(UserInfo userInfo) {
        UserInfo.UserBean user = userInfo.getUser();
        tvUserName.setText(user.getName());
    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mHomePresenter = Utils.checkNotNull(presenter);
    }

}
