package cn.dahuoji.body_temperature.skinview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;


/**
 * Created by 10732 on 2019/12/20 0020.
 * 解决异常：
 * ViewPager 在更新内容的时候报 java.lang.IndexOutOfBoundsException: Invalid index 0, size is 0 异常
 */

public class StrongViewPager extends ViewPager {

    private Context context;
    public boolean enabled = true;

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public StrongViewPager(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public StrongViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onDispatchTouchEventListener != null) {
                    onDispatchTouchEventListener.onTouchDown();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (onDispatchTouchEventListener != null) {
                    onDispatchTouchEventListener.onTouchUp();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.enabled) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                return false;
            }
            try {
                return super.onTouchEvent(ev);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.enabled) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                return false;
            }
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (onDispatchTouchEventListener != null) {
            onDispatchTouchEventListener.onAttachedToWindow();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (context != null && context instanceof Activity && ((Activity) context).isFinishing()) {
            super.onDetachedFromWindow();
        }
        if (onDispatchTouchEventListener != null) {
            onDispatchTouchEventListener.onDetachedFromWindow();
        }
    }

    private OnDispatchTouchEventListener onDispatchTouchEventListener;

    public void setOnDispatchTouchEventListener(OnDispatchTouchEventListener onDispatchTouchEventListener) {
        this.onDispatchTouchEventListener = onDispatchTouchEventListener;
    }

    public interface OnDispatchTouchEventListener {
        void onTouchDown();

        void onTouchUp();

        void onAttachedToWindow();

        void onDetachedFromWindow();
    }
}