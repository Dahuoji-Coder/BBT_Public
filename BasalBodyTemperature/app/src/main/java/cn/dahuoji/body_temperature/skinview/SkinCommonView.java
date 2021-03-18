package cn.dahuoji.body_temperature.skinview;

import android.content.Context;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.View;

import cn.dahuoji.body_temperature.util.ThemeUtil;

/**
 * Created by 10732 on 2019/7/2.
 */

public class SkinCommonView extends View {

    private int currentBackgroundResourceId;

    public SkinCommonView(Context context) {
        super(context);
        initView(context, null);
    }

    public SkinCommonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SkinCommonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        if (isInEditMode()) return;
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
        resId = ThemeUtil.getResourceIdByTheme(resId);
        super.setBackgroundResource(resId);
    }
}
