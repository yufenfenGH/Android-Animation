### 属性动画
&emsp;&emsp;比较常用的几个动画类是：ValueAnimator、ObjectAnimator和AnimatorSet，其中ObjectAnimator继承自ValueAnimator，AnimatorSet是动画集，可以定义一组动画。使用起来也是及其简单的，下面举个小栗子。

栗子效果如下：

![](https://upload-images.jianshu.io/upload_images/12972541-bd9aa4d4438a4c2f.gif?imageMogr2/auto-orient/strip)


&emsp;&emsp;运行后，就会出现上图的效果，你可以用动画属性做出更为复杂的动画效果。说到属性动画，就不得不提到插值器（TimeInterpolator）和估值算法（TypeEvaluator），下面介绍。

###TimeInterpolator和TypeEvaluator
&emsp;&emsp;TimeInterpolator中文翻译为时间插值器，它的作用是根据时间流逝的百分比来计算出当前属性值改变的百分比，系统预置的有

|  类型  |  描述  |
|:------:|:------:|
|  LinearInterpolator  |线性插值器：匀速动画  |
|AccelerateDecelerateInterpolator  |  加速减速插值器：动画两头慢中间快  |
|  DecelerateInterpolator  |  减速插值器：动画越来越慢  |

&emsp;&emsp;TypeEvaluator的中文翻译为类型估值算法，它的作用是根据当前属性改变的百分比来计算改变后的属性值，系统预置的有IntEvaluator（针对整型属性）、FloatEvaluator（针对浮点型属性）和ArgbEvaluator（针对Color属性）。可能这么说还有点晦涩，没关系，下面给出一个实例就很好理解了。


&emsp;&emsp;看上述动画，很显然上述动画是一个匀速动画，其采用了线性插值器和整型估值算法，在40ms内，View的x属性实现从0到40的变换，由于动画的默认刷新率为10ms/帧，所以该动画将分5帧进行，我们来考虑第三帧（x=20 t=20ms），当时间t=20ms的时候，时间流逝的百分比是0.5 (20/40=0.5)，意味这现在时间过了一半，那x应该改变多少呢，这个就由插值器和估值算法来确定。拿线性插值器来说，当时间流逝一半的时候，x的变换也应该是一半，即x的改变是0.5，为什么呢？因为它是线性插值器，是实现匀速动画的，下面看它的源码：
```
public class LinearInterpolator implements Interpolator {
 
    public LinearInterpolator() {
    }
    
    public LinearInterpolator(Context context, AttributeSet attrs) {
    }
    
    public float getInterpolation(float input) {
        return input;
    }
}
```
&emsp;&emsp;很显然，线性插值器的返回值和输入值一样，因此插值器返回的值是0.5，这意味着x的改变是0.5，这个时候插值器的工作就完成了。

&emsp;&emsp;具体x变成了什么值，这个需要估值算法来确定，我们来看看整型估值算法的源码：
```
public class IntEvaluator implements TypeEvaluator<Integer> {
 
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int)(startInt + fraction * (endValue - startInt));
    }
}
```
&emsp;&emsp;上述算法很简单，evaluate的三个参数分别表示：估值小数、开始值和结束值，对应于我们的例子就分别是：0.5，0，40。根据上述算法，整型估值返回给我们的结果是20，这就是（x=20 t=20ms）的由来。

&emsp;&emsp;说明：属性动画要求该属性有set方法和get方法（可选）；插值器和估值算法除了系统提供的外，我们还可以自定义，实现方式也很简单，因为插值器和估值算法都是一个接口，且内部都只有一个方法，我们只要派生一个类实现接口就可以了，然后你就可以做出千奇百怪的动画效果。具体一点就是：自定义插值器需要实现Interpolator或者TimeInterpolator，自定义估值算法需要实现TypeEvaluator。还有就是如果你对其他类型（非int、float、color）做动画，你必须要自定义类型估值算法。

之前做的动画加上TimeInterpolator的效果如下：

![AccelerateDecelerateInterpolator效果](https://upload-images.jianshu.io/upload_images/12972541-9047e87ffc887ef1.gif?imageMogr2/auto-orient/strip)

![DecelerateInterpolator效果](https://upload-images.jianshu.io/upload_images/12972541-1d40791e7f283ab6.gif?imageMogr2/auto-orient/strip)

![LinearInterpolator效果](https://upload-images.jianshu.io/upload_images/12972541-a29ca9bf3a6f4e90.gif?imageMogr2/auto-orient/auto-orient/strip)

### 自定义TimeInterpolator
&emsp;&emsp;在实际开发中，系统预置的时间差值器肯定是不能满足我们的需求的，这就需要自定义了。这里自定义一个简单的时间插值器，使其具有这样的曲线特征：
基于中心点正负振幅逐渐趋于0的时间插值器，数学表达式：
pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor) + 1 

自定义曲线的特征：
![image](https://upload-images.jianshu.io/upload_images/3270074-bd7ec463ebc29b27.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/480/format/webp)


上面已经说过，自定义非常简单，只要实现接口就OK，实现如下：
```
    public class MyInterpolator implements Interpolator {

        private static final float DEFAULT_FACTOR = 0.3f;

        // 因子数值越小振动频率越高
        private float mFactor;

        public MyInterpolator() {
            this(DEFAULT_FACTOR);
        }

        public MyInterpolator(float factor) {
            mFactor = factor;
        }

        @Override
        public float getInterpolation(float input) {
            // pow(2, -10 * input) * sin((input - factor / 4) * (2 * PI) / factor) + 1
            return (float) (Math.pow(2, -10 * input) * Math.sin((input - mFactor / 4.0d) * (2.0d * Math.PI) / mFactor) + 1);

        }
    }
```

效果：
![](https://upload-images.jianshu.io/upload_images/12972541-0b4334bd59c55992.gif?imageMogr2/auto-orient/strip)

OK，属性动画就学习到这里，下一节进行属性动画的深入分析
