package com.southernbox.springscrollview

import android.content.Context
import android.support.animation.SpringAnimation
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by SouthernBox on 2017/6/19 0019.
 * 下拉后有回弹动画的滚动控件
 */

class SpringScrollView @JvmOverloads constructor(context: Context,
                                                 attr: AttributeSet? = null,
                                                 defStyle: Int = 0)
    : NestedScrollView(context, attr, defStyle) {

    private var startDragY: Float = 0f
    private val springAnim: SpringAnimation = SpringAnimation(this, SpringAnimation.TRANSLATION_Y, 0.0f)

    init {
        //刚度 默认1200 值越大回弹的速度越快
        springAnim.spring.stiffness = 800.0f
        //阻尼 默认0.5 值越小，回弹之后来回的次数越多
        springAnim.spring.dampingRatio = 0.50f
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_MOVE ->
                if (scrollY <= 0) {
                    //顶部下拉
                    if (startDragY == 0f) {
                        startDragY = ev.rawY
                    }
                    if (ev.rawY - startDragY >= 0) {
                        translationY = (ev.rawY - startDragY) / 3
                        return true
                    } else {
                        startDragY = 0f
                        springAnim.cancel()
                        translationY = 0f
                    }
                } else if (scrollY + height >= getChildAt(0).measuredHeight) {
                    //底部上拉
                    if (startDragY == 0f) {
                        startDragY = ev.rawY
                    }
                    if (ev.rawY - startDragY <= 0) {
                        translationY = (ev.rawY - startDragY) / 3
                        return true
                    } else {
                        startDragY = 0f
                        springAnim.cancel()
                        translationY = 0f
                    }
                }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (translationY != 0f) {
                    springAnim.start()
                }
                startDragY = 0f
            }
        }
        return super.onTouchEvent(ev)
    }
}