# 前言
&emsp;&emsp;好久之前就想做一个，关于Android动画相关学习分析课程，但是一直没有时间，直到最近看了一篇 [任玉刚](https://blog.csdn.net/singwhatiwanna) 的动画分享文章，感觉写得很好，于是结合他的分享，写几篇关于Android动画的分享文章。

### Android动画系列：
+ [android动画一：动画简介](https://www.jianshu.com/p/605547126358)
+ [Android动画二：属性动画](https://www.jianshu.com/p/bc74c01ef3f0)
+ [Android动画三：属性动画深入分析](https://www.jianshu.com/p/81dfa79f3c43)
+ [Android动画四：属性动画的工作原理](https://www.jianshu.com/p/ce52f7808c0d)

# android中动画分为3种：

+ **Tween Animation**：通过对场景里的对象不断做图像变换(平移、缩放、旋转)产生动画效果，即是一种渐变动画；
+ **Frame Animation**：顺序播放事先做好的图像，是一种画面转换动画。
+ **Property Animation**：属性动画，通过动态地改变对象的属性从而达到动画效果，属性动画为API 11新特性。

&emsp;&emsp;下面只介绍前两种动画的使用方法，属性动画将在后续文章中介绍

##一 Tween Animation

&emsp;&emsp;Tween Animation有四种形式：
|  形式|  说明 |
| :-------: |:-------------:|
|  alpha  |            渐变透明度动画效果  |
|  scale      |        渐变尺寸伸缩动画效果  |
| translate   |      画面位置移动动画效果  |
|  rotate     |         画面旋转动画效果  |


&emsp;&emsp;这四种动画实现方式都是通过Animation类和AnimationUtils配合实现。

&emsp;&emsp;可以通过xml实现：动画的XML文件在工程中res/anim目录。

&emsp;&emsp;例如：rotate.xml
```
<?xml version="1.0" encoding="utf-8"?>
 
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:fillAfter = "false"
    android:zAdjustment="bottom"
    >
    <rotate
        android:fromDegrees="0"
        android:toDegrees="360"
        android:pivotX="50%"
        android:pivotY="50%"
        android:duration="4000"
        />
</set>
```
&emsp;&emsp;使用动画
```
Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
//监听动画的状态（开始，结束）
anim.setAnimationListener(new EffectAnimationListener());
textWidget = (TextView)findViewById(R.id.text_widget);
textWidget.setText("画面旋转动画效果");
textWidget.startAnimation(anim);
```
## 二 Frame Animation

&emsp;&emsp;Frame Animation是顺序播放事先做好的图像，跟电影类似。不同于animation package，Android SDK提供了另外一个类AnimationDrawable来定义使用Frame Animation。

&emsp;&emsp;利用xml文件实现：res/drawable-hdpi/frame.xml:
```
<?xml version="1.0" encoding="utf-8"?>
 
<animation-list
  xmlns:android="http://schemas.android.com/apk/res/android"
  android:oneshot="true"
  >
       <item android:drawable="@drawable/p1" android:duration="1000"></item>
       <item android:drawable="@drawable/p2" android:duration="1000"></item>
       <item android:drawable="@drawable/p3" android:duration="1000"></item>
       <item android:drawable="@drawable/p4" android:duration="1000"></item>
       <item android:drawable="@drawable/p5" android:duration="1000"></item>
       <item android:drawable="@drawable/p6" android:duration="1000"></item>
</animation-list>
```
&emsp;&emsp;使用动画使用动画
```
AnimationDrawable anim = (AnimationDrawable)getResources().
getDrawable(R.drawable.frame);
textWidget = (TextView)findViewById(R.id.text_widget);
textWidget.setText("背景渐变动画效果");
textWidget.setBackgroundDrawable(anim);
anim.start();
```
&emsp;&emsp;使用动画这里有点不同的是，利用AnimationDrawable实现动画时，本身并没有提供接口来监听动画的状态（开始，结束），需要自己处理。

原文：https://blog.csdn.net/singwhatiwanna/article/details/9270275 
