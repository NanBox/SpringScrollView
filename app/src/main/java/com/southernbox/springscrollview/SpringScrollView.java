package com.southernbox.springscrollview;

import android.content.Context;
import android.support.animation.SpringAnimation;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by SouthernBox on 2017/3/23 0023.
 * 下拉后有回弹动画的滚动控件
 */

public class SpringScrollView extends NestedScrollView {

    private float startDragY;
    private SpringAnimation springAnim;

    public SpringScrollView(Context context) {
        this(context, null);
    }

    public SpringScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpringScrollView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        springAnim = new SpringAnimation(this, SpringAnimation.TRANSLATION_Y, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getScrollY() <= 0) {
                    //顶部下拉
                    if (startDragY == 0) {
                        startDragY = e.getRawY();
                    }
                    if (e.getRawY() - startDragY >= 0) {
                        setTranslationY((e.getRawY() - startDragY) / 3);
                        return true;
                    } else {
                        startDragY = 0;
                        springAnim.cancel();
                        setTranslationY(0);
                    }
                } else if ((getScrollY() + getHeight()) >= getChildAt(0).getMeasuredHeight()) {
                    //顶部上拉
                    if (startDragY == 0) {
                        startDragY = e.getRawY();
                    }
                    if (e.getRawY() - startDragY <= 0) {
                        setTranslationY((e.getRawY() - startDragY) / 3);
                        return true;
                    } else {
                        startDragY = 0;
                        springAnim.cancel();
                        setTranslationY(0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getTranslationY() != 0) {
                    //刚度 默认1200 值越大回弹的速度越快
                    springAnim.getSpring().setStiffness(800.0f);
                    //阻尼 默认0.5 值越小，回弹之后来回的次数越多
                    springAnim.getSpring().setDampingRatio(0.50f);
                    springAnim.start();
                }
                startDragY = 0;
                break;
        }
        return super.onTouchEvent(e);
    }

}
