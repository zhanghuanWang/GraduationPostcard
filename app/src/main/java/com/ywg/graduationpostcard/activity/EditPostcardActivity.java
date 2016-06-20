package com.ywg.graduationpostcard.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ywg.graduationpostcard.R;
import com.ywg.graduationpostcard.utils.BitmapUtil;
import com.ywg.graduationpostcard.utils.IntentUtils;
import com.ywg.graduationpostcard.utils.KeyBoardUtils;
import com.ywg.graduationpostcard.utils.SPUtils;

import java.io.File;
import java.util.Calendar;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;

/**
 * 编辑明信片的Activity
 */
public class EditPostcardActivity extends BaseToolbarActivity implements View.OnClickListener {

    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),150 X 150的正方形。
    private static int output_X = 500;
    private static int output_Y = 500;


    private ScrollView mScrollView;

    private int styleCode = 0;

    private LinearLayout mRootLayout;

    private ImageView mCoverIv;

    private TextView mContentTv1;

    private TextView mContentTv2;

    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_postcard;
    }

    @Override
    protected void initValues() {
        mContext = EditPostcardActivity.this;

        Intent intent = getIntent();
        if (intent != null) {
            styleCode = intent.getIntExtra("styleCode", 0);
        }
    }

    @Override
    protected void initViews() {
        initToolBar("编辑", true);

        mScrollView = (ScrollView) findViewById(R.id.sv_postcard);

        switch (styleCode) {
            case 0:
                LayoutInflater.from(this).inflate(R.layout.pattern_1, mScrollView, true);
                break;
            case 1:
                LayoutInflater.from(this).inflate(R.layout.pattern_2, mScrollView, true);
                break;
            case 2:
                LayoutInflater.from(this).inflate(R.layout.pattern_3, mScrollView, true);
                break;
            case 3:
                LayoutInflater.from(this).inflate(R.layout.pattern_4, mScrollView, true);
                break;
            case 4:
                LayoutInflater.from(this).inflate(R.layout.pattern_5, mScrollView, true);
                break;
            case 5:
                LayoutInflater.from(this).inflate(R.layout.pattern_6, mScrollView, true);
                break;
            case 6:
                LayoutInflater.from(this).inflate(R.layout.pattern_7, mScrollView, true);
                break;
            case 7:
                LayoutInflater.from(this).inflate(R.layout.pattern_8, mScrollView, true);
                break;
        }

        mRootLayout = (LinearLayout) findViewById(R.id.ll);
        mRootLayout.setDrawingCacheEnabled(true);
        mCoverIv = (ImageView) findViewById(R.id.iv1);
        mCoverIv.setDrawingCacheEnabled(true);
        mContentTv1 = (TextView) findViewById(R.id.tv1);
        mContentTv2 = (TextView) findViewById(R.id.tv_date);

        mCoverIv.setOnClickListener(this);
        mContentTv1.setOnClickListener(this);
        mContentTv2.setOnClickListener(this);

        boolean isFirstGuide = (boolean) SPUtils.get(this, "isFirstGuide", true);
        if (isFirstGuide) {
            new MaterialIntroView.Builder(this)
                    .enableDotAnimation(true)
                    .enableIcon(false)
                    .setFocusGravity(FocusGravity.CENTER)
                    .setFocusType(Focus.MINIMUM)
                    .setDelayMillis(500)
                    .enableFadeAnimation(true)
                    .performClick(true)
                    .setInfoText("点击指定内容进行修改")
                    .setTarget(mRootLayout)
                    .setUsageId("8")
                    .show();
        }


    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_edit;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                Bitmap bmp = mRootLayout.getDrawingCache();
                new SaveImageTask().execute(bmp);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class SaveImageTask extends AsyncTask<Bitmap, Void, Uri> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("正在保存中..");
        }

        @Override
        protected Uri doInBackground(Bitmap... params) {
            return BitmapUtil.saveImageToGallery(mContext, params[0]);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            dissmissProgressDialog();
            if (uri != null) {
                IntentUtils.startAppShareImage(mContext, "这是我制作的毕业明信片", uri);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv1:
                showSelectImageDialog();
                break;

            case R.id.tv1:
                final EditText et = new EditText(this);
                et.setText(mContentTv1.getText().toString());
                et.setSelection(mContentTv1.getText().toString().length());
                et.setFocusable(true);
                et.setFocusableInTouchMode(true);
                et.requestFocus();
                new AlertDialog.Builder(this)
                        .setTitle(R.string.edit)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mContentTv1.setText(et.getText().toString());
                            }
                        })
                        .setCancelable(true)
                        .create().show();
                KeyBoardUtils.openKeybord(et, this);
                break;

            case R.id.tv_date:
                if (mContentTv2.getText().toString().contains("/")) {
                    Calendar c = Calendar.getInstance();
                    new DatePickerDialog(
                            this,
                            new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                                    mContentTv2.setText(year + " / " + (month + 1) + " / " + dayOfMonth);
                                }
                            },
                            c.get(Calendar.YEAR), // 传入年份
                            c.get(Calendar.MONTH), // 传入月份
                            c.get(Calendar.DAY_OF_MONTH) // 传入天数
                    ).show();
                } else {
                    final EditText et1 = new EditText(this);
                    et1.setText(mContentTv2.getText().toString());
                    et1.setSelection(mContentTv2.getText().toString().length());
                    et1.setFocusable(true);
                    et1.setFocusableInTouchMode(true);
                    et1.requestFocus();
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.edit)
                            .setView(et1)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mContentTv2.setText(et1.getText().toString());
                                }
                            })
                            .setCancelable(true)
                            .create().show();
                    KeyBoardUtils.openKeybord(et1, this);
                }
                break;
        }
    }

    private void showSelectImageDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{"本地图库", "本地相机"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        choseHeadImageFromGallery();
                        break;
                    case 1:
                        choseHeadImageFromCameraCapture();
                        break;
                    default:
                        break;
                }
            }
        })
                .setCancelable(true);
        builder.create().show();

    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
            startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
        } else {
            Toast.makeText(getApplication(), "请插入sd卡", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    cropRawPhoto(getImageUri());
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }
                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1.5);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            mCoverIv.setImageBitmap(photo);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取路径
     */
    private Uri getImageUri() {
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                IMAGE_FILE_NAME));
    }
}
