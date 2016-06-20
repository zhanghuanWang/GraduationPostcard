package com.ywg.graduationpostcard.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ywg.graduationpostcard.R;
import com.ywg.graduationpostcard.utils.AppUtils;
import com.ywg.graduationpostcard.utils.BitmapUtil;
import com.ywg.graduationpostcard.utils.IntentUtils;
import com.ywg.graduationpostcard.utils.ToastUtil;

/**
 *  关于Activity
 */
public class AboutActivity extends BaseToolbarActivity implements View.OnClickListener {

    private TextView mTvAppVersion;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private TextView mTvGithub;

    private Context mContext;

    private NestedScrollView mNestedScrollView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initValues() {
        mContext = AboutActivity.this;
    }

    @Override
    protected void initViews() {
        initToolBar(getString(R.string.action_about), true);

        mTvAppVersion = (TextView) findViewById(R.id.tv_app_version);
        mTvAppVersion.setText("当前版本号：" + AppUtils.getAppVersionName(this));

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        mCollapsingToolbarLayout.setTitle(getString(R.string.action_about));

        mNestedScrollView = (NestedScrollView) findViewById(R.id.scrollView);
        mTvGithub = (TextView) findViewById(R.id.tv_github);
        mTvGithub.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_about;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Bitmap bitmap = BitmapUtil.takeScreenShot(this);
                new ShareImageTask().execute(bitmap);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_github:
                IntentUtils.startToWebActivity(this, null, getString(R.string.github), getString(R.string.github_my));
                break;
        }
    }

    /**
     * 分享图片的异步线程
     */
    private class ShareImageTask extends AsyncTask<Bitmap, Void, Uri> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //显示dialog
            showProgressDialog("正在创建分享...");
        }

        @Override
        protected Uri doInBackground(Bitmap... params) {
            return BitmapUtil.saveImageToGallery(mContext, params[0]);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            if (uri != null) {
                IntentUtils.startAppShareImage(mContext, "这应用不错", uri);
                dissmissProgressDialog();
            } else {
                ToastUtil.showLong(mContext, "分享失败");
            }
        }
    }
}