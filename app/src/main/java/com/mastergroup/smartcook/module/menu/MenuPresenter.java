package com.mastergroup.smartcook.module.menu;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
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

import com.blankj.utilcode.utils.ImageUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mastergroup.smartcook.App;
import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.api.Network;
import com.mastergroup.smartcook.model.Base;
import com.mastergroup.smartcook.model.Comment;
import com.mastergroup.smartcook.model.CookingStep;
import com.mastergroup.smartcook.model.DescribeStep;
import com.mastergroup.smartcook.model.DetailRecipes;
import com.mastergroup.smartcook.model.Favorites;
import com.mastergroup.smartcook.model.Food;
import com.mastergroup.smartcook.model.Like;
import com.mastergroup.smartcook.model.MyCollectionRecipes;
import com.mastergroup.smartcook.model.Recipes;
import com.mastergroup.smartcook.model.RecipesList;
import com.mastergroup.smartcook.module.progress.ProgressSubscriber;
import com.mastergroup.smartcook.util.ImageLoader;
import com.mastergroup.smartcook.util.JxUtils;
import com.mastergroup.smartcook.util.Utils;
import com.mastergroup.smartcook.view.MenuListRVAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.yuyh.library.imgsel.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;
import rx.Observable;
import rx.Subscriber;

import static com.mastergroup.smartcook.util.Utils.isLogin;

/**
 * Created by 11473 on 2016/12/21.
 */

public class MenuPresenter implements Contract.Presenter {
    Contract.MenuAloneView menuAloneView;
    Contract.MenuListView menuListView;
    Contract.MyMenuListView myMenuListView;
    Contract.FavoriteListView favoriteListView;
    Contract.SearchView searchView;

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
    String shareUrl;
    String recipesName;
    String author;

    RecyclerView rvMenuComment;

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


    public MenuPresenter(Contract.SearchView View) {
        searchView = Utils.checkNotNull(View, "mView cannot be null");
        searchView.setPresenter(this);
        mContext = searchView.getContext();
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
                    shareUrl = base.getRes().getShareUrl();
                    recipesName = base.getRes().getName();
                    food_adapter.notifyDataSetChanged();

                    step_adapter.notifyDataSetChanged();
                    cooking_step_adapter.notifyDataSetChanged();
                    author = base.getRes().getOwner().getOwnerUid().getName();
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

        rvMenuComment = comment_rv;

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
    public void getFavoriteRecipes() {
        Observable o = Network.getMainApi().getFavoritesList();
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<MyCollectionRecipes>>() {
            @Override
            public void onNext(Base<MyCollectionRecipes> o) {
                if (o.getErrorCode() == 0) {
                    favoriteListView.onGetMyFavoriteListSuccess(o.getRes().getList());
                } else {
                    favoriteListView.onGetMyFavoriteListFailure(o.getMessage());
                }
            }
        }, mContext);

        JxUtils.toSubscribe(o, s);
    }


    @Override
    public void share() {
        showShare();
    }

    @Override
    public void search(String searchStr, int index, int count) {
        Observable o = Network.getMainApi().searchRecipesList(searchStr, index, count);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<RecipesList>>() {
            @Override
            public void onNext(Base<RecipesList> o) {
                if (o.getErrorCode() == 0) {
                    //RecipesBeans = o.getRes().sgetList();
                    searchView.onGetRecipesSuccess(o.getRes().getList());
                } else {
                    searchView.onGetRecipesFailure(o.getMessage());
                }
            }
        }, mContext);

        JxUtils.toSubscribe(o, s);

    }


    @Override
    public void sendComment(String commentStr, final String menuId) {
        Comment comment = new Comment();
        comment.setComment(commentStr);


        Observable o = Network.getMainApi().addComment(menuId, comment);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<Recipes.RecipesBean>>() {
            @Override
            public void onNext(Base<Recipes.RecipesBean> base) {
                if (base.getErrorCode() == 0) {

                   // getComment(menuId);

                }
            }
        }, mContext);

        JxUtils.toSubscribe(o, s);

    }


    @Override
    public void getComment(String menuId, final TextView commentCountTv) {
        Observable o = Network.getMainApi().getRecipesDetail(menuId);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<DetailRecipes.RecipesBean>>() {
            @Override
            public void onNext(Base<DetailRecipes.RecipesBean> base) {
                if (base.getErrorCode() == 0) {

                    comments = base.getRes().getComment();
                    comment_adapter = new CommentAdapter();
                    rvMenuComment.setLayoutManager(new LinearLayoutManager(mContext));
                    rvMenuComment.setAdapter(comment_adapter);
                    commentCountTv.setText(String.format("%s条评论", comments.size()));
                }
            }
        }, mContext);

        JxUtils.toSubscribe(o, s);
    }

    @Override
    public void jumpLikeView() {
        Intent likeIntent = new Intent(mContext, LikeListActivity.class);
        likeIntent.putExtra("likes", (Serializable) likes);
        mContext.startActivity(likeIntent);
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
            Collections.reverse(comments);
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


    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(author + "的" + recipesName);
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("smartCook上这个食谱不错哦，你也试试吧！");
        /** 封面图片写到本地 */

      String savePath = saveBitmap(mContext, menuAloneView.getCoverBitmap());

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(savePath);//确保SDcard下面存在此张图片

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(Constant.SHARE_BASEURL + shareUrl);

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(App.mContext.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(mContext);
    }

    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";

    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public  String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + recipesName + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

}
