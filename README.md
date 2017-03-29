在 Support Library 25.3.0 里面新增了一个叫 SpringAnimation 的动画，也就是弹簧动画。这里用它来做一个滑动控件下拉回弹的效果。

![](/images/SpringScrollView.gif)

# SpringAnimation

开始之前，别忘了在 app 的 build.gradle 加上：

>compile 'com.android.support:appcompat-v7:25.3.0'  
compile 'com.android.support:design:25.3.0'  
compile 'com.android.support:support-dynamic-animation:25.3.0'

然后我们看看 SpringAnimation 的基本用法，首先是它的构造方法：

```java
public SpringAnimation(View v, ViewProperty property, float finalPosition) {
    super(v, property);
    mSpring = new SpringForce(finalPosition);
    setSpringThreshold();
}
```

看命名可以大概猜到参数的意义了：

* v - 要执行动画的控件
* property - 动画的性质，可以选择平移、缩放、旋转等
* finalPosition - 动画结束时，控件所在位置的坐标偏移量

这里实现的滑动控件是上下滑动的，所以我们这样来获取 SpringAnimation ：

```java
springAnim = new SpringAnimation(this, SpringAnimation.TRANSLATION_X, 0);
```

SpringAnimation 里面有两个比较重要的属性，分别是：

* Stiffness - 刚度，值越大回弹的速度越快，类似于劲度系数，默认值是 1500f  
* DampingRatio - 阻尼，值越小，回弹后，动画来回的次数越多，就是更有「DUANG」的感觉，默认值是 0.5f

通过

```java
springAnim.getSpring().setStiffness(float stiffness) 
```
和
```java
springAnim.getSpring().setDampingRatio(float dampingRatio) 
```
来设置上面两个属性。

再调用 springAnim.start() 就可以开始动画啦。

# SpringScrollView

我们自定义一个 SpringScrollView 继承 NestedScrollView，重写 onTouchEvent 方法让它有回弹的效果：

```java
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
            } 
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            if (getTranslationY() != 0) {
                springAnim.start();
            }
            startDragY = 0;
            break;
    }
    return super.onTouchEvent(e);
}
```

简单解释一下哈。

当 ScrollView 在顶部时，记录下手指所在的 y 轴位置。在顶部并且是往下滑动的时候，给 ScrollView 设置一个纵向的偏移。之所以除以 3，是为了让控件有种要用力才能拖动的感觉。

在顶部的时候如果是往上滑动，则把动画效果取消，把控件位置复原，否则可能出现控件一直偏移的情况。

最后当手指抬起时，执行弹簧动画就好了。

为什么这里用 getRawY() 获取坐标，而不是用 getY() 来获取。因为 getY() 是相对于控件的坐标，当设置了 TranslationY 之后会改变它的值，也就是在滑动的时候 getY() 的值是不连续的，会出现卡顿的现象。而 getRawY() 是相对于屏幕的位置，管你控件怎么动，屏幕都是固定的。 

下拉回弹的效果就已经完成了。对了，我们顺便把底部上拉的回弹也做一下呗。由于ScrollView只有一个子布局，所以可以通过

```java
getScrollY() + getHeight()) >= getChildAt(0).getMeasuredHeight()
```

判断是否滑动到了底部。

同样的思路也可以用在别的滑动控件里面。

妥妥的。