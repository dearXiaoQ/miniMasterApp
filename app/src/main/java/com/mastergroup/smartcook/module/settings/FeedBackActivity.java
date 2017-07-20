package com.mastergroup.smartcook.module.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.util.ToastUtils;
import com.mastergroup.smartcook.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//意见反馈页面
public class FeedBackActivity extends Activity implements Contract.FeedBackView{

    Contract.Presenter mPresenter;

    Context mContext;

    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_more_button)
    TextView tvMoreButton;
    @Bind(R.id.phone_ll)
    LinearLayout phoneLl;
    @Bind(R.id.input_msg_et)
    EditText msgEt;
    @Bind(R.id.rb)
    RadioGroup rb;

    String type = FEED_BACK;

    public static final String FEED_BACK = "1";
    public static final String FUNCTION_ENABLE = "2";
    public static final String OTHER = "3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        new SettingsPresenter(this);
        mPresenter.start();
        mContext = this;
        initView();
    }

    private void initView() {
        tvTitle.setText(R.string.feedback);
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.feedback_rb:
                        type = FEED_BACK;
                        break;
                    case R.id.function_enable_rb:
                        type = FUNCTION_ENABLE;
                        break;
                    case R.id.other_rb:
                        type = OTHER;
                        break;
                }
            }
        });
    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }

    @Override
    public Context onGetContext() {
        return this;
    }

    @Override
    public void onFeedBackInfo(String info) {
        ToastUtils.showCustomToast(mContext, ToastUtils.TOAST_BOTTOM, info);
    }


    @OnClick({R.id.iv_return, R.id.tv_more_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
            case R.id.tv_more_button:

                mPresenter.feedBack(msgEt.getText().toString().trim(), type);

                break;
        }
    }

}









