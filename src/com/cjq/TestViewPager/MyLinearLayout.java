package com.cjq.TestViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by CJQ on 2015/5/25.
 */
public class MyLinearLayout extends LinearLayout {
    private int mWidth;

    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mWidth = metrics.widthPixels;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            int cellWidth = mWidth/3;

            int nowRow = 0;
            int nowCol =0;

            for(int i=0;i<this.getChildCount();i++){
                View view = this.getChildAt(i);
                int left = nowRow*cellWidth;
                int top = nowCol*cellWidth;
                int right = left+cellWidth;
                int bottom = top+cellWidth;

                view.layout(left,top,right,bottom);

                nowRow++;
                if(nowRow>=3){
                    nowCol++;
                    nowRow=0;
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getChildCount()%3==0?getChildCount()*mWidth/9:(getChildCount()/3+1)*mWidth/3;
        setMeasuredDimension(getMeasuredWidth(),height);
    }
}
