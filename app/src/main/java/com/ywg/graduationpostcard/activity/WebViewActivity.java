package com.ywg.graduationpostcard.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ywg.graduationpostcard.R;
import com.ywg.graduationpostcard.utils.CommonUtil;
import com.ywg.graduationpostcard.utils.IntentUtils;
import com.ywg.graduationpostcard.utils.NetWorkUtils;

/**
 * Web Activity
 */
public class WebViewActivity extends BaseToolbarActivity {

    private static final String TAG = WebViewActivity.class.getSimpleName();

    //标题
    private String flagTitle;
    private String titleContent;
    private String url;

    private WebView mWebView;

    private ProgressBar mProgressBar;

    private String title;

    private WebSettings mWebSettings;

    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initValues() {
        mContext = WebViewActivity.this;

        initIntent();

        if (TextUtils.isEmpty(flagTitle)) {
            title = titleContent;
        } else {
            title = flagTitle + "+" + titleContent;
        }

    }

    @Override
    protected void initViews() {
        initToolBar("", true);
        mWebView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        initWebView();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_web;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mWebView.reload();
                break;
            case R.id.action_copy_link:
                CommonUtil.copyLink(mContext, url);
                break;
            case R.id.action_open_link:
                IntentUtils.openLinkInBrowser(mContext, url);
                break;
            case R.id.action_share_link:
                IntentUtils.startAppShareText(mContext, mWebView.getUrl());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initIntent() {
        Intent intent = getIntent();
        flagTitle = intent.getStringExtra(IntentUtils.WebTitleFlag);
        titleContent = intent.getStringExtra(IntentUtils.WebTitle);
        url = intent.getStringExtra(IntentUtils.WebUrl);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mWebSettings = mWebView.getSettings();

        //启用javascript支持 用于访问页面中的javascript
        mWebSettings.setJavaScriptEnabled(true);
        //设置脚本是否允许自动打开弹窗
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebSettings.setAppCachePath(getFilesDir() + getPackageName() + "/cache");
        //设置H5的缓存打开,默认关闭
        mWebSettings.setAppCacheEnabled(true);
        // 建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT,无网络时，使用LOAD_CACHE_ELSE_NETWORK
        if (NetWorkUtils.isNetWorkConnected(this)) {
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);   // 根据cache-control决定是否从网络上取数据。
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);   //优先加载缓存
        }

        mWebSettings.setDatabaseEnabled(true);

        //让WebView支持缩放
        mWebSettings.setSupportZoom(true);
        //启用WebView内置缩放功能
        mWebSettings.setBuiltInZoomControls(true);
        //设置webview自适应屏幕大小
        mWebSettings.setUseWideViewPort(true);

        //设置webview自适应屏幕大小
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //设置WebView使用内置缩放机制时，是否展现在屏幕缩放控件上
        mWebSettings.setDisplayZoomControls(false);
        //设置在WebView内部是否允许访问文件
        mWebSettings.setAllowFileAccess(true);

        // 设置编码格式
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        //开启DOM缓存，关闭的话H5自身的一些操作是无效的
        mWebSettings.setDomStorageEnabled(true);
        //WebView硬件加速导致页面渲染闪烁
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }*/
        //设置加载完页面后再加载图片 加快HTML网页加载完成速度
        if (Build.VERSION.SDK_INT >= 19) {
            mWebSettings.setLoadsImagesAutomatically(true);
        } else {
            mWebSettings.setLoadsImagesAutomatically(false);
        }


        //设置了默认在本应用打开，不设置会用浏览器打开的
        mWebView.setWebViewClient(new MyWebViewClient());

        // 设置网页加载的进度条显示
        mWebView.setWebChromeClient(new MyWebChromeClient());

        mWebView.setDownloadListener(new MyWebViewDownLoadListener());

        mWebView.loadUrl(url);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 不采用系统默认浏览器加载URL
            if (url != null) {
                view.loadUrl(url);
            }
            // 当返回true时，控制网页在WebView中打开
            // 当返回false时，控制网页在系统自带的或第三方的浏览器中打开
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!mWebSettings.getLoadsImagesAutomatically()) {
                mWebSettings.setLoadsImagesAutomatically(true);
            }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mWebView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            /**
             *   newProgress 为1-100之间的整数
             newProgress = 100时，意味着网页加载完毕
             newProgress < 100时，意味着网页正在加载
             */
            if (newProgress == 100) {
                // 网页加载完毕，关闭进度提示
                mProgressBar.setVisibility(View.GONE);
            } else {
                // 网页正在加载
                if (mProgressBar.getVisibility() == View.GONE) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            IntentUtils.openLinkInBrowser(mContext, url);
        }

    }

    @Override
    public void onBackPressed() {
        // 如果当前WebView存在访问栈
        if (mWebView.canGoBack()) {
            // 返回页面的上一页
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //保证了webView退出后不再有声音
        if (mWebView != null) {
            mWebView.reload();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

}