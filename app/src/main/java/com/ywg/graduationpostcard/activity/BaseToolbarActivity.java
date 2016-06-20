package com.ywg.graduationpostcard.activity;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.animation.DecelerateInterpolator;

import com.ywg.graduationpostcard.R;

/**
 * 带Toolbar的Activity基类
 */

public abstract class BaseToolbarActivity extends BaseActivity {


    private Toolbar mToolbar;

    private AppBarLayout mAppBarLayout;

    protected boolean isToolBarHiding = false;

    /**
     * 初始化Toolbar
     */
    protected void initToolBar(String title, boolean isBack) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new NullPointerException("please add a Toolbar in your layout.");
        }

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(title);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(isBack); //设置左上角图标显示
            //ab.setHomeAsUpIndicator(icon); //自定义返回箭头
        }

    }

    protected void hideOrShowToolBar() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mAppBarLayout.animate()
                .translationY(isToolBarHiding ? 0 : -mAppBarLayout.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        isToolBarHiding = !isToolBarHiding;
    }

    /**
     * 设置 home icon 是否可见
     * @return
     */
    protected boolean canBack(){
        return true;
    }


}
