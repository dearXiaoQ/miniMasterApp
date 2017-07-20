package com.mastergroup.smartcook.module.home;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mastergroup.smartcook.R;
import com.yuyh.library.imgsel.utils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiaoQ on 2017/5/6.
 */
/** 商城页面 */
public class ShopFragment extends Fragment {


    public static final String SHOP_URL = "https://mall.jd.com/index-603017.html";



    @Bind(R.id.webView)
    WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // View view = new View(getActivity());
        View view = inflater.inflate(R.layout.fragment_shop_view, container, false);
        ButterKnife.bind(this, view);
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
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        //覆盖webView默认使用第三方或系统默认浏览器打开网页的行为，使用网页用webView打开
        mWebView.setWebViewClient(new MyWebViewClient());

        mWebView.setDownloadListener(new MyWebViewDownloadListener());

        super.onResume();
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.i("webViewClient", "url = " + url);
            if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
                return false;
            } else {

                try {
                   /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            }
        }

    }

    @OnClick({R.id.iv_return})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return :
                mWebView.goBack();
                break;

        }
    }


    class MyWebViewDownloadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


}
