package com.ywg.graduationpostcard.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ywg.graduationpostcard.utils.AppManager;

/**
 * Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected final  String LOG_TAG = this.getClass().getSimpleName();

    private ProgressDialog mProgressDialog;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            HandleMessage(msg);
        }
    };

    /**
     * 复写来处理mHandler发来的消息
     *
     * @param msg
     */
    public void HandleMessage(Message msg) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()");
        setContentView(getLayoutId());
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);

        initDialog();

        initValues();

        initViews();

        initData();

    }

    /**
     * 设置布局
     *
     * @return the id of layout
     */
    protected abstract int getLayoutId();

    /**
     * 初始化一些变量
     */
    protected abstract void initValues();

    /**
     * 初始化控件
     */
    protected abstract void initViews();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * set the id of menu
     *
     * @return if values is less then zero ,and the activity will not show menu
     */
    protected int getMenuRes() {
        return -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuRes() < 0) {
            return true;
        }
        getMenuInflater().inflate(getMenuRes(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //ToolBar左上角图标
                //don't use finish() and use onBackPressed() will be a good practice , trust me !
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化对话框
     */
    private void initDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false); // 设置是否可以通过点击Back键取消
        mProgressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
    }

    /**
     * 显示对话框 无提示内容
     */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            initDialog();
            mProgressDialog.show();
        } else {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    /**
     * 显示对话框 有提示内容
     */
    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            initDialog();
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.setMessage(message);
                mProgressDialog.show();
            }
        }
    }

    /**
     * 销毁对话框
     */
    public void dissmissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
        dissmissProgressDialog();
    }

}