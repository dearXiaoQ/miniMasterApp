package com.mastergroup.smartcook.module.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastergroup.smartcook.Constant;
import com.mastergroup.smartcook.R;
import com.mastergroup.smartcook.model.Like;
import com.mastergroup.smartcook.util.ImageLoader;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 菜谱点赞列表 */
public class LikeListActivity extends Activity {

    @Bind(R.id.iv_return)
    ImageView backIv;

    @Bind(R.id.rv)
    PullLoadMoreRecyclerView rv;

    LikeRvAdapter adapter;
    Context mContext;

    List<Like> likes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_list);
        ButterKnife.bind(this);
        mContext = this;
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        likes = (List<Like>) intent.getSerializableExtra("likes");
        rv.setLinearLayout();
        adapter = new LikeRvAdapter();
        rv.setAdapter(adapter);
        rv.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener(){

            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                rv.setPullLoadMoreCompleted();
            }

            @Override
            public void onLoadMore() {
                adapter.notifyDataSetChanged();
                rv.setPullLoadMoreCompleted();
            }
        });
    }


    class LikeRvAdapter extends RecyclerView.Adapter<LikeListActivity.LikeListRvHolder> {

        LayoutInflater layoutInflater;

        private LikeRvAdapter() {
            layoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public LikeListRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LikeListRvHolder(layoutInflater.inflate(R.layout.like_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(LikeListRvHolder holder, int position) {
            try{
                Like.LikeUser like = likes.get(position).getUser();
                ImageLoader.getInstance().displayGlideImage(Constant.BASEURL + like.getHeadUrl(), holder.userHeadIv, mContext, false);
                //holder.timeTv.setText();
                holder.userNameTv.setText(like.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return likes.size();
        }
    }



    @OnClick({R.id.iv_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return :

                this.finish();
                break;
        }
    }

    class LikeListRvHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_head_iv)
        ImageView userHeadIv;

        @Bind(R.id.username_tv)
        TextView userNameTv;

        @Bind(R.id.time_tv)
        TextView timeTv;


        public LikeListRvHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
