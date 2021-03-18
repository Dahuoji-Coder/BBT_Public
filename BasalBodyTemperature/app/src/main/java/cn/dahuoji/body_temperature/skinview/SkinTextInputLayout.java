package cn.dahuoji.body_temperature.skinview;

import android.content.Context;

import com.google.android.material.textfield.TextInputLayout;
import android.util.AttributeSet;

import cn.dahuoji.body_temperature.util.ThemeUtil;

/**
 * Created by Yang Jiwei on 2020/4/27 0027.
 */

public class SkinTextInputLayout extends TextInputLayout {
    public SkinTextInputLayout(Context context) {
        super(context);
        init(context);
    }

    public SkinTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SkinTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (ThemeUtil.getTheme() == ThemeUtil.DARK) {
            setAlpha(0.7f);
        } else {
            setAlpha(1.0f);
        }
    }
}
