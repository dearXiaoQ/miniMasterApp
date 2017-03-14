package com.masterdroup.minimasterapp.module.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.model.RecipesList;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.JxUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

public class MyMenuListActivity extends AppCompatActivity {


    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_more_button)
    TextView mTvMoreButton;
    @Bind(R.id.rv)
    RecyclerView mRv;
    List<Recipes.RecipesBean> RecipesBeans = new ArrayList<>();
    RecipesBeansAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu_list);
        ButterKnife.bind(this);
        ivitView();
        ivitData();
    }

    void ivitView() {
//        adapter = new RecipesBeansAdapter(R.layout.view_menu_item, RecipesBeans);
        mTvTitle.setText("我的菜谱");
        mTvMoreButton.setVisibility(View.GONE);
        mRv.setLayoutManager(new LinearLayoutManager(this));

        mRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(MyMenuListActivity.this, MenuViewActivity.class).putExtra("_id", RecipesBeans.get(position).get_id()));
            }
        });
    }

    private void ivitData() {

        Observable o = Network.getMainApi().listByOwner(0, 10);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {

                if (o.getErrorCode() == 0) {
                    RecipesBeans = o.getRes().getList();
                    mRv.setAdapter(new RecipesBeansAdapter(R.layout.view_menu_item, RecipesBeans));
                }
            }
        }, this);

        JxUtils.toSubscribe(o, s);

    }

    @OnClick(R.id.iv_return)
    public void onClick() {
        finish();
    }

    public class RecipesBeansAdapter extends BaseQuickAdapter<Recipes.RecipesBean, BaseViewHolder> {

        public RecipesBeansAdapter(int layoutResId, List<Recipes.RecipesBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Recipes.RecipesBean item) {

            helper.setText(R.id.tv_menu_name, item.getName());
            helper.setText(R.id.tv_menu_user_name, item.getOwner().getOwnerUid().getName());
            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL+item.getDetail().getImgSrc(), ((ImageView) helper.getView(R.id.iv_cover)), MyMenuListActivity.this, false);
            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL+item.getOwner().getOwnerUid().getHeadUrl(), ((ImageView) helper.getView(R.id.iv_head)), MyMenuListActivity.this, true);

        }
    }


}
