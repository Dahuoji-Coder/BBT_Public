package cn.dahuoji.body_temperature.skinview;

import android.content.Context;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cn.dahuoji.body_temperature.util.ThemeUtil;

/**
 * Created by 10732 on 2019/7/3.
 */

public class SkinRelativeLayout extends RelativeLayout {

    private Context context;
    private int currentBackgroundResourceId;

    public SkinRelativeLayout(Context context) {
        super(context);
    }

    public SkinRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SkinRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (onHeightChangedListener != null && h != oldh) {
            onHeightChangedListener.onHeightChanged(h);
        }
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }

    private OnHeightChangedListener onHeightChangedListener;

    public void setOnHeightChangedListener(OnHeightChangedListener onHeightChangedListener) {
        this.onHeightChangedListener = onHeightChangedListener;
    }

    public interface OnHeightChangedListener {
        void onHeightChanged(int height);
    }
}