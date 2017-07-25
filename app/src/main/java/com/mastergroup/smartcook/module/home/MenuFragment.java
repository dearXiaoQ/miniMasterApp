package com.mastergroup.smartcook.module.home;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.Recipes;
import com.mastergroup.smartcook.module.menu.MenuListActivity;
import com.mastergroup.smartcook.module.menu.MenuViewActivity;
import com.mastergroup.smartcook.module.menu.SearchActivity;
import com.mastergroup.smartcook.module.message.MessageActivity;
import com.mastergroup.smartcook.util.ImageLoader;
import com.mastergroup.smartcook.util.ToastUtils;
import com.mastergroup.smartcook.util.Utils;

import com.mastergroup.smartcook.view.VerticalSwipeRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;
import com.yuyh.library.imgsel.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mastergroup.smartcook.util.Utils.isLogin;

/**
 * Created by 11473 on 2016/12/19.
 */

/** 菜谱页面 */

public class MenuFragment extends Fragment implements Contract.MenuView,  SwipeRefreshLayout.OnRefreshListener{

    Contract.Presenter mPresenter;

    @Bind(R.id.banner)
    Banner banner;
    /*  @Bind(R.id.toolbar)
      Toolbar mToolbar;*/
    View view;
    @Bind(R.id.rv_menu)
    RecyclerView mRvMenu;
    @Bind(R.id.scrollView)
    ScrollView  mScrollView;
    @Bind(R.id.bottomTv)
    TextView moreTv;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.id_swipe_ly)
    VerticalSwipeRefreshLayout refreshLayout;

    List<Recipes.RecipesBean> recipes_banner = new ArrayList<>();
    List<Recipes.RecipesBean> recipes_list = new ArrayList<>();

    StaggeredMenuRVAdapter mAdapter;

    List<ButtonMenu> mButtonMenus = new ArrayList<>();
    @Bind(R.id.rv_menu_b)
    RecyclerView mRvMenuB;

    public static int  LOADER_RECIPES_COUNT = 5;

    private StaggeredGridLayoutManager mStaggerGridLayoutManager;

    /** 是否用可以加载了 */
    private boolean isLoading = false;

    /** 是否用更多的数据加载 */
    private  boolean isCanLoading = true;

    /**  Banner图片  */
    private List<String> images = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HomePresenter(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_menu_home, container, false);
        ButterKnife.bind(this, view);


        if (isLogin())
            LogUtils.d("已登陆");
        else
            LogUtils.d("未登陆");
        //设置ActionBar
        //   ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);//需要额外调用
        this.view = view;

        refreshLayout.setOnRefreshListener(this);
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //initData();
    }

    void initData() {
        /** 瀑布流设置 */
        mRvMenu.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));   //RecyclerView 瀑布流布局
        //   mRvMenu.setLayoutManager(new GridLayoutManager(view.getContext(), 2)); //等宽布局
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(6);
        mRvMenu.addItemDecoration(decoration);
        mRvMenu.setNestedScrollingEnabled(false);
        //设置Item增加、移除动画
        mStaggerGridLayoutManager = (StaggeredGridLayoutManager) mRvMenu.getLayoutManager();
        mRvMenu.setItemAnimator(new DefaultItemAnimator());

        mScrollView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //判断 scrollView 当前滚动位置在顶部
                if(mScrollView.getScrollY() == 0) {
                    LogUtils.d("mScrollView", "The scrollView is swipe  to top！");
                    // ptrCFL.setEnabled(true);
                    return false;
                }
                //    ptrCFL.setEnabled(false);
                if(isCanLoading)//用更多数据加载
                    if(mScrollView.getChildAt(0).getHeight() - mScrollView.getHeight() == mScrollView.getScrollY() )  //滑动到底部
                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            //抬起动作（释放刷新）
                            mPresenter.getMoreRecipes(recipes_list.size(), LOADER_RECIPES_COUNT);
                            mProgressBar.setVisibility(View.VISIBLE);
                            moreTv.setVisibility(View.GONE);
                        }

                return false;
            }
        });

        mPresenter.getRecipes(0, LOADER_RECIPES_COUNT);
        mPresenter.getBanner();

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



    @Override
    public void onGetRecipesSuccess(List<Recipes.RecipesBean> recipes_list) {
        this.recipes_list = recipes_list;
        //mAdapter.notifyDataSetChanged();

        //   ptrCFL.setEnabled(false);
        mAdapter = new StaggeredMenuRVAdapter();

        mRvMenu.setAdapter(mAdapter);
        refreshLayout.setRefreshing(false);

    }

    @Override
    public void onGetRecipesFailure(String info) {
        ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_BOTTOM, info);
    }

    @Override
    public Context onGetContext() {
        return getActivity();
    }

    @Override
    public void onGetMoreRecipesSuccess(List<Recipes.RecipesBean> recipes_more_list) {
        if(recipes_more_list.size() < 5) {
            isCanLoading = false;
            moreTv.setText(R.string.not_more_data);
        }

        mProgressBar.setVisibility(View.GONE);
        moreTv.setVisibility(View.VISIBLE);

        for (Recipes.RecipesBean recipes : recipes_more_list)
            this.recipes_list.add(recipes);


        mAdapter.notifyDataSetChanged();

        isLoading = false;


    }

    @Override
    public void onGetMoreRecipesFailure(String info) {
        isLoading = false;
        mProgressBar.setVisibility(View.GONE);
        moreTv.setVisibility(View.VISIBLE);
        ToastUtils.showCustomToast(App.mContext, ToastUtils.TOAST_BOTTOM, info);
    }

    @Override
    public void onGetBannerSuccess(List<Recipes.RecipesBean> banner_list) {
        this.recipes_banner = banner_list;
        if(!(images.size() == 5)) {
            for (Recipes.RecipesBean s : recipes_banner) {
                images.add(Constant.BASEURL + s.getDetail().getImgSrc());
            }
        }
        setBanner();
    }


    @Override
    public void onGetBannerFailure(String info) {
        Log.d("banner", " info = " + info);
    }


    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }


    @Override
    public void onRefresh() {
        mPresenter.getRecipes(0, LOADER_RECIPES_COUNT);
        mPresenter.getBanner();
        isCanLoading = true;
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
                params.height = 700;
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
            int favoriteSize =r.getFavorite().size();
            holder.favoriteTv.setText(favoriteSize + "");
            if(favoriteSize == 0) {
                holder.favoriteIv.setImageResource(R.drawable.like_it);
            }

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
            @Bind(R.id.favorite_tv)
            TextView favoriteTv;
            @Bind(R.id.favorite_iv)
            ImageView favoriteIv;

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


 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        *//** 设置搜索输入框的颜色 *//*
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        SearchView.SearchAutoComplete mEdit = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        mEdit.setTextColor(getActivity().getResources().getColor(R.color.white));
    }*/

    @OnClick({R.id.search_rl, R.id.message_iv})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.search_rl:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;

            case R.id.message_iv:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

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

   /* @OnClick(R.id.toolbar)
    public void onClick() {
    }*/

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
