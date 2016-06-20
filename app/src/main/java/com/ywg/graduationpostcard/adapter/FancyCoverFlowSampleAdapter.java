package com.ywg.graduationpostcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ywg.graduationpostcard.R;
import com.ywg.graduationpostcard.constant.Constants;
import com.ywg.graduationpostcard.view.FancyCoverFlow.FancyCoverFlow;
import com.ywg.graduationpostcard.view.FancyCoverFlow.FancyCoverFlowAdapter;

public class FancyCoverFlowSampleAdapter extends FancyCoverFlowAdapter {

    int a;
    // =============================================================================
    // Private members
    // =============================================================================

    private int[] images = {R.drawable.s01, R.drawable.s02, R.drawable.s03, R.drawable.s04,
            R.drawable.s05, R.drawable.s06, R.drawable.s07, R.drawable.s08};

    // =============================================================================
    // Supertype overrides
    // =============================================================================

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Integer getItem(int i) {
        return images[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
        CustomViewGroup customViewGroup = null;

        if (reuseableView != null) {
            customViewGroup = (CustomViewGroup) reuseableView;
        } else {
            customViewGroup = new CustomViewGroup(viewGroup.getContext());
            customViewGroup.setLayoutParams(new FancyCoverFlow.LayoutParams(Constants.CoverFlow_Width, Constants.CoverFlow_height));//图片高度
        }

        customViewGroup.getImageView().setImageResource(this.getItem(i));
//        customViewGroup.getTextView().setText(String.format("Item %d", i));
//        int pos=customViewGroup.getPos().String.format("Item %d", i);

        return customViewGroup;
    }

    private class CustomViewGroup extends LinearLayout {

        private ImageView imageView;
//        Button bt;

        public CustomViewGroup(final Context context) {
            super(context);
            this.imageView = new ImageView(context);

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.imageView.setLayoutParams(layoutParams);
            this.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            this.imageView.setAdjustViewBounds(true);


            this.addView(this.imageView);

        }

        public ImageView getImageView() {
            return imageView;
        }


    }
}