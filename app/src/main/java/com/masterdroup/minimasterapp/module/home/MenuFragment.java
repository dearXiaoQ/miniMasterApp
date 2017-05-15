package com.masterdroup.minimasterapp.module.home;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.model.RecipesList;
import com.masterdroup.minimasterapp.module.menu.MenuCreateActivity;
import com.masterdroup.minimasterapp.module.menu.MenuListActivity;
import com.masterdroup.minimasterapp.module.menu.MenuViewActivity;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;
import com.yuyh.library.imgsel.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

import static com.masterdroup.minimasterapp.util.Utils.isLogin;

/**
 * Created by 11473 on 2016/12/19.
 */

/** 菜谱页面 */

public class MenuFragment extends Fragment {

    @Bind(R.id.banner)
    Banner banner;
    //    @Bind(R.id.iv_center1)
//    ImageView ivCenter1;
//    @Bind(R.id.iv_center2)
//    ImageView ivCenter2;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    View view;
    @Bind(R.id.rv_menu)
    RecyclerView mRvMenu;

    List<Recipes.RecipesBean> recipes_banner = new ArrayList<>();
    List<Recipes.RecipesBean> recipes_list = new ArrayList<>();

    StaggeredMenuRVAdapter mAdapter;

    List<ButtonMenu> mButtonMenus = new ArrayList<>();
    @Bind(R.id.rv_menu_b)
    RecyclerView mRvMenuB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_menu_home, container, false);
        ButterKnife.bind(this, view);

//
        if (isLogin())
            LogUtils.d("已登陆");
        else
            LogUtils.d("未登陆");
        //设置ActionBar
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);//需要额外调用
        this.view = view;

        initData();
        return view;
    }


    void initData() {
        mAdapter = new StaggeredMenuRVAdapter();
        mRvMenu.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));   //RecyclerView 瀑布流布局
        //   mRvMenu.setLayoutManager(new GridLayoutManager(view.getContext(), 2)); //等宽布局
        //设置item之间的间隔
        mRvMenu.setAdapter(mAdapter);
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        mRvMenu.addItemDecoration(decoration);
        mRvMenu.setNestedScrollingEnabled(false);
        //设置Item增加、移除动画
        mRvMenu.setItemAnimator(new DefaultItemAnimator());

        Observable o = Network.getMainApi().getRecipesList(0, 10);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {
                if (o.getErrorCode() == 0) {
                    for (int i = 0; i < 4; i++) {
                        recipes_banner.add(o.getRes().getList().get(i));
                    }
                    setBanner();

                    recipes_list = o.getRes().getList();
                    mAdapter.notifyDataSetChanged();

                }

            }
        }, view.getContext());


        JxUtils.toSubscribe(o, s);

        ButtonMenu bm1 = new ButtonMenu("本周流行", R.drawable.ic_menu_button1);
        ButtonMenu bm2 = new ButtonMenu("无肉不欢", R.drawable.ic_menu_button2);
        ButtonMenu bm3 = new ButtonMenu("精致西点", R.drawable.ic_menu_button3);
        ButtonMenu bm4 = new ButtonMenu("小聚简食", R.drawable.ic_menu_button4);
        ButtonMenu bm5 = new ButtonMenu("快手家常", R.drawable.ic_menu_button5);
        ButtonMenu bm6 = new ButtonMenu("美味早点", R.drawable.ic_menu_button6);
        ButtonMenu bm7 = new ButtonMenu("官方推荐", R.drawable.ic_menu_button7);
        ButtonMenu bm8 = new ButtonMenu("面包大师", R.drawable.ic_menu_button8);

        mButtonMenus.add(bm1);
        mButtonMenus.add(bm2);
        mButtonMenus.add(bm3);
        mButtonMenus.add(bm4);
        mButtonMenus.add(bm5);
        mButtonMenus.add(bm6);
        mButtonMenus.add(bm7);
        mButtonMenus.add(bm8);
        mRvMenuB.setLayoutManager(new GridLayoutManager(view.getContext(), 4, LinearLayoutManager.VERTICAL, false));
        mRvMenuB.setAdapter(new ButtonMenuAdapter(R.layout.view_home_menu_button_item, mButtonMenus));
        mRvMenuB.setNestedScrollingEnabled(false);
        mRvMenuB.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(view.getContext(), MenuListActivity.class).putExtra("title", mButtonMenus.get(position).getTitle()));
            }
        });
    }

    void setBanner() {

        List<String> images = new ArrayList<>();

        for (Recipes.RecipesBean s : recipes_banner) {
            images.add(Constant.BASEURL + s.getDetail().getImgSrc());
        }


        //设置图片加载器
        banner.setImageLoader(new ImageLoader());
        //设置图片集合
        banner.setImages(images);

        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                startActivity(new Intent(view.getContext(), MenuViewActivity.class).putExtra("_id", recipes_banner.get(position - 1).get_id()));
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


    class StaggeredMenuRVAdapter extends RecyclerView.Adapter<StaggeredMenuRVAdapter.ViewHolder> {
        private final LayoutInflater mLayoutInflater;

        StaggeredMenuRVAdapter() {
            mLayoutInflater = LayoutInflater.from(view.getContext());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ViewHolder(mLayoutInflater.inflate(R.layout.view_home_recipes_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if(position == 0) {
                LogUtils.d("menuFragment", "设置第一张图片高度");
                ViewGroup.LayoutParams params = holder.mIvMenuCover.getLayoutParams();
                params.height = 800;
                holder.mIvMenuCover.setLayoutParams(params);
            }

            final Recipes.RecipesBean r = recipes_list.get(position);

            holder.mIvMenuCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), MenuViewActivity.class).putExtra("_id", r.get_id()));
                }
            });

            ImageLoader.getInstance().displayImage(view.getContext(), Constant.BASEURL + r.getDetail().getImgSrc(), holder.mIvMenuCover);
            holder.mTvMenuName.setText(r.getName());

            if (null != r.getOwner().getOwnerUid())
                holder.mTvUserName.setText(r.getOwner().getOwnerUid().getName());
            else
                holder.mTvUserName.setText("null");
        }

        @Override
        public int getItemCount() {
            return recipes_list == null ? 0 : recipes_list.size();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.iv_menu_cover)
            ImageView mIvMenuCover;
            @Bind(R.id.tv_menu_name)
            TextView mTvMenuName;
            @Bind(R.id.tv_user_name)
            TextView mTvUserName;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    public class ButtonMenuAdapter extends BaseQuickAdapter<ButtonMenu, BaseViewHolder> {

        public ButtonMenuAdapter(int layoutResId, List<ButtonMenu> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ButtonMenu item) {

            helper.setText(R.id.tv_title, item.getTitle());
            Glide.with(mContext).load(item.getIc()).crossFade().into((ImageView) helper.getView(R.id.iv_ic));
//            ImageLoader.getInstance().displayGlideImage(item.getIc(), ((ImageView) helper.getView(R.id.iv_ic)), view.getContext(), false);

        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        /** 设置搜索输入框的颜色 */
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        SearchView.SearchAutoComplete mEdit = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        mEdit.setTextColor(getActivity().getResources().getColor(R.color.white));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
           /* case R.id.ic_add:
                startActivity(new Intent(view.getContext(), MenuCreateActivity.class));
                break;*/
            case R.id.action_search:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @OnClick(R.id.toolbar)
    public void onClick() {
    }

    class ButtonMenu {
        String title;
        int ic;

        ButtonMenu(String title, int ic) {
            this.title = title;
            this.ic = ic;

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIc() {
            return ic;
        }

        public void setIc(int ic) {
            this.ic = ic;
        }
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space=space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left=space;
            outRect.right=space;
            outRect.bottom=space;
            if(parent.getChildAdapterPosition(view)==0){
                outRect.top=space;
            }
        }
    }





}
