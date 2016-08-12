package com.dom.a10174987.stickynavproject.com.dom.a10174987.stickynavproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 10174987 on 2016/8/12.
 */

public class SimpleIndicatorView extends LinearLayout {

    private static final int INDICATOR_COLOR_DEFAULT = 0xFF000000;

    private Paint mPaint;

    private int mTabWidth;

    private String[] mTitles;

    private int mTitlesSize;

    private float xTranslation;

    private int mIndicatorColor = INDICATOR_COLOR_DEFAULT;

    public SimpleIndicatorView(Context context) {
        this(context, null);
    }

    public SimpleIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        mPaint = new Paint();
        mPaint.setStrokeWidth(10.0f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTabWidth = w / mTitles.length;
    }

    public void setIndicatorColor(int mIndicatorColor) {
        this.mIndicatorColor = mIndicatorColor;
    }

    public void setIndicatorTitles(String[] titles) {
        removeAllViews();
        mTitlesSize = titles.length;
        mTitles = titles;
        generateViews();
    }

    private void generateViews() {
        setWeightSum(mTitlesSize);
        for (int i = 0; i < mTitlesSize; i++) {
            TextView tmp = new TextView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            tmp.setLayoutParams(lp);
            tmp.setText(mTitles[i]);
            tmp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tmp.setGravity(Gravity.CENTER);
            addView(tmp);
        }
    }

    public void scroll(int index, float offset) {
        xTranslation = (index + offset) * mTabWidth;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.translate(xTranslation, getHeight() - 2);
        canvas.drawLine(0, 0, mTabWidth, 0, mPaint);
        canvas.restore();
    }
}
