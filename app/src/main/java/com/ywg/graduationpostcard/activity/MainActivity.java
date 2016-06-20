package com.ywg.graduationpostcard.activity;

import android.content.Intent;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.ywg.graduationpostcard.R;
import com.ywg.graduationpostcard.adapter.FancyCoverFlowSampleAdapter;
import com.ywg.graduationpostcard.constant.Constants;
import com.ywg.graduationpostcard.utils.IntentUtils;
import com.ywg.graduationpostcard.view.FancyCoverFlow.FancyCoverFlow;

public class MainActivity extends BaseToolbarActivity {

    private FancyCoverFlow mFancyCoverFlow;

    @Override
    public void HandleMessage(Message msg) {
        super.HandleMessage(msg);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initValues() {
        setupFlowWidthAndHeight();

    }

    @Override
    protected void initViews() {
        initToolBar(getString(R.string.app_name), false);
        mFancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
        mFancyCoverFlow.setAdapter(new FancyCoverFlowSampleAdapter());
        mFancyCoverFlow.setUnselectedAlpha(0.9f);//
        mFancyCoverFlow.setUnselectedSaturation(1f);//透明度
        mFancyCoverFlow.setUnselectedScale(0.5f);//深度差
        mFancyCoverFlow.setSpacing(0);
        mFancyCoverFlow.setMaxRotation(0);
        mFancyCoverFlow.setScaleDownGravity(0.2f);//高度差
        mFancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
        mFancyCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditPostcardActivity.class);
                intent.putExtra("styleCode", position);
                startActivity(intent);
            }
        });
    }

    private void setupFlowWidthAndHeight() {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Constants.CoverFlow_height = height * 2 / 3;
        Constants.CoverFlow_Width = width * 2 / 3;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                IntentUtils.startAppShareText(this, "这个应用不错哦");
                break;
            case R.id.action_about:
                IntentUtils.startAboutActivity(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
