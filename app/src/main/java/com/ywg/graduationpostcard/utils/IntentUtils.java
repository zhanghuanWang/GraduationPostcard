package com.ywg.graduationpostcard.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ywg.graduationpostcard.R;
import com.ywg.graduationpostcard.activity.AboutActivity;
import com.ywg.graduationpostcard.activity.WebViewActivity;

/**
 * Created by maning on 16/3/3.
 * <p/>
 * 页面跳转
 */
public class IntentUtils {

    public static final String ImagePositionForImageShow = "PositionForImageShow";
    public static final String ImageArrayList = "BigImageArrayList";
    public static final String WebTitleFlag = "WebTitleFlag";
    public static final String WebTitle = "WebTitle";
    public static final String WebUrl = "WebUrl";


    public static void startToWebActivity(Context context, String title, String url) {
        Intent intent = new Intent(context.getApplicationContext(), WebViewActivity.class);
        intent.putExtra(WebTitle, title);
        intent.putExtra(WebUrl, url);
        context.startActivity(intent);
    }

    /**
     * 打开WebView界面
     *
     * @param context
     * @param titleFlag
     * @param title
     * @param url
     */
    public static void startToWebActivity(Context context, String titleFlag, String title, String url) {
        Intent intent = new Intent(context.getApplicationContext(), WebViewActivity.class);
        intent.putExtra(WebTitleFlag, titleFlag);
        intent.putExtra(WebTitle, title);
        intent.putExtra(WebUrl, url);
        context.startActivity(intent);
    }
    /**
     * 打开关于界面
     *
     * @param context
     */
    public static void startAboutActivity(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), AboutActivity.class);
        context.startActivity(intent);
    }

    /**
     * 打开设置界面
     *
     * @param context
     */
   /* public static void startSettingActivity(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), SettingActivity.class);
        context.startActivity(intent);
    }*/

    /**
     * 分享带文本
     *
     * @param context
     * @param shareText
     */
    public static void startAppShareText(Context context, String shareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain"); // 纯文本
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.action_share));
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.action_share)));
    }

    /**
     * 分享带文本和图片
     *
     * @param context
     * @param shareText
     * @param uri
     */
    public static void startAppShareImage(Context context, String shareText, Uri uri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        //shareIntent.putExtra(Intent.EXTRA_TITLE, shareTitle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.action_share)));
    }

    /**
     * 用浏览器打开链接
     * @param context
     * @param url
     */
    public static void openLinkInBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            ToastUtil.showShort(context, "此系统没有浏览器");
        }
    }

    /**
     * 打开应用商店
     * @param context
     */
    public static void openInMarket(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            ToastUtil.showShort(context,"此系统没有应用商店");
        }
    }

}