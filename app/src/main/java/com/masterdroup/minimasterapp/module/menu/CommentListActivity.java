package com.masterdroup.minimasterapp.module.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.blankj.utilcode.utils.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.masterdroup.minimasterapp.Constant;
import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.api.Network;
import com.masterdroup.minimasterapp.model.Base;
import com.masterdroup.minimasterapp.model.Comment;
import com.masterdroup.minimasterapp.model.Recipes;
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

public class CommentListActivity extends AppCompatActivity {

    @Bind(R.id.rv_menu_comment)
    RecyclerView rvMenuComment;
    List<Comment> comments = new ArrayList<>();
    String menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        ButterKnife.bind(this);

        getComment();
    }

    private void getComment() {
        menuId = getIntent().getStringExtra("_id");
        Observable o = Network.getMainApi().getRecipesDetail(menuId);
        Subscriber s = new ProgressSubscriber(new ProgressSubscriber.SubscriberOnNextListener<Base<Recipes.RecipesBean>>() {
            @Override
            public void onNext(Base<Recipes.RecipesBean> base) {
                if (base.getErrorCode() == 0) {

                    comments = base.getRes().getComment();
                    ivitView();
                }
            }
        }, this);

        JxUtils.toSubscribe(o, s);

    }

    private void ivitView() {
        rvMenuComment.setLayoutManager(new LinearLayoutManager(this));
        rvMenuComment.setAdapter(new CommentAdapter());

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

    @OnClick(R.id.iv_return)
    public void onViewClicked() {
        finish();
    }
}
