package com.masterdroup.minimasterapp.module.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.masterdroup.minimasterapp.R;

import butterknife.ButterKnife;

/**
 * Created by xiaoQ on 2017/5/6.
 */
/** 商城页面 */
public class ShopFragment extends Fragment {

    WebView mWebView;

    public static final String SHOP_URL = "https://momscook.tmall.com/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // View view = new View(getActivity());
        View view = inflater.inflate(R.layout.fragment_shop_view, container, false);
        ButterKnife.bind(this, view);
        mWebView = (WebView) view.findViewById(R.id.webView);
        return  view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        mWebView.loadUrl(SHOP_URL);
        //覆盖webView默认使用第三方或系统默认浏览器打开网页的行为，使用网页用webView打开
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制区webView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;

            }
        });
        super.onResume();
    }
}
