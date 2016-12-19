package com.masterdroup.minimasterapp.module.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masterdroup.minimasterapp.R;
import com.masterdroup.minimasterapp.util.ImageLoader;
import com.youth.banner.Banner;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_home, container, false);
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
        //banner设置方法全部调用完毕时最后调用
        banner.start();

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
