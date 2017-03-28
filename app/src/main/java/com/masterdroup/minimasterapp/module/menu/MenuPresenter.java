package com.masterdroup.minimasterapp.module.menu;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
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
import com.masterdroup.minimasterapp.model.Food;
import com.masterdroup.minimasterapp.model.Like;
import com.masterdroup.minimasterapp.model.Recipes;
import com.masterdroup.minimasterapp.model.RecipesList;
import com.masterdroup.minimasterapp.module.progress.ProgressSubscriber;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.masterdroup.minimasterapp.util.JxUtils;
import com.masterdroup.minimasterapp.util.Utils;
import com.masterdroup.minimasterapp.view.MenuListRVAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

import static android.widget.LinearLayout.HORIZONTAL;
import static com.masterdroup.minimasterapp.util.Utils.isLogin;

/**
 * Created by 11473 on 2016/12/21.
 */

public class MenuPresenter implements Contract.Presenter {
    Contract.MenuAloneView menuAloneView;
    Contract.MenuListView menuListView;


    Context mContext;
    MenuListRVAdapter adapter;
    FoodsAdapter food_adapter;
    StepAdapter step_adapter;
    LikeAdapter like_adapter;
    CookingStepAdapter cooking_step_adapter;
    CommentAdapter comment_adapter;
    public static List<Recipes.RecipesBean> list;


    List<DescribeStep> mSteps = new ArrayList<>();
    List<Food> mFoods = new ArrayList<>();
    List<CookingStep> mCookingSteps = new ArrayList<>();
    List<Like> likes = new ArrayList<>();
    List<Comment> comments = new ArrayList<>();

    int index = 0;//从第index个开始获取
    int count = 3;//页数

    Observable o_recipesList;
    Subscriber s_recipesList;

    Recipes.RecipesBean recipesBean;

    boolean islike;

    @Override
    public void start() {

        list = new ArrayList<>();
        adapter = new MenuListRVAdapter(mContext);
        food_adapter = new FoodsAdapter(mContext);
        step_adapter = new StepAdapter(mContext);
        cooking_step_adapter = new CookingStepAdapter(mContext);
        like_adapter = new LikeAdapter();
        like_adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        comment_adapter = new CommentAdapter();

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


    @Override
    public void gettingData(final String menuId) {
        Observable o = Network.getMainApi().getRecipesDetail(menuId);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<Recipes.RecipesBean>>() {
            @Override
            public void onNext(Base<Recipes.RecipesBean> base) {
                if (base.getErrorCode() == 0) {
                    menuAloneView.settingData(base.getRes());
                    recipesBean = base.getRes();
                    recipesBean.set_id(menuId);
                    mFoods = base.getRes().getFoodList();
                    mSteps = base.getRes().getDescribeSteps();
                    mCookingSteps = base.getRes().getCookingStep();
                    likes = base.getRes().getLikes();
                    comments = base.getRes().getComment();

                    food_adapter.notifyDataSetChanged();

                    step_adapter.notifyDataSetChanged();
                    cooking_step_adapter.notifyDataSetChanged();


                    like_adapter.setNewData(likes);
                    comment_adapter.setNewData(comments);

                    menuAloneView.onIsOwner(isOwner());
                    islike = isLike();
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
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<Recipes.RecipesBean>>() {
            @Override
            public void onNext(Base<Recipes.RecipesBean> base) {
                if (base.getErrorCode() == 0) {
                    likes = base.getRes().getLikes();
                    like_adapter.setNewData(likes);

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
    public void initMenuViewRV(RecyclerView food_rv, RecyclerView step_rv, RecyclerView cooking_step_rv, RecyclerView like_rv, RecyclerView comment_rv) {

        food_rv.setAdapter(food_adapter);
        food_rv.setLayoutManager(new GridLayoutManager(mContext, 2));

        step_rv.setAdapter(step_adapter);
        step_rv.setLayoutManager(new LinearLayoutManager(mContext));
        step_rv.setNestedScrollingEnabled(false);

        cooking_step_rv.setAdapter(cooking_step_adapter);
        cooking_step_rv.setLayoutManager(new LinearLayoutManager(mContext));
        cooking_step_rv.setNestedScrollingEnabled(false);

        like_rv.setAdapter(like_adapter);
//        like_rv.setLayoutManager(new GridLayoutManager(mContext, 2));
        like_rv.setLayoutManager(new LinearLayoutManager(mContext, HORIZONTAL, false));

        comment_rv.setAdapter(comment_adapter);
        comment_rv.setLayoutManager(new LinearLayoutManager(mContext));

    }

    @Override
    public void like() {

        if (!isLogin()) {
            ToastUtils.showShortToast("点赞前请登录");
            return;
        }
        if (!islike) {
            Observable o = Network.getMainApi().addFollower(recipesBean.get_id());
            Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base>() {
                @Override
                public void onNext(Base o) {
                    if (o.getErrorCode() == 0) {
                        ToastUtils.showShortToast("点赞成功");
                        islike = true;
                        menuAloneView.onIsLike(islike);

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
                        ToastUtils.showShortToast("取消点赞成功");
                        islike = false;
                        menuAloneView.onIsLike(islike);
                    }
                }
            }, mContext);
            JxUtils.toSubscribe(o, s);
        }
    }

    @Override
    public boolean isLike() {
        boolean l = false;
        //// 判断是否 like
        String name = App.spUtils.getString(App.mContext.getString(R.string.name));

        if (recipesBean.getLikes().size() == 0)
            l = false;
        else {
            for (Like like : recipesBean.getLikes()) {
                if (name.equals(like.getName()))
                    l = true;

            }

        }
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
        count = count + 1;
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

    class LikeAdapter extends BaseQuickAdapter<Like, BaseViewHolder> {

        public LikeAdapter() {
            super(R.layout.view_menu_like_show_item, likes);
        }

        @Override
        protected void convert(BaseViewHolder holder, Like o) {
            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + o.getHeadUrl(), (ImageView) holder.getView(R.id.iv_head), mContext, true);
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
