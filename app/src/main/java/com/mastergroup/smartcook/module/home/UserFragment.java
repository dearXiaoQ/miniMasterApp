package com.mastergroup.smartcook.module.home;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.User;
import com.mastergroup.smartcook.module.menu.FavoriteListActivity;
import com.mastergroup.smartcook.module.menu.MenuCreateActivity;
import com.mastergroup.smartcook.module.menu.MyMenuListActivity;
import com.mastergroup.smartcook.module.settings.FeedBackActivity;
import com.mastergroup.smartcook.module.settings.SettingsActivity;
import com.mastergroup.smartcook.util.ImageLoader;
import com.mastergroup.smartcook.util.Utils;

import org.w3c.dom.Text;

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

    @Bind(R.id.create_menu)
    Button createNewMenu;

    @Bind(R.id.iv_settings)
    ImageView mIvSettings;

    @Bind(R.id.tv_out)
    TextView mTvOut;

    @Bind(R.id.rl_mymenu)
    RelativeLayout mRlMymenu;

    @Bind(R.id.rl_store)
    RelativeLayout rlStore;

    @Bind(R.id.signature_tv)
    TextView signature_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new  HomePresenter(this);

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

        if(!(userBean.getHeadUrl().equals(""))) {
            signature_tv.setText(user.getUser().getSingnature());
        }

        if( !(userBean.getHeadUrl().equals("")) ) {
            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + userBean.getHeadUrl(), ivUserHead, tvUserName.getContext(), true);
        }

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

    @OnClick({R.id.iv_settings, R.id.tv_out, R.id.rl_store, R.id.create_menu, R.id.rl_mycollect, R.id.rl_mymenu, R.id.rl_opinion})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_settings:

                    startActivity(new Intent(this.getActivity(), SettingsActivity.class));

                break;

            case R.id.tv_out:

                mHomePresenter.outLogin();

                break;

            case R.id.rl_store:

                Uri uri = Uri.parse("https://momscook.tmall.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                break;

            case R.id.create_menu:

                startActivity(new Intent(getActivity(), MenuCreateActivity.class));

                break;

            case R.id.rl_mycollect:

                startActivity(new Intent(getActivity(), FavoriteListActivity.class));

                break;

            case R.id.rl_mymenu:

                startActivity(new Intent(getActivity(), MyMenuListActivity.class));

                break;

            case R.id.rl_opinion:

                startActivity(new Intent(getActivity(), FeedBackActivity.class));

                break;

        }

    }



}
