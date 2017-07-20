package com.mastergroup.smartcook.module.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.api.Network;
import com.mastergroup.smartcook.model.Base;
import com.mastergroup.smartcook.model.Comment;
import com.mastergroup.smartcook.model.DetailRecipes;
import com.mastergroup.smartcook.model.Recipes;
import com.mastergroup.smartcook.module.progress.ProgressSubscriber;
import com.mastergroup.smartcook.util.ImageLoader;
import com.mastergroup.smartcook.util.JxUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

public class CommentListActivity extends AppCompatActivity implements View.OnClickListener {

    List<Comment> comments = new ArrayList<>();
    String menuId;
    @Bind(R.id.iv_return)
    ImageView ivReturn;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_write)
    TextView tvWrite;
    @Bind(R.id.rv_menu_comment)
    RecyclerView rvMenuComment;
    @Bind(R.id.tv_comment_write)
    EditText tvCommentWrite;
    @Bind(R.id.tv_coles)
    TextView tvClose;
    @Bind(R.id.tv_release)
    TextView tvRelease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        ButterKnife.bind(this);
        menuId = getIntent().getStringExtra("_id");
        getComment();
    }

    private void initView() {


        tvClose.setVisibility(View.GONE);
        ivReturn.setVisibility(View.VISIBLE);
        tvWrite.setVisibility(View.VISIBLE);
        tvRelease.setVisibility(View.GONE);
        tvTitle.setText("全部评论");
        tvCommentWrite.setVisibility(View.GONE);
        rvMenuComment.setVisibility(View.VISIBLE);

        tvClose.setOnClickListener(this);
        ivReturn.setOnClickListener(this);
        tvWrite.setOnClickListener(this);
        tvRelease.setOnClickListener(this);
    }

    private void showWriteComment() {
        tvClose.setVisibility(View.VISIBLE);
        ivReturn.setVisibility(View.GONE);
        tvWrite.setVisibility(View.GONE);
        tvRelease.setVisibility(View.VISIBLE);
        tvCommentWrite.setVisibility(View.VISIBLE);
        rvMenuComment.setVisibility(View.GONE);
        tvTitle.setText("写评论");
    }

    private void getComment() {

        Observable o = Network.getMainApi().getRecipesDetail(menuId);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<DetailRecipes.RecipesBean>>() {
            @Override
            public void onNext(Base<DetailRecipes.RecipesBean> base) {
                if (base.getErrorCode() == 0) {

                    comments = base.getRes().getComment();
                    Collections.reverse(comments);
                    rvMenuComment.setLayoutManager(new LinearLayoutManager(CommentListActivity.this));
                    rvMenuComment.setAdapter(new CommentAdapter());
                    initView();
                }
            }
        }, this);

        JxUtils.toSubscribe(o, s);

    }

    private void onReleaseComment() {
        Comment comment = new Comment();
        comment.setComment(tvCommentWrite.getText().toString());


        Observable o = Network.getMainApi().addComment(menuId, comment);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<Recipes.RecipesBean>>() {
            @Override
            public void onNext(Base<Recipes.RecipesBean> base) {
                if (base.getErrorCode() == 0) {

                    getComment();

                }
            }
        }, this);

        JxUtils.toSubscribe(o, s);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_return)
            finish();
        if (id == R.id.tv_coles)
            initView();
        if (id == R.id.tv_write)
            showWriteComment();
        if (id == R.id.tv_release)
            onReleaseComment();

    }


    class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
        public CommentAdapter() {
            super(R.layout.view_menu_comment_item, comments);
        }

        @Override
        protected void convert(BaseViewHolder holder, Comment comment) {
            holder.setText(R.id.tv_comment, comment.getComment());
            holder.setText(R.id.tv_user_name, comment.getUid().getName());

            holder.setText(R.id.tv_date, TimeUtils.date2String(comment.getTime(), "yyyy-MM-dd"));

            ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + comment.getUid().getHeadUrl(), (ImageView) holder.getView(R.id.iv_head), mContext, true);


        }
    }
}
