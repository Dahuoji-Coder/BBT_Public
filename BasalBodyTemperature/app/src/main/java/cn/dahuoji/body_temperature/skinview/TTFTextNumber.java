package cn.dahuoji.body_temperature.skinview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import cn.dahuoji.body_temperature.util.MathUtil;

/**
 * Created by 10732 on 2018/5/28.
 */

public class TTFTextNumber extends TTFTextView {
    private String currencyValue;
    private int decimal;

    public void setCurrentValue(String currencyValue, int decimal) {
        this.currencyValue = currencyValue;
        this.decimal = decimal;
    }

    public String getCurrentValue() {
        return TextUtils.isEmpty(currencyValue) ? "0" : currencyValue;
    }

    public int getDecimal() {
        return decimal;
    }

    public TTFTextNumber(Context context) {
        super(context);
    }

    public TTFTextNumber(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TTFTextNumber(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNumberWithoutAnimate(String value, final int decimal) {
        if (TextUtils.isEmpty(value)) value = "0";
        setCurrentValue(value, decimal);
        setText(MathUtil.getFormatNumber(Double.parseDouble(value), decimal));
    }

    public void setNumber(String value, final int decimal) {
        if (TextUtils.isEmpty(value)) value = "0";
        setCurrentValue(value, decimal);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, Float.parseFloat(value));
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float tempValue = (float) animation.getAnimatedValue();
                setText(MathUtil.getFormatNumber(tempValue, decimal));
            }
        });
        final String finalValue = value;
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setText(MathUtil.getFormatNumber(Double.valueOf(finalValue), decimal));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setText(MathUtil.getFormatNumber(Double.valueOf(finalValue), decimal));
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    public ValueAnimator setNumberForShowOff(float fromValue, final float toValue, final int decimal, int duration, int delay) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromValue, toValue);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float tempValue = (float) animation.getAnimatedValue();
                if (tempValue < 10) {
                    setText("0" + MathUtil.getFormatNumber(tempValue, decimal));
                } else {
                    setText(MathUtil.getFormatNumber(tempValue, decimal));
                }
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (toValue < 10) {
                    setText("0" + MathUtil.getFormatNumber(toValue, decimal));
                } else {
                    setText(MathUtil.getFormatNumber(toValue, decimal));
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (toValue < 10) {
                    setText("0" + MathUtil.getFormatNumber(toValue, decimal));
                } else {
                    setText(MathUtil.getFormatNumber(toValue, decimal));
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setStartDelay(delay);
        valueAnimator.start();

        return valueAnimator;
    }

}