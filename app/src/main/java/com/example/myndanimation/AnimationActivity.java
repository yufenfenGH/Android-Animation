package com.example.myndanimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

public class AnimationActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "AnimationActivity";

    private Button mMenuButton;
    private Button mItemButton1;
    private Button mItemButton2;
    private Button mItemButton3;
    private Button mItemButton4;
    private Button mItemButton5;
    private Button mItemButton6;
    private Button mItemButton7;
    private Button mItemButton8;
    private Button mItemButton9;
    private Button mItemButton10;
    private Button mItemButton11;
    private Button mItemButton12;

    private boolean mIsMenuOpen = false;
    private int menuNum = 12;
    private int range = 360; // 显示角度范围： 0 - 360
    private float centerX = 0;
    private float centerY = 0;

    private float radius = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_layout);

        initView();
    }


    private void initView() {
        mMenuButton = (Button) findViewById(R.id.menu);
        mMenuButton.setOnClickListener(this);

        mItemButton1 = (Button) findViewById(R.id.item1);
        mItemButton1.setOnClickListener(this);

        mItemButton2 = (Button) findViewById(R.id.item2);
        mItemButton2.setOnClickListener(this);

        mItemButton3 = (Button) findViewById(R.id.item3);
        mItemButton3.setOnClickListener(this);

        mItemButton4 = (Button) findViewById(R.id.item4);
        mItemButton4.setOnClickListener(this);

        mItemButton5 = (Button) findViewById(R.id.item5);
        mItemButton5.setOnClickListener(this);

        mItemButton6 = (Button) findViewById(R.id.item6);
        mItemButton6.setOnClickListener(this);

        mItemButton7 = (Button) findViewById(R.id.item7);
        mItemButton7.setOnClickListener(this);

        mItemButton8 = (Button) findViewById(R.id.item8);
        mItemButton8.setOnClickListener(this);

        mItemButton9 = (Button) findViewById(R.id.item9);
        mItemButton9.setOnClickListener(this);

        mItemButton10 = (Button) findViewById(R.id.item10);
        mItemButton10.setOnClickListener(this);

        mItemButton11 = (Button) findViewById(R.id.item11);
        mItemButton11.setOnClickListener(this);

        mItemButton12 = (Button) findViewById(R.id.item12);
        mItemButton12.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mMenuButton) {
            if (!mIsMenuOpen) {
                mIsMenuOpen = true;
                doAnimateOpen(mItemButton1, 0, menuNum);
                doAnimateOpen(mItemButton2, 1, menuNum);
                doAnimateOpen(mItemButton3, 2, menuNum);
                doAnimateOpen(mItemButton4, 3, menuNum);
                doAnimateOpen(mItemButton5, 4, menuNum);
                doAnimateOpen(mItemButton6, 5, menuNum);
                doAnimateOpen(mItemButton7, 6, menuNum);
                doAnimateOpen(mItemButton8, 7, menuNum);
                doAnimateOpen(mItemButton9, 8, menuNum);
                doAnimateOpen(mItemButton10, 9, menuNum);
                doAnimateOpen(mItemButton11, 10, menuNum);
                doAnimateOpen(mItemButton12, 11, menuNum);
            } else {
                mIsMenuOpen = false;
                doAnimateClose(mItemButton1, 0, menuNum);
                doAnimateClose(mItemButton2, 1, menuNum);
                doAnimateClose(mItemButton3, 2, menuNum);
                doAnimateClose(mItemButton4, 3, menuNum);
                doAnimateClose(mItemButton5, 4, menuNum);
                doAnimateClose(mItemButton6, 5, menuNum);
                doAnimateClose(mItemButton7, 6, menuNum);
                doAnimateClose(mItemButton8, 7, menuNum);
                doAnimateClose(mItemButton9, 8, menuNum);
                doAnimateClose(mItemButton10, 9, menuNum);
                doAnimateClose(mItemButton11, 10, menuNum);
                doAnimateClose(mItemButton12, 11, menuNum);
            }
        } else {
            Toast.makeText(this, "你点击了" + v.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 打开菜单的动画
     * @param view 执行动画的view
     * @param index view在动画序列中的顺序
     * @param total 动画序列的个数
     */
    private void doAnimateOpen(View view, int index, int total) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        // 圆弧形上坐标公式：
//            x = cos(π * (度数/180))
//            y = sin(π * (度数/180))
        float angle = index * (range/total);
        double degree = Math.PI * (angle/180);
        int translationX = (int) (radius * Math.cos(degree));
        int translationY = (int) (radius * Math.sin(degree));
        Log.d(TAG, String.format("degree=%f, translationX=%d, translationY=%d",
                degree, translationX, translationY));
        AnimatorSet set = new AnimatorSet();
        //包含平移、缩放、透明度、旋转和倒转显示动画
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1),
                ObjectAnimator.ofFloat(view, "rotation", 0, 360)
                //need sdk 21
//                ObjectAnimator.ofArgb((Button)view, "textColor", 0x000, 0xFFFF00),
//                ObjectAnimator.ofArgb(view, "backgroundColor", 0x000, 0xFF0000)

        );

        // add TimeInterpolator
//        set.setInterpolator(new LinearInterpolator());
//        set.setInterpolator(new AccelerateDecelerateInterpolator());
//        set.setInterpolator(new DecelerateInterpolator());
        set.setInterpolator(new MyInterpolator(0.2f));

        //动画周期为500ms
        set.setDuration(1 * 2000).start();
    }

    /**
     * 关闭菜单的动画
     * @param view 执行动画的view
     * @param index view在动画序列中的顺序
     * @param total 动画序列的个数
     */
    private void doAnimateClose(final View view, int index, int total) {
//        if (view.getVisibility() != View.VISIBLE) {
//            view.setVisibility(View.VISIBLE);
//        }
        // 圆弧形上坐标公式：
//            x = cos(π * (度数/180))
//            y = sin(π * (度数/180))
        float angle = index * (range/total);
        double degree = Math.PI * (angle/180);
        int translationX = (int) (radius * Math.cos(degree));
        int translationY = (int) (radius * Math.sin(degree));
        Log.d(TAG, String.format("degree=%f, translationX=%d, translationY=%d",
                degree, translationX, translationY));
        AnimatorSet set = new AnimatorSet();
        //包含平移、缩放、透明度、旋转和倒转显示动画
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX, 0),
                ObjectAnimator.ofFloat(view, "translationY", translationY, 0),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f),
                ObjectAnimator.ofFloat(view, "rotation", 360, 0)
                //need sdk 21
//                ObjectAnimator.ofArgb((Button)view, "textColor", 0xFFFF00, 0x000),
//                ObjectAnimator.ofArgb(view, "backgroundColor", 0xFF0000, 0x000)
        );
        //为动画加上事件监听，当动画结束的时候，我们把当前view隐藏
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {

//                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }
        });

        // add TimeInterpolator
//        set.setInterpolator(new LinearInterpolator());
//        set.setInterpolator(new AccelerateDecelerateInterpolator());
//        set.setInterpolator(new DecelerateInterpolator());
        set.setInterpolator(new MyInterpolator(0.2f));

        set.setDuration(1 * 2000).start();
    }

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
}