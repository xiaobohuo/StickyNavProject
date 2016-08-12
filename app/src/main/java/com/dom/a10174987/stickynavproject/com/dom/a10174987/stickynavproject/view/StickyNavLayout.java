package com.dom.a10174987.stickynavproject.com.dom.a10174987.stickynavproject.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.ScrollView;

import com.dom.a10174987.stickynavproject.R;

/**
 * Created by 10174987 on 2016/8/10.
 */

public class StickyNavLayout extends LinearLayout {

    private View mTopView;
    private View mIndicatorView;
    private ViewGroup mInnerScrollView;
    private ViewPager mViewPager;

    private OverScroller scroller;
    private VelocityTracker velocityTracker;

    private int mTouchSlop;
    private int minFlingVelocity, maxFlingVelocity;

    private int mLastY;
    private int mTopViewHeight;
    private boolean isOuterInControl = true;
    private boolean isDragging;
    private boolean isTopViewHidden = false;

    public StickyNavLayout(Context context) {
        this(context, null);
    }

    public StickyNavLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyNavLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

        scroller = new OverScroller(getContext());
//        velocityTracker = VelocityTracker.obtain();
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        minFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        maxFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = findViewById(R.id.id_top_view);
        mIndicatorView = findViewById(R.id.id_indicator_view);
        View v = findViewById(R.id.id_viewpager);
        if (v instanceof ViewPager) {
            mViewPager = (ViewPager) v;
        } else {
            throw new RuntimeException("no view pager found");
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight() - mIndicatorView.getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTopView.getMeasuredHeight();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int y = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = y - mLastY;
                getCurrentScrollView();
                if (mInnerScrollView instanceof ScrollView) {
                    if (!isOuterInControl && mInnerScrollView.getScrollY() == 0 && dy > 0 && isTopViewHidden) {
                        isOuterInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        dispatchTouchEvent(ev);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                } else if (mInnerScrollView instanceof ListView) {
                    ListView listView = (ListView) mInnerScrollView;
                    View child = listView.getChildAt(listView.getFirstVisiblePosition());
                    if (!isOuterInControl && child != null && child.getTop() == 0 && dy > 0 && isTopViewHidden) {
                        isOuterInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        dispatchTouchEvent(ev);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                } else if (mInnerScrollView instanceof RecyclerView) {
                    RecyclerView rv = (RecyclerView) mInnerScrollView;
                    if (!isOuterInControl && ViewCompat.canScrollVertically(rv, -1) && dy > 0 && isTopViewHidden) {
                        isOuterInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        dispatchTouchEvent(ev);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    private void getCurrentScrollView() {
        PagerAdapter adapter = mViewPager.getAdapter();
        int currentIndex = mViewPager.getCurrentItem();
        if (adapter instanceof FragmentPagerAdapter) {
            FragmentPagerAdapter fadapter = (FragmentPagerAdapter) adapter;
            Fragment fragment = (Fragment) fadapter.instantiateItem(mViewPager, currentIndex);
            mInnerScrollView = (ViewGroup) fragment.getView().findViewById(R.id.id_inner_scrollview);
        } else if (adapter instanceof FragmentStatePagerAdapter) {
            FragmentStatePagerAdapter fadapter = (FragmentStatePagerAdapter) adapter;
            Fragment fragment = (Fragment) fadapter.instantiateItem(mViewPager, currentIndex);
            mInnerScrollView = (ViewGroup) fragment.getView().findViewById(R.id.id_inner_scrollview);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = y - mLastY;
                getCurrentScrollView();
                if (Math.abs(dy) > mTouchSlop) {
                    isDragging = true;
                    if (mInnerScrollView instanceof ScrollView) {
                        if (!isTopViewHidden || (mInnerScrollView.getScrollY() == 0 && dy > 0 && isTopViewHidden)) {
                            obtainVelocityTrackerIfNotExist();
                            velocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    } else if (mInnerScrollView instanceof ListView) {
                        ListView listView = (ListView) mInnerScrollView;
                        View child = listView.getChildAt(listView.getFirstVisiblePosition());
                        if (!isTopViewHidden || child != null && child.getTop() == 0 && dy > 0 && isTopViewHidden) {
                            obtainVelocityTrackerIfNotExist();
                            velocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    } else if (mInnerScrollView instanceof RecyclerView) {
                        RecyclerView rv = (RecyclerView) mInnerScrollView;
                        if (!isTopViewHidden || ViewCompat.canScrollVertically(rv, -1) && dy > 0 && isTopViewHidden) {
                            obtainVelocityTrackerIfNotExist();
                            velocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isDragging = false;
                recyleVelocityTracker();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void recyleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    private void obtainVelocityTrackerIfNotExist(){
        if (velocityTracker == null){
            velocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = y - mLastY;
                getCurrentScrollView();
                if (Math.abs(dy) > mTouchSlop) {
                    isDragging = true;
                }
                if (!isTopViewHidden && isDragging) {
                    scrollBy(0, -dy);
                }
                if (getScrollY() == mTopViewHeight && dy < 0) {
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    dispatchTouchEvent(event);
                    MotionEvent ev2 = MotionEvent.obtain(event);
                    ev2.setAction(MotionEvent.ACTION_DOWN);
                    return onTouchEvent(ev2);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                recyleVelocityTracker();
                break;
            case MotionEvent.ACTION_UP:
                isDragging = false;
                obtainVelocityTrackerIfNotExist();
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000, maxFlingVelocity);
                int vy = (int) velocityTracker.getYVelocity();
                if (Math.abs(vy) > minFlingVelocity) {
                    scroller.fling(0, getScrollY(), 0, -vy, 0, 0, 0, mTopViewHeight);
                    invalidate();
                }
                recyleVelocityTracker();
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        isTopViewHidden = getScrollY() == mTopViewHeight;
    }
}
