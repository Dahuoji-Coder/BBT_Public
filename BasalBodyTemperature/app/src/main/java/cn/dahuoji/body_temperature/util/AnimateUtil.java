package cn.dahuoji.body_temperature.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by 10732 on 2018/6/28.
 */

public class AnimateUtil {

    public static ValueAnimator alphaAnim(View view, int duration, int delay) {
        float fromAlpha = view.getAlpha();
        float toAlpha = 1f - fromAlpha;

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha);
        objectAnimator.setDuration(duration);
        objectAnimator.setStartDelay(delay);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        return objectAnimator;
    }

    public static ValueAnimator showTransBack(View view) {
        view.setVisibility(View.VISIBLE);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        objectAnimator.setDuration(200);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        return objectAnimator;
    }

    public static ValueAnimator hideTransBack(final View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        objectAnimator.setDuration(200);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();

        return objectAnimator;
    }

    public static ValueAnimator showTransBack(View view, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);

        if (view.getVisibility() == View.VISIBLE) return objectAnimator;
        view.setVisibility(View.VISIBLE);

        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        return objectAnimator;
    }

    public static ValueAnimator hideTransBack(final View view, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);

        if (view.getVisibility() == View.GONE) return objectAnimator;

        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();

        return objectAnimator;
    }

    public static ObjectAnimator alphaShowAnim(View view, long duration, long delay) {
        ObjectAnimator titleAnim = ObjectAnimator.ofFloat(view, "alpha", 0, 1.0f);
        titleAnim.setDuration(duration);
        titleAnim.setStartDelay(delay);
        titleAnim.start();
        return titleAnim;
    }

    public static ObjectAnimator alphaShowAnim(final View view, long duration, long delay, final float startY, final float moveLength) {
        ObjectAnimator titleAnim = ObjectAnimator.ofFloat(view, "alpha", 0, 1.0f);
        titleAnim.setDuration(duration);
        titleAnim.setStartDelay(delay);
        titleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                view.setY(startY - moveLength * percent);
            }
        });
        titleAnim.start();
        return titleAnim;
    }
}
