package cn.dahuoji.body_temperature.skinview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import cn.dahuoji.body_temperature.util.MathUtil;

/**
 * Created by 10732 on 2018/5/28.
 */

public class TTFTextReSize extends TTFTextView {

    private TextPaint textPaint;
    private int measuredWidth;

    public TTFTextReSize(Context context) {
        super(context);
    }

    public TTFTextReSize(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialise(context, attrs);
    }

    public TTFTextReSize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise(context, attrs);
    }

    private void initialise(Context context, AttributeSet attrs) {
        textPaint = this.getPaint();
        setSingleLine(true);

//        if (!isInEditMode()) {
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.pfStyle);
//            String textStyle = typedArray.getString(R.styleable.pfStyle_textStyle);
//            typedArray.recycle();
//            if (SPUtil.getLanguage().startsWith("zh")) {
//                if (null != textStyle && "bold".equals(textStyle)) {
//                    setTypeface(LocalApplication.getPf_normal(), Typeface.BOLD);
//                } else {
//                    setTypeface(LocalApplication.getPf_normal());
//                }
//            } else {
//                if (null != textStyle && "bold".equals(textStyle)) {
//                    setTypeface(LocalApplication.getPf_en(), Typeface.BOLD);
//                } else {
//                    setTypeface(LocalApplication.getPf_en());
//                }
//            }
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measuredWidth = getMeasuredWidth();
    }

    public void setLongText(String str, int maxWidth) {
        float measureText = textPaint.measureText(str);
        while (measureText > maxWidth) {
            textPaint.setTextSize(textPaint.getTextSize() - 1);
            measureText = textPaint.measureText(str);
        }
        TTFTextReSize.this.setText(str);
    }

    public ValueAnimator setNumber(float fromValue, final double toValue, final int decimal, final String coinType, int duration, int delay) {
        float measureText = textPaint.measureText(MathUtil.getFormatNumber(toValue, decimal) + " " + coinType);
        while (measureText > measuredWidth) {
            textPaint.setTextSize(textPaint.getTextSize() - 1);
            measureText = textPaint.measureText(MathUtil.getFormatNumber(toValue, decimal) + " " + coinType);
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromValue, (float) toValue);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float tempValue = (float) animation.getAnimatedValue();
                String currentValue = MathUtil.getFormatNumber(tempValue, decimal) + " " + coinType;
                TTFTextReSize.this.setText(currentValue);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                TTFTextReSize.this.setText(MathUtil.getFormatNumber(toValue, decimal) + " " + coinType);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                TTFTextReSize.this.setText(MathUtil.getFormatNumber(toValue, decimal) + " " + coinType);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setStartDelay(delay);
        valueAnimator.start();

        return valueAnimator;
    }

    public ValueAnimator setMoneyNumber(float fromValue, final double toValue, final int decimal, final String moneyUnit, int duration, int delay) {
        String finalText = "≈ " + moneyUnit + " " + MathUtil.getFormatNumberWithThousandPlace(toValue, decimal);
        float measureText = textPaint.measureText(finalText);
        while (measureText > measuredWidth) {
            textPaint.setTextSize(textPaint.getTextSize() - 1);
            measureText = textPaint.measureText(finalText);
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromValue, (float) toValue);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float per = (float) animation.getAnimatedValue();
                String currentValue = "≈ " + moneyUnit + " " + MathUtil.getFormatNumberWithThousandPlace(per, decimal);
                TTFTextReSize.this.setText(currentValue);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                TTFTextReSize.this.setText("≈ " + moneyUnit + " " + MathUtil.getFormatNumberWithThousandPlace(toValue, decimal));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                TTFTextReSize.this.setText("≈ " + moneyUnit + " " + MathUtil.getFormatNumberWithThousandPlace(toValue, decimal));
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