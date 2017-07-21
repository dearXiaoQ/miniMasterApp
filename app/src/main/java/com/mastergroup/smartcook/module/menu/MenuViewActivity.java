package com.mastergroup.smartcook.module.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.DetailRecipes;
import com.mastergroup.smartcook.model.Like;
import com.mastergroup.smartcook.util.ImageLoader;
import com.mastergroup.smartcook.util.Utils;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**  菜谱详情页 */
public class MenuViewActivity extends Activity implements Contract.MenuAloneView {


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_cover)
    ImageView ivCover;
    /*@Bind(R.id.tv_menu_name)
    TextView tvMenuName;* /
   /* @Bind(R.id.tv_menu_info)
    TextView tvMenuInfo;*/
    @Bind(R.id.iv_user_head)
    ImageView ivUserHead;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.like_gv)
    GridView gridView;


    @Bind(R.id.tv_menu_note)
    TextView tvMenuNote;

    @Bind(R.id.rv_food)
    RecyclerView rv_food;

    @Bind(R.id.rv_step)
    RecyclerView rv_step;

    @Bind(R.id.tv_more_button)
    TextView mTvMoreButton;

    @Bind(R.id.rv_cooking_step)
    RecyclerView rv_cookingStep;

    Contract.Presenter mPresenter;

    @Bind(R.id.vsv)
    NestedScrollView mVsv;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.rv_like)
    RecyclerView rv_like;
    @Bind(R.id.favorite)
    ImageView favoriteIv;


    /**
     * 菜谱id
     */
    String recipesBeanID;
    String userHeadUrl;

    @Bind(R.id.rv_menu_comment)
    RecyclerView rvMenuComment;
    @Bind(R.id.tv_comment_count)
    TextView tvCommentCount;
    @Bind(R.id.ll_comment)
    LinearLayout llComment;
    @Bind(R.id.like_iv)
    ImageView likeIv;
    @Bind(R.id.like_num_tv)
    TextView likeNumTv;
    @Bind(R.id.comment_btn)
    Button commentBtn;

    @Bind(R.id.more_iv)
    ImageView moreIv;

    EditText commentEt;
    TextView sendTv;
    /**
     * 点赞列表
     */
    List<Like> likes;
    /**
     * 底部评论输入窗口（dialog）
     */
    BottomSheetDialog bottomSheetDialog;


    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);
        mContext = this;
        ButterKnife.bind(this);
        new MenuPresenter(this);
        mPresenter.start();
        initView();
        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.gettingData(recipesBeanID);
    }

    private void initView() {
        mTvMoreButton.setVisibility(View.VISIBLE);

        mVsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
            }
        });

    }

    private void initData() {

        mPresenter.initMenuViewRV(rv_food, rv_step, rv_cookingStep, rvMenuComment, gridView, likeIv, favoriteIv, likeNumTv);

        recipesBeanID = getIntent().getStringExtra("_id");

        mPresenter.gettingData(recipesBeanID);
    }


    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = Utils.checkNotNull(presenter);
    }


    @OnClick({R.id.more_iv, R.id.iv_return, R.id.fab, R.id.tv_more_button, R.id.tv_comment_count, R.id.like_iv, R.id.favorite, R.id.share, R.id.comment_btn})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_return:
                finish();
                break;

            case R.id.fab:
                Intent fabIntent = new Intent(MenuViewActivity.this, DeviceSelectActivity.class);
                fabIntent.putExtra("_id", recipesBeanID);
                startActivity(fabIntent);
                break;

            case R.id.tv_more_button:

                break;

            case R.id.like_iv:
                mPresenter.like();
                break;

            case R.id.tv_comment_count:
                Intent intent = new Intent(MenuViewActivity.this, CommentListActivity.class);
                intent.putExtra("_id", recipesBeanID);
                startActivity(intent);
                break;

            case R.id.favorite:
                mPresenter.favorite();
                break;

            case R.id.share:
                mPresenter.share();
                break;

            case R.id.comment_btn:
                showBottomCommentView();
                break;

            case R.id.more_iv:
                mPresenter.jumpLikeView();
                break;

        }

    }

    /**
     * 底部弹窗方法
     */
    private void showBottomCommentView() {

        if (bottomSheetDialog == null) {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.remark_toast_item, null);
            bottomSheetDialog = new BottomSheetDialog(mContext);
            bottomSheetDialog.setContentView(mView);
            bottomSheetDialog.setCancelable(true);

            commentEt = (EditText) mView.findViewById(R.id.comment_et);
            sendTv = (TextView) mView.findViewById(R.id.send_tv);
            /** 发表评论 */
            sendTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String commentStr = commentEt.getText().toString().trim();
                    if( !(commentStr.equals("")) ) {
                        mPresenter.sendComment(commentStr, recipesBeanID);
                        commentEt.setText("");
                        bottomSheetDialog.cancel();
                        mPresenter.getComment(recipesBeanID, tvCommentCount);
                    }
                }
            });
        }

        bottomSheetDialog.show();
        //弹出键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) commentEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(commentEt, 0);
            }

        }, 50);


    }

    @Override
    public void settingData(DetailRecipes.RecipesBean recipesBean) {
//        tvTitle.setText(recipesBean.getName());
        //      tvMenuName.setText(recipesBean.getName());
        tvUserName.setText(recipesBean.getOwner().getOwnerUid().getName());
        userHeadUrl = recipesBean.getDetail().getImgSrc();
        ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + userHeadUrl, ivCover, this, false);
        ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + recipesBean.getOwner().getOwnerUid().getHeadUrl(), ivUserHead, this, true);
        tvMenuNote.setText(recipesBean.getDetail().getDescribe());


        if (recipesBean.getComment().size() == 0) {
            tvCommentCount.setText(String.format("评论"));
        } else
            tvCommentCount.setText(String.format("%s条评论", recipesBean.getComment().size()));

    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onIsOwner(boolean o) {
        //是否隐藏点赞按键
        if (o)
            mTvMoreButton.setVisibility(View.INVISIBLE);
        else mTvMoreButton.setVisibility(View.VISIBLE);

    }

    @Override
    public void onIsLike(boolean o) {
        //根据是否点赞去设置点赞按键样式
        if (o)
            mTvMoreButton.setText(getString(R.string.liking));
        else
            mTvMoreButton.setText(getString(R.string.like));
        mPresenter.reLike(recipesBeanID);
    }

    @Override
        public Bitmap getCoverBitmap() {
        Bitmap bmp = convertViewToBitmap(ivCover);
        return bmp;
    }



    public Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }



}

