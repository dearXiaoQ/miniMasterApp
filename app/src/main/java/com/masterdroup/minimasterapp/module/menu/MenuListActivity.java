package com.masterdroup.minimasterapp.module.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.model.Menu;
import com.masterdroup.minimasterapp.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuListActivity extends Activity {

    RecyclerView recyclerView;

    List<Menu> menus = new ArrayList<>();
    ThisAdapter thisAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        ButterKnife.bind(this);


        Menu menu = new Menu();
        menu.score = "2.0分";
        menu.menu_name = "面包";
        menu.user_name = "大人";
        menu.cover_url = "https://images.pexels.com/photos/5506/bread-food-salad-sandwich.jpg?w=1260&h=750&auto=compress&cs=tinysrgb";
        menu.head_url = "https://static.pexels.com/users/avatars/2656/jaymantri-693-medium.jpeg";
        menus.add(menu);


        Menu menu2 = new Menu();
        menu2.score = "9.9分";
        menu2.menu_name = "Sponsored";
        menu2.user_name = "Josh Sorenson";
        menu2.cover_url = "https://images.pexels.com/photos/7401/pexels-photo.jpg?w=1260&h=750&auto=compress&cs=tinysrgb";
        menu2.head_url = "https://static.pexels.com/users/avatars/11929/josh-sorenson-243-medium.jpeg";
        menus.add(menu2);

        Menu menu3 = new Menu();
        menu3.score = "9.9分";
        menu3.menu_name = "Sponsored";
        menu3.user_name = "Daniel Lindstrom";
        menu3.cover_url = "https://images.pexels.com/photos/14737/pexels-photo.jpg?w=1260&h=750&auto=compress&cs=tinysrgb";
        menu3.head_url = "https://www.gravatar.com/avatar/156096673227a23cbbed3bfa9784167e?s=200&d=mm";
        menus.add(menu3);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        thisAdapter = new ThisAdapter(this);
        recyclerView.setAdapter(thisAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @OnClick(R.id.iv_return)
    public void onClick() {
        finish();
    }


    class ThisAdapter extends RecyclerView.Adapter<ThisAdapter.ThisViewHolder> {
        LayoutInflater layoutInflater;
        Context context;

        ThisAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public ThisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ThisViewHolder(layoutInflater.inflate(R.layout.view_menu_item, parent, false));

        }

        @Override
        public void onBindViewHolder(ThisViewHolder holder, int position) {
            Menu menu = menus.get(position);
            holder.score.setText(menu.getScore());
            holder.menu_name.setText(menu.getMenu_name());
            holder.user_name.setText(menu.getUser_name());
            ImageLoader.getInstance().displayGlideImage(menu.getCover_url(), holder.iv_cover, context, false);
            ImageLoader.getInstance().displayGlideImage(menu.getHead_url(), holder.iv_head, context, true);
            holder.iv_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context, MenuViewActivity.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            return menus == null ? 0 : menus.size();
        }

        public class ThisViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_menu_score)
            TextView score;
            @Bind(R.id.tv_menu_name)
            TextView menu_name;
            @Bind(R.id.tv_menu_user_name)
            TextView user_name;
            @Bind(R.id.iv_cover)
            ImageView iv_cover;
            @Bind(R.id.iv_head)
            ImageView iv_head;

            ThisViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }


    }
}
