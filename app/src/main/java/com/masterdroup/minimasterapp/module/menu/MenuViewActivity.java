package com.masterdroup.minimasterapp.module.menu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.model.Step;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.Utils;
import com.masterdroup.minimasterapp.view.FoodsAdapter;
import com.masterdroup.minimasterapp.view.StepAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuViewActivity extends Activity implements Contract.MenuAloneView {


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_cover)
    ImageView ivCover;
    @Bind(R.id.tv_menu_name)
    TextView tvMenuName;
    @Bind(R.id.tv_menu_info)
    TextView tvMenuInfo;
    @Bind(R.id.iv_user_head)
    ImageView ivUserHead;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;

    Contract.Presenter mPresenter;
    @Bind(R.id.tv_menu_note)
    TextView tvMenuNote;
    @Bind(R.id.activity_menu_view)
    LinearLayout activityMenuView;
    @Bind(R.id.rv_food)
    RecyclerView rv_food;

    @Bind(R.id.rv_step)
    RecyclerView rv_step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);
        ButterKnife.bind(this);
        new MenuPresenter(this);

        initData();
    }

    private void initData() {

        int i = getIntent().getIntExtra("menuId", -1);
        if (i != -1)
            mPresenter.gettingData(i);

        Menu menu = new Menu();
        menu.cover_url = "https://images.pexels.com/photos/203089/pexels-photo-203089.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb";
        menu.head_url = "https://static.pexels.com/users/avatars/592/pawel-malinowski-908-medium.jpeg";
        menu.user_name = "0078";
        menu.menu_name = "土豆丝";
        menu.score = "9.3";
        settingData(menu);

        List<String> foods = new ArrayList<>();
        foods.add("油    30g");
        foods.add("面粉   30g");
        foods.add("面粉   30g");
        foods.add("面粉   30g");
        rv_food.setAdapter(new FoodsAdapter(this, foods));
        rv_food.setLayoutManager(new GridLayoutManager(this, 2));

        List<Step> steps = new ArrayList<>();

        Step step1 = new Step();
        step1.setStepNo(1);
        step1.setDes("放在温度37度左右湿度75%的环境下发酵至2倍大\n" + "手指轻摁表面可以缓慢回弹");
        step1.setPicture_url("http://s1.cdn.xiachufang.com/58e03f86c72411e6bc9d0242ac110002_1333w_1000h.jpg@2o_50sh_1pr_1l_200w_90q_1wh");

        Step step2 = new Step();
        step2.setStepNo(2);
        step2.setDes("放入预热好的烤箱，中下层\n" +
                "上下管180度烘烤20分钟出炉，顶部上色要及时盖锡纸");
        step2.setPicture_url("http://s1.cdn.xiachufang.com/60d7fea4c72411e6947d0242ac110002_1333w_1000h.jpg@2o_50sh_1pr_1l_200w_90q_1wh");

        Step step3 = new Step();
        step3.setStepNo(3);
        step3.setDes("出炉趁热刷一层黄油，脱模冷却即可");
        step3.setPicture_url("http://s1.cdn.xiachufang.com/681cc2d0c72411e6bc9d0242ac110002_1333w_1000h.jpg@2o_50sh_1pr_1l_200w_90q_1wh");

        steps.add(step1);
        steps.add(step2);
        steps.add(step3);

        rv_step.setAdapter(new StepAdapter(this, steps));
//        rv_step.setLayoutManager(new GridLayoutManager(this, 1));
        rv_step.setLayoutManager(new LinearLayoutManager(this));
        rv_step.setNestedScrollingEnabled(false);

    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }

    @OnClick(R.id.iv_return)
    public void onClick() {
        finish();
    }

    @Override
    public void settingData(Menu menu) {
        tvTitle.setText(menu.getMenu_name());
        tvMenuName.setText(menu.getMenu_name());
        tvMenuInfo.setText(String.format("%s综合评分 %s人收藏", menu.getScore(), menu.getScore()));
        tvUserName.setText(menu.getUser_name());
        ImageLoader.getInstance().displayGlideImage(menu.getCover_url(), ivCover, this, false);
        ImageLoader.getInstance().displayGlideImage(menu.getHead_url(), ivUserHead, this, true);
    }

}
