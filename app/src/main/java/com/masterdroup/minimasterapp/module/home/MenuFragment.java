package com.masterdroup.minimasterapp.module.home;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.module.menu.MenuListActivity;
import com.masterdroup.minimasterapp.module.menu.MenuViewActivity;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 11473 on 2016/12/19.
 */

public class MenuFragment extends Fragment {

    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.iv_center1)
    ImageView ivCenter1;
    @Bind(R.id.iv_center2)
    ImageView ivCenter2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_menu_home, container, false);
        ButterKnife.bind(this, view);

        List<String> images = new ArrayList<>();
        images.add("https://images.pexels.com/photos/203089/pexels-photo-203089.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb");
        images.add("https://images.pexels.com/photos/139746/pexels-photo-139746.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb");
        images.add("https://images.pexels.com/photos/7390/pexels-photo.jpg?w=1260&h=750&auto=compress&cs=tinysrgb");
        images.add("https://images.pexels.com/photos/33406/pexels-photo.jpg?w=1260&h=750&auto=compress&cs=tinysrgb");


        //设置图片加载器
        banner.setImageLoader(new ImageLoader());
        //设置图片集合
        banner.setImages(images);

        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                startActivity(new Intent(view.getContext(), MenuViewActivity.class));
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        ImageLoader.getInstance().displayGlideImage("https://images.pexels.com/photos/5485/handwritten-italian-marketing-menu.jpg?w=1260&h=750&auto=compress&cs=tinysrgb", ivCenter1, view.getContext(), false);
        ImageLoader.getInstance().displayGlideImage("https://images.pexels.com/photos/3498/italian-pizza-restaurant-italy.jpg?w=1260&h=750&auto=compress&cs=tinysrgb", ivCenter2, view.getContext(), false);
        ivCenter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MenuListActivity.class));
            }
        });

        ivCenter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MenuListActivity.class));
            }
        });


        return view;
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
}
