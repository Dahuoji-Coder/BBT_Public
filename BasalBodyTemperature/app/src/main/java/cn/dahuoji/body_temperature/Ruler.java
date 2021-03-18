package cn.dahuoji.body_temperature;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import cn.dahuoji.body_temperature.util.MathUtil;
import cn.dahuoji.body_temperature.util.WindowUtil;

public class Ruler extends View {

    private Context context;
    private Paint paint;
    private float lastTouchX;
    private Scroller mScroller;
    private VelocityTracker velocityTracker;
    private boolean isFling;
    private int limitItemWidth;
    private int minScrollX;
    private int maxScrollX;
    private TextPaint textPaint;
    private float textSize;
    private float textOffsetY;

    public Ruler(Context context) {
        super(context);
        init(context);
    }

    public Ruler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.parseColor("#CC3333"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(WindowUtil.dip2px(context, 2));
        paint.setAntiAlias(true);
        textPaint = new TextPaint();
        textPaint.setColor(Color.parseColor("#CC3333"));
        textSize = WindowUtil.dip2px(context, 15);
        textOffsetY = WindowUtil.dip2px(context, 3);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        //
        mScroller = new Scroller(context, null);
        //
        limitItemWidth = WindowUtil.dip2px(context, 5);//0.01度
        minScrollX = -limitItemWidth * 100;
        maxScrollX = limitItemWidth * 200;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                isFling = false;
                lastTouchX = currentX;
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                velocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (velocityTracker != null) {
                    velocityTracker.addMovement(event);
                }
                float dX = lastTouchX - currentX;
                if (getScrollX() + dX < minScrollX) {
                    scrollTo(minScrollX, 0);
                } else if (getScrollX() + dX > maxScrollX) {
                    scrollTo(maxScrollX, 0);
                } else {
                    scrollBy((int) dX, 0);
                }
                lastTouchX = currentX;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (velocityTracker != null) {
                    velocityTracker.computeCurrentVelocity(1000);
                    float xVelocity = velocityTracker.getXVelocity();
                    //Log.d(">>>ScrollableLayout", "x 轴速度: " + xVelocity + ", y 轴速度: " + yVelocity);
                    velocityTracker.recycle();
                    velocityTracker = null;
                    if (getScrollX() <= minScrollX) {
                        scrollTo(minScrollX, 0);
                    } else if (getScrollX() >= maxScrollX) {
                        scrollTo(maxScrollX, 0);
                    } else {
                        mScroller.fling(getScrollX(), 0, (int) -xVelocity, 0, minScrollX, maxScrollX, 0, 0);
                        isFling = true;
                        postInvalidate();
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    private float lastValue;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int nextX = mScroller.getCurrX();
            scrollTo(nextX, 0);
            postInvalidate();
            //Log.d(">>>Ruler", "===nextY: " + nextX);
        } else {
            //Log.d(">>>Ruler", "===nextY: " + "stopped");
        }
        if (onCursorChangedListener != null) {
            if (resetValue) {
                resetValue = false;
                return;
            }
            String value = MathUtil.getFormatNumberForElectricity(36 + getScrollX() * 1.0f / limitItemWidth * 0.01, 2);
            if (Float.parseFloat(value) != lastValue) {
                onCursorChangedListener.onCursorChanged(value);
                Log.d(">>>Ruler", "===value: " + value);
            }
            lastValue = Float.parseFloat(value);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int largeScaleHeight = WindowUtil.dip2px(context, 15);
        int middleScaleHeight = WindowUtil.dip2px(context, 10);
        int smallScaleHeight = WindowUtil.dip2px(context, 5);
        int baseLineHeight = (int) (height - (textSize + textOffsetY + getPaddingBottom()));
        //一根长横轴线
        float startX = width / 2.0f - limitItemWidth * 100;
        canvas.drawLine(startX - 50, baseLineHeight, startX + limitItemWidth * 300 + 50, baseLineHeight, paint);
        //画小竖条
        for (int i = 0; i < 301; i++) {
            float itemX = startX + limitItemWidth * i;
            int scaleHeight;
            if (i % 100 == 0) {
                textPaint.setTextSize(WindowUtil.dip2px(context, 15));
                textPaint.setColor(Color.parseColor("#CC3333"));
                canvas.drawText(MathUtil.getFormatNumberForElectricity(35 + 0.01 * i, 0), itemX, baseLineHeight + textSize + textOffsetY, textPaint);
                scaleHeight = largeScaleHeight;
            } else if (i % 10 == 0) {
                textPaint.setTextSize(WindowUtil.dip2px(context, 10));
                String number = MathUtil.getFormatNumberForElectricity(35 + 0.01 * i, 1);
                if (number.endsWith(".5")) {
                    textPaint.setColor(Color.parseColor("#CC3333"));
                    canvas.drawText(number, itemX, baseLineHeight + textSize + textOffsetY, textPaint);
                } else {
                    textPaint.setColor(Color.parseColor("#336633"));
                    canvas.drawText(number.split("\\.")[1], itemX, baseLineHeight + textSize + textOffsetY, textPaint);
                }
                scaleHeight = middleScaleHeight;
            } else {
                scaleHeight = smallScaleHeight;
            }
            canvas.drawLine(itemX, baseLineHeight - scaleHeight, itemX, baseLineHeight, paint);
        }
    }

    private boolean resetValue = false;

    public void setValue(float value) {
        resetValue = true;
        scrollTo((int) (limitItemWidth * ((value - 36) / 0.01f)), 0);
    }

    private OnCursorChangedListener onCursorChangedListener;

    public void setOnCursorChangedListener(OnCursorChangedListener onCursorChangedListener) {
        this.onCursorChangedListener = onCursorChangedListener;
    }

    public interface OnCursorChangedListener {
        void onCursorChanged(String value);
    }
}
