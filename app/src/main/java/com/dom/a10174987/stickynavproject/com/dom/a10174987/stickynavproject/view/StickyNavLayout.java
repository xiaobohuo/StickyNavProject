package com.dom.a10174987.stickynavproject.com.dom.a10174987.stickynavproject.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by 10174987 on 2016/8/10.
 */

public class StickyNavLayout extends LinearLayout {
    public StickyNavLayout(Context context) {
        this(context,null);
    }

    public StickyNavLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StickyNavLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
