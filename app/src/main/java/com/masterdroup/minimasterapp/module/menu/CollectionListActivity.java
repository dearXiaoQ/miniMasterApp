package com.masterdroup.minimasterapp.module.menu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.util.header.RentalsSunHeaderView;
import com.yuyh.library.imgsel.utils.LogUtils;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;
public class CollectionListActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.iv_return)
    ImageView backIv;

    @Bind(R.id.tv_title)
    TextView titleTv;

    @Bind(R.id.tv_more_button)
    TextView moreBtn;

    @Bind(R.id.ptr)
    PtrFrameLayout mPtrFrame;

    @Bind(R.id.collection_lv)
    ListView menuLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_list);
        ButterKnife.bind(this);

        initView();

    }

    private void initView() {

        setPullToRefresh();

        backIv.setOnClickListener(this);

        titleTv.setText(getString(R.string.collection_menu));

        moreBtn.setVisibility(View.GONE);

        menuLv.setAdapter(new MenuAdapter());

        menuLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                 //what should I do
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem == 0) {
                    View firstVisibleItemView = menuLv.getChildAt(0);
                    if(firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        LogUtils.i("onScroll", "####  滚动到顶部  ####");
                        mPtrFrame.setEnabled(true);
                    }
                } else if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    View lastVisibleItemView = menuLv.getChildAt(menuLv.getChildCount() - 1);
                    if(lastVisibleItemView != null && lastVisibleItemView.getBottom() == menuLv.getHeight()) {
                        LogUtils.i("", "####  滚动到底部  ####");
                        mPtrFrame.setEnabled(false);
                    }
                } else {
                    //默认为不可以刷新
                    mPtrFrame.setEnabled(false);
                }
            }
        });
    }

    private void setPullToRefresh() {
        /**
         *  在 xml 配置过， 就不要在这里配置了
         */
        /*
        mPtrFrame.setResistance(1.7f); //阻尼系数 默认: 1.7f，越大，感觉下拉时越吃力。
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f); //触发刷新时移动的位置比例 默认，1.2f，移动达到头部高度1.2倍时可触发刷新操作。
        mPtrFrame.setDurationToClose(200);//回弹延时 默认 200ms，回弹到刷新高度所用时间
        mPtrFrame.setDurationToCloseHeader(1000);//头部回弹时间 默认1000ms
        mPtrFrame.setPullToRefresh(false);// 刷新是保持头部 默认值 true.
        mPtrFrame.setKeepHeaderWhenRefresh(true);//下拉刷新 / 释放刷新 默认为释放刷新
        */
        /** 经典的头部风格实现*/
       /* final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(this);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0 ,0);*/


        /**
         * StoreHouse风格的头部实现
         */
  /*      final StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);*/



        /**
         * using a string, support: A-Z 0-9 - .
         * you can add more letters by {@link in.srain.cube.views.ptr.header.StoreHousePath#addChar}
         */
        // header.initWithString("Alibaba");


        /**
         * Material Design风格的头部实现
         */
      /*  final MaterialHeader header = new MaterialHeader(this);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);*///显示相关工具类，用于获取用户屏幕宽度、高度以及屏幕密度。同时提供了dp和px的转化方法。

        /**
         * Rentals Style风格的头部实现
         * 这个需要引入这两个类RentalsSunDrawable.java ; RentalsSunHeaderView.java
         * 在人家git上的daemon中能找到
         */
        final RentalsSunHeaderView header = new RentalsSunHeaderView(this);

        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setUp(mPtrFrame);
        mPtrFrame.setLoadingMinTime(1000);
        mPtrFrame.setDurationToCloseHeader(1500);

        mPtrFrame.setEnabled(false);

        mPtrFrame.setHeaderView(header);
        mPtrFrame.setPinContent(true); //刷新时，保持内容不动，仅头部下移，默认，false
        mPtrFrame.addPtrUIHandler(header);
        // mPtrFrame.setKeepHeaderWhenRefresh(true); //刷新时保持头部的显示， 默认为true
        // mPtrFrame.disableWhenHorizontalMove(true); //如果是ViewPager, 设置为true, 会解决ViewPager 滑动冲突问题
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {

                LogUtils.d("refresh", "checkCanDoRefresh");
                return true;

            }

            /*** 检查是否可以执行下拉刷新， 比如列表为空或者列表第一项在最上面时 */
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {

                LogUtils.d("refresh", "onRefreshBegin");
                //return true;
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.d("refresh", "running");
                        mPtrFrame.refreshComplete();
                        // mPtrFrame.autoRefresh(); //自动刷新
                    }
                }, 1800);

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_return:
                CollectionListActivity.this.finish();
                break;

            default:

                break;

        }
    }



    class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(CollectionListActivity.this).inflate(R.layout.collection_lv_item, null);
            return convertView;
        }
    }

}
