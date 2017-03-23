package com.southernbox.springscrollview;

import android.content.Context;
import android.support.animation.SpringAnimation;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * Created by SouthernBox on 2017/3/23 0023.
 * 下拉后有回弹动画的滚动控件
 */

public class SpringScrollView extends NestedScrollView {

    private float downY;
    private float springY;
    private VelocityTracker velocityTracker;
    private boolean canSpring;
    private SpringAnimation animY;

    public SpringScrollView(Context context) {
        this(context, null);
    }

    public SpringScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpringScrollView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        velocityTracker = VelocityTracker.obtain();
        animY = new SpringAnimation(this, SpringAnimation.TRANSLATION_Y, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (getScrollY() == 0 && e.getRawY() - downY > 0) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = e.getRawY();
                velocityTracker.addMovement(e);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (canSpring || (getScrollY() == 0 && e.getRawY() - downY > 0)) {
                    if (springY == 0) {
                        springY = e.getRawY();
                    }
                    setTranslationY((e.getRawY() - springY) / 3);
                    velocityTracker.addMovement(e);
                    canSpring = true;
                    return true;
                } else if (e.getRawY() - downY < 0) {
                    animY.cancel();
                    setTranslationY(0);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (canSpring) {
                    velocityTracker.computeCurrentVelocity(1000);
                    if (getTranslationY() != 0) {
                        //刚度 默认1200 值越大回弹的速度越快
                        animY.getSpring().setStiffness(800.0f);
                        //阻尼 默认0.5 值越小，回弹之后来回的次数越多
                        animY.getSpring().setDampingRatio(0.50f);
                        animY.setStartVelocity(velocityTracker.getYVelocity());
                        animY.start();
                    }
                    velocityTracker.clear();
                    canSpring = false;
                    springY = 0;
                    return true;
                }
                break;
        }
        return super.onTouchEvent(e);
    }

}
