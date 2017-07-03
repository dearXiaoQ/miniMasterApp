package com.masterdroup.minimasterapp.module.menu;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.bumptech.glide.util.Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.masterdroup.minimasterapp.App;
import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Comment;
import com.masterdroup.minimasterapp.model.CookingStep;
import com.masterdroup.minimasterapp.model.DescribeStep;
import com.masterdroup.minimasterapp.model.DetailRecipes;
import com.masterdroup.minimasterapp.model.Favorites;
import com.masterdroup.minimasterapp.model.Food;
import com.masterdroup.minimasterapp.model.Like;
import com.masterdroup.minimasterapp.model.MyCollectionRecipes;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.model.RecipesList;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.masterdroup.minimasterapp.util.Utils;
import com.masterdroup.minimasterapp.view.MenuListRVAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

import static com.masterdroup.minimasterapp.util.Utils.isLogin;

/**
 * Created by 11473 on 2016/12/21.
 */

public class MenuPresenter implements Contract.Presenter {
    Contract.MenuAloneView menuAloneView;
    Contract.MenuListView menuListView;
    Contract.MyMenuListView myMenuListView;
    Contract.FavoriteListView favoriteListView;

    Context mContext;
    MenuListRVAdapter adapter;
    FoodsAdapter food_adapter;
    StepAdapter step_adapter;
    LikeAdapter like_adapter;
    CookingStepAdapter cooking_step_adapter;
    CommentAdapter comment_adapter;
    LikeGVAdapter likeGVAdapter;
    public static List<Recipes.RecipesBean> list;


    List<DescribeStep> mSteps = new ArrayList<>();
    List<Food> mFoods = new ArrayList<>();
    List<CookingStep> mCookingSteps = new ArrayList<>();
    List<Like> likes = new ArrayList<>();
    List<Favorites> favorites = new ArrayList<>();
    List<Comment> comments = new ArrayList<>();

    int index = 0;//从第index个开始获取
    int count = 5;//页数

    Observable o_recipesList;
    Subscriber s_recipesList;

    ImageView likeIv;  //点赞图标
    ImageView favoriteIv; //收藏图标
    TextView likeNumTv;    //点赞数量

    DetailRecipes.RecipesBean recipesBean;
    /** 当前用户是已否点赞 */
    boolean islike;
    /** 当前用户是否已收藏 */
    boolean isfavorite;

    String userHeadUrl ;
    String userName;

    @Override
    public void start() {

        list = new ArrayList<>();
        adapter = new MenuListRVAdapter(mContext);
        food_adapter = new FoodsAdapter(mContext);
        step_adapter = new StepAdapter(mContext);
        cooking_step_adapter = new CookingStepAdapter(mContext);
        comment_adapter = new CommentAdapter();
        likeGVAdapter = new LikeGVAdapter();

    }


    public MenuPresenter(Contract.MenuAloneView View) {
        menuAloneView = Utils.checkNotNull(View, "mView cannot be null!");
        menuAloneView.setPresenter(this);
        mContext = menuAloneView.getContext();
    }

    public MenuPresenter(Contract.MenuListView View) {
        menuListView = Utils.checkNotNull(View, "mView cannot be null!");
        menuListView.setPresenter(this);
        mContext = menuListView.getContext();
    }

    public MenuPresenter(Contract.MyMenuListView View) {
        myMenuListView = Utils.checkNotNull(View, "mView cannot be null!");
        myMenuListView.setPresenter(this);
        mContext = myMenuListView.getContext();
    }

    public MenuPresenter(Contract.FavoriteListView View) {
        favoriteListView = Utils.checkNotNull(View, "mView cannot be null!");
        favoriteListView.setPresenter(this);
        mContext = favoriteListView.getContext();
    }

    @Override
    public void gettingData(final String menuId) {
        Observable o = Network.getMainApi().getRecipesDetail(menuId);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<DetailRecipes.RecipesBean>>() {
            @Override
            public void onNext(Base<DetailRecipes.RecipesBean> base) {
                if (base.getErrorCode() == 0) {
                    menuAloneView.settingData(base.getRes());
                    recipesBean = base.getRes();
                    recipesBean.set_id(menuId);
                    mFoods = base.getRes().getFoodList();
                    mSteps = base.getRes().getDescribeSteps();
                    mCookingSteps = base.getRes().getCookingStep();
                    likes = base.getRes().getLikes();
                    favorites = base.getRes().getFavorite();
                    comments = base.getRes().getComment();

                    food_adapter.notifyDataSetChanged();

                    step_adapter.notifyDataSetChanged();
                    cooking_step_adapter.notifyDataSetChanged();


                    //    like_adapter.setNewData(likes);
                    comment_adapter.setNewData(comments);

                    likeGVAdapter.notifyDataSetChanged();
                    menuAloneView.onIsOwner(isOwner());
                    userName = App.spUtils.getString(App.mContext.getString(R.string.username));
                    isfavorite = isFavorite();
                    if(!isfavorite) {
                        favoriteIv.setImageResource(R.drawable.like_it);
                    }
                    islike = isLike();
                    if(islike) {
                        likeIv.setImageResource(R.drawable.love_it_dark);
                    }
                    userHeadUrl = App.spUtils.getString(App.mContext.getString(R.string.user_headurl));
                    if (!isOwner())
                        menuAloneView.onIsLike(islike);
                }
            }
        }, mContext);

        JxUtils.toSubscribe(o, s);
    }

    @Override
    public void reLike(final String menuId) {
        Observable o = Network.getMainApi().getRecipesDetail(menuId);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<DetailRecipes.RecipesBean>>() {
            @Override
            public void onNext(Base<DetailRecipes.RecipesBean> base) {
                if (base.getErrorCode() == 0) {
                    likes = base.getRes().getLikes();
                    // like_adapter.setNewData(likes);

                }
            }
        }, mContext);

        JxUtils.toSubscribe(o, s);
    }


    @Override
    public void getRecipesListData() {
        o_recipesList = Network.getMainApi().getRecipesList(index, count);
        s_recipesList = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {

                if (o.getErrorCode() == 0) {
                    list = o.getRes().getList();
                    adapter.notifyDataSetChanged();
                }
            }
        }, mContext);

        JxUtils.toSubscribe(o_recipesList, s_recipesList);
    }

    @Override
    public void initRV(final PullLoadMoreRecyclerView rv) {
        //// TODO: 2017/3/1 初始化 RecyclerView

        rv.setAdapter(adapter);
        rv.setLinearLayout();
        rv.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                refreshRV(rv);
            }

            @Override
            public void onLoadMore() {
                loadMore(rv);
            }
        });

    }

    @Override
    public void initMenuViewRV(RecyclerView food_rv, RecyclerView step_rv, RecyclerView cooking_step_rv, RecyclerView comment_rv, GridView likeGView, ImageView likeIv, ImageView favoriteIv, TextView likeNumIv) {

        food_rv.setAdapter(food_adapter);
        food_rv.setLayoutManager(new GridLayoutManager(mContext, 2));

        step_rv.setAdapter(step_adapter);
        step_rv.setLayoutManager(new LinearLayoutManager(mContext));
        step_rv.setNestedScrollingEnabled(false);

        cooking_step_rv.setAdapter(cooking_step_adapter);
        cooking_step_rv.setLayoutManager(new LinearLayoutManager(mContext));
        cooking_step_rv.setNestedScrollingEnabled(false);


        comment_rv.setAdapter(comment_adapter);
        comment_rv.setLayoutManager(new LinearLayoutManager(mContext));

        this.likeNumTv = likeNumIv;
        this.likeIv = likeIv;
        this.favoriteIv = favoriteIv;

        likeGView.setAdapter(likeGVAdapter);
    }


    @Override
    public void favorite() {
        if (!isLogin()) {
            ToastUtils.showShortToast(App.mContext.getString(R.string.favorites_please_login));
            return;
        }

        if (!isfavorite) {
            Observable o = Network.getMainApi().addFavorites(recipesBean.get_id());
            Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base>() {
                @Override
                public void onNext(Base o) {
                    if (o.getErrorCode() == 0) {
                        ToastUtils.showShortToast(App.mContext.getString(R.string.favorites_success));
                        isfavorite = true;
                        favoriteIv.setImageResource(R.drawable.like_it_dark);
                    }
                }
            }, mContext);
            JxUtils.toSubscribe(o, s);
        } else {
            Observable o = Network.getMainApi().cancelFavorites(recipesBean.get_id());
            Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base>() {
                @Override
                public void onNext(Base o) {
                    if (o.getErrorCode() == 0) {
                        ToastUtils.showShortToast(App.mContext.getString(R.string.favorites_failure));
                        isfavorite = false;
                        favoriteIv.setImageResource(R.drawable.like_it);
                    }
                }
            }, mContext);
            JxUtils.toSubscribe(o, s);
        }
    }

    @Override
    public void like() {

        if (!isLogin()) {
            ToastUtils.showShortToast(App.mContext.getString(R.string.addFollower_please_login));
            return;
        }
        if (!islike) {
            Observable o = Network.getMainApi().addFollower(recipesBean.get_id());
            Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base>() {
                @Override
                public void onNext(Base o) {
                    if (o.getErrorCode() == 0) {
                        ToastUtils.showShortToast(App.mContext.getString(R.string.addFollower_success));
                        islike = true;
                        //menuAloneView.onIsLike(islike);
                        refreshLikeData();
                        likeGVAdapter.notifyDataSetChanged();
                        likeIv.setImageResource(R.drawable.love_it_dark);
                    }
                }
            }, mContext);
            JxUtils.toSubscribe(o, s);
        } else {
            Observable o = Network.getMainApi().cancelFollower(recipesBean.get_id());
            Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base>() {
                @Override
                public void onNext(Base o) {
                    if (o.getErrorCode() == 0) {
                        ToastUtils.showShortToast(App.mContext.getString(R.string.addFollower_failure));
                        islike = false;
                        //menuAloneView.onIsLike(islike);
                        refreshLikeData();
                        likeGVAdapter.notifyDataSetChanged();
                        likeIv.setImageResource(R.drawable.love_it_icon);
                    }
                }
            }, mContext);
            JxUtils.toSubscribe(o, s);
        }
    }

    private void refreshLikeData() {
        int likesSize = likes.size();
        if(likesSize > 0 && (islike == false)) {

            for (int i = 0; i < likesSize; i ++) {
                if(userName.equals(likes.get(i).getUser().getName())) {
                    likes.remove(i);
                    likesSize--;
                    likeNumTv.setText(likesSize + App.mContext.getString(R.string.like_num));
                    return;
                }
            }
        } else if(islike == true) {
            // String headUrl

            if(userHeadUrl != null) {
            /*    like.setName(name);
                like.setHeadUrl(userHeadUrl);*/
                Like like = new Like();
                Like.LikeUser likeUser = like.new LikeUser();
                likeUser.setName(userName);
                likeUser.setHeadUrl(userHeadUrl);
                like.setDate("5201314");
                like.setUser(likeUser);
                likes.add(0, like);
                likesSize++ ;
                likeNumTv.setText(likesSize + App.mContext.getString(R.string.like_num));
            }
        }
    }


    @Override
    public boolean isFavorite() {
        boolean l = false;
        //// 判断是否favorite
        int favoritesSize = favorites.size();
        if(favoritesSize == 0) {
            l = false;
        } else {
            for(Favorites favorite : favorites) {
                if (favorite.getUser() != null)
                    if (userName.equals(favorite.getUser().getName()))
                        l = true;
            }
        }

        return l;
    }



    @Override
    public void getMyMenu(int path, int index) {
        Observable o = Network.getMainApi().listByOwner(path, index);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<MyCollectionRecipes>>() {
            @Override
            public void onNext(Base<MyCollectionRecipes> o) {
                if (o.getErrorCode() == 0) {
                    //RecipesBeans = o.getRes().sgetList();
                    myMenuListView.onGetMyMenuSuccess(o.getRes().getList());
                }


            }
        }, mContext);

        JxUtils.toSubscribe(o, s);
    }



    @Override
    public boolean isLike() {
        boolean l = false;
        //// 判断是否 like
        int likeSize = recipesBean.getLikes().size();
        if (recipesBean.getLikes().size() == 0) {
            l = false;

        } else {
            for (Like like : recipesBean.getLikes()) {
                if(like.getUser().getName() != null)
                    if (userName.equals(like.getUser().getName()))
                        l = true;
            }

        }
        likeNumTv.setText(likeSize + App.mContext.getString(R.string.like_num));
        return l;
    }

    @Override
    public boolean isOwner() {
        String name = App.spUtils.getString(App.mContext.getString(R.string.name));
        if (recipesBean.getOwner().getOwnerUid().getName().equals(name))
            return true;
        return false;
    }

    void refreshRV(final PullLoadMoreRecyclerView rv) {
        s_recipesList = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {

                if (o.getErrorCode() == 0) {
                    list = o.getRes().getList();
                    adapter.notifyDataSetChanged();
                    rv.setPullLoadMoreCompleted();

                }
            }
        }, mContext);
        JxUtils.toSubscribe(o_recipesList, s_recipesList);
    }

    void loadMore(final PullLoadMoreRecyclerView rv) {
        count = count + 5;
        o_recipesList = Network.getMainApi().getRecipesList(index, count);
        s_recipesList = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {

                if (list.size() == o.getRes().getList().size()) {
                    Toast.makeText(mContext, "没有更多了", Toast.LENGTH_SHORT).show();
                } else {
                    list = o.getRes().getList();
                    adapter.notifyDataSetChanged();
                }
                rv.setPullLoadMoreCompleted();
            }
        }, mContext);

        JxUtils.toSubscribe(o_recipesList, s_recipesList);

    }



    class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
        public CommentAdapter() {
            super(R.layout.view_menu_comment_item, comments);
        }

        @Override
        public int getItemCount() {
            return 2;//显示数量
        }

        @Override
        protected void convert(BaseViewHolder holder, Comment comment) {
            holder.setText(R.id.tv_comment, comment.getComment());
            holder.setText(R.id.tv_user_name, comment.getUid().getName());

            holder.setText(R.id.tv_date, TimeUtils.date2String(comment.getTime(), "yyyy-MM-dd"));

            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + comment.getUid().getHeadUrl(), (ImageView) holder.getView(R.id.iv_head), mContext, true);


        }
    }

    class LikeGVAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return likes.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menuview_gridview_item, null);
            try {
                ImageView userHeadIv = (ImageView) convertView.findViewById(R.id.lite_head_iv);
                //String url, ImageView imageView, Context context, boolean isCircle

                ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + likes.get(position).getUser().getHeadUrl(), userHeadIv, mContext, true);
                userHeadIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent likeIntent = new Intent(mContext, LikeListActivity.class);
                        likeIntent.putExtra("likes", (Serializable) likes);
                        mContext.startActivity(likeIntent);
                    }
                });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }


    class LikeAdapter extends BaseQuickAdapter<Like, BaseViewHolder> {

        public LikeAdapter() {
            super(R.layout.view_menu_like_show_item, likes);
        }

        @Override
        protected void convert(BaseViewHolder holder, Like o) {
            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + o.getUser().getHeadUrl(), (ImageView) holder.getView(R.id.iv_head), mContext, true);
        }

    }

    class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {
        LayoutInflater layoutInflater;
        Context context;

        public StepAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StepHolder(layoutInflater.inflate(R.layout.view_menu_step_show_item, parent, false));

        }

        @Override
        public void onBindViewHolder(StepHolder holder, int position) {

            holder.tvStepNo.setText(String.format("准备步骤 %d", position + 1));
            holder.tvDec.setText(mSteps.get(position).getDescribe());
            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + mSteps.get(position).getImgSrc(), holder.ivPicture, context, false);

        }

        @Override
        public int getItemCount() {
            return mSteps == null ? 0 : mSteps.size();
        }

        class StepHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_step_no)
            TextView tvStepNo;
            @Bind(R.id.iv_picture)
            ImageView ivPicture;
            @Bind(R.id.tv_dec)
            TextView tvDec;

            StepHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodsHolder> {
        LayoutInflater layoutInflater;
        Context context;

        public FoodsAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public FoodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FoodsHolder(layoutInflater.inflate(R.layout.view_menu_food_show_item, parent, false));

        }

        @Override
        public void onBindViewHolder(FoodsHolder holder, int position) {

            holder.mTvFoodType.setText(mFoods.get(position).getFoodType());
            holder.mTvAmount.setText(String.valueOf(mFoods.get(position).getAmount()));
            holder.mTvAmount.setText(mFoods.get(position).getAmount() + "克");
        }

        @Override
        public int getItemCount() {
            return mFoods == null ? 0 : mFoods.size();
        }

        class FoodsHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_food_type)
            TextView mTvFoodType;
            @Bind(R.id.tv_amount)
            TextView mTvAmount;


            FoodsHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    class CookingStepAdapter extends RecyclerView.Adapter<CookingStepAdapter.CookingHolder> {
        LayoutInflater layoutInflater;
        Context context;


        public CookingStepAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public CookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CookingHolder(layoutInflater.inflate(R.layout.view_menu_cooking_step_show_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CookingHolder holder, int position) {
            holder.mTvStepNo.setText(String.format("烹饪步骤 %d", position + 1));
            holder.mTvDec.setText(mCookingSteps.get(position).getDescribe());
            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + mCookingSteps.get(position).getImgSrc(), holder.mIvPicture, context, false);

            holder.mTvTemperature.setText(String.format("%d℃", mCookingSteps.get(position).getTemperature()));
            holder.mTvDuration.setText(String.format("%d分钟", mCookingSteps.get(position).getDuration()));

            String power = "";
            switch (mCookingSteps.get(position).getPower()) {
                case 1:
                    power = "文火";
                    break;
                case 2:
                    power = "中火";
                    break;
                case 3:
                    power = "大火";
                    break;
            }
            holder.mTvFire.setText(power);
        }

        @Override
        public int getItemCount() {
            return mCookingSteps == null ? 0 : mCookingSteps.size();
        }


        class CookingHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_step_no)
            TextView mTvStepNo;
            @Bind(R.id.iv_picture)
            ImageView mIvPicture;
            @Bind(R.id.tv_dec)
            TextView mTvDec;

            @Bind(R.id.tv_fire)
            TextView mTvFire;
            @Bind(R.id.tv_temperature)
            TextView mTvTemperature;
            @Bind(R.id.tv_duration)
            TextView mTvDuration;

            public CookingHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

}
