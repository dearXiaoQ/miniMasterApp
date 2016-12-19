package com.masterdroup.minimasterapp.module.menu;

import android.app.Activity;
import android.content.Context;
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

public class MenuListActivity extends Activity {

    RecyclerView recyclerView;

    List<Menu> menus = new ArrayList<>();
    ThisAdapter thisAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);


        Menu menu = new Menu();
        menu.score = "2";
        menu.menu_name = "jun";
        menu.user_name = "ten";
        menu.cover_url = "https://images.pexels.com/photos/5506/bread-food-salad-sandwich.jpg?w=1260&h=750&auto=compress&cs=tinysrgb";
        menu.head_url = "https://static.pexels.com/users/avatars/2656/jaymantri-693-medium.jpeg";
        menus.add(menu);


        Menu menu2 = new Menu();
        menu.score = "2";
        menu.menu_name = "jun";
        menu.user_name = "ten";
        menu.cover_url = "https://images.pexels.com/photos/5506/bread-food-salad-sandwich.jpg?w=1260&h=750&auto=compress&cs=tinysrgb";
        menu.head_url = "https://static.pexels.com/users/avatars/2656/jaymantri-693-medium.jpeg";
        menus.add(menu2);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        thisAdapter = new ThisAdapter(this);
        recyclerView.setAdapter(thisAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }


    class ThisAdapter extends RecyclerView.Adapter<ThisAdapter.ThisViewHolder> {
        LayoutInflater layoutInflater;

        ThisAdapter(Context context) {
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
            ImageLoader.getInstance().displayGlideImage(menu.getCover_url(), holder.iv_cover);
            ImageLoader.getInstance().displayGlideImage(menu.getHead_url(), holder.iv_head);

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
