package cn.dahuoji.body_temperature.skinview;

import android.content.Context;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import cn.dahuoji.body_temperature.util.ThemeUtil;

/**
 * Created by 10732 on 2019/7/3.
 */

public class SkinLinearLayout extends LinearLayout {

    private Context context;
    private int currentBackgroundResourceId;
    private MotionEvent motionEvent;

    public SkinLinearLayout(Context context) {
        super(context);
    }

    public SkinLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SkinLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        if (attrs != null) {
            currentBackgroundResourceId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", 0);
        }
        updateTheme();
    }

    public void updateTheme() {
        int backgroundResourceId = currentBackgroundResourceId;
        backgroundResourceId = ThemeUtil.getResourceIdByTheme(backgroundResourceId);
        if (backgroundResourceId != 0) {
            setBackgroundResource(backgroundResourceId);
        }
    }

    @Override
    public void setBackgroundResource(int resId) {
        //currentBackgroundResourceId = resId;
        resId = ThemeUtil.getResourceIdByTheme(resId);
        super.setBackgroundResource(resId);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        motionEvent = ev;
        return super.dispatchTouchEvent(ev);
    }

    public MotionEvent getMotionEvent() {
        return motionEvent;
    }
}