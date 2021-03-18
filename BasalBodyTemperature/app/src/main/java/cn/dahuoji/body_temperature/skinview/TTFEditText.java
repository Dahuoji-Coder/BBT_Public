package cn.dahuoji.body_temperature.skinview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import java.lang.reflect.Field;

import cn.dahuoji.body_temperature.R;
import cn.dahuoji.body_temperature.util.ResourcesUtil;
import cn.dahuoji.body_temperature.util.ThemeUtil;

/**
 * Created by 10732 on 2018/5/28.
 */

public class TTFEditText extends AppCompatEditText {

    private Context context;
    private int initialBackgroundResourceId,
            currentTextColorId, currentHintTextColorId, textCursorDrawableId;

    public TTFEditText(Context context) {
        super(context);
    }

    public TTFEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TTFEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
//        if (!isInEditMode()) {
//            if (SPUtil.getLanguage().startsWith("zh")) {
//                setTypeface(LocalApplication.getPf_normal());
//            } else {
//                setTypeface(LocalApplication.getPf_en());
//            }
//        }
        initialBackgroundResourceId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", 0);
        currentTextColorId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textColor", 0);
        currentHintTextColorId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textColorHint", 0);
        textCursorDrawableId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textCursorDrawable", 0);

        updateTheme();
    }

    public void updateTheme() {
        int backgroundResourceId = initialBackgroundResourceId;
        backgroundResourceId = ThemeUtil.getResourceIdByTheme(backgroundResourceId);
        if (backgroundResourceId != 0) {
            setBackgroundResource(backgroundResourceId);
        }

        int textColorId = ThemeUtil.getResourceIdByTheme(currentTextColorId);
        if (textColorId != 0) {
            setTextColor(ResourcesUtil.getColor(context, textColorId));
        }
        int hintTextColorId = ThemeUtil.getResourceIdByTheme(currentHintTextColorId);
        if (hintTextColorId != 0) {
            setHintTextColor(ResourcesUtil.getColor(context, hintTextColorId));
        }

        if (textCursorDrawableId == R.drawable.edit_text_cursor_drawable) {
            setCursorDrawableColor();
        }
    }

    /**
     * 代码设置光标颜色
     */
    public void setCursorDrawableColor() {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(this, ThemeUtil.getResourceIdByTheme(R.drawable.edit_text_cursor_drawable));
        } catch (Exception ignored) {
        }
    }

    public void setTextColorById(int colorId) {
        currentTextColorId = colorId;
        colorId = ThemeUtil.getResourceIdByTheme(colorId);
        if (colorId != 0) {
            super.setTextColor(ResourcesUtil.getColor(context, colorId));
        }
    }

    public void setHintTextColorById(int colorId) {
        currentHintTextColorId = colorId;
        colorId = ThemeUtil.getResourceIdByTheme(colorId);
        if (colorId != 0) {
            super.setHintTextColor(ResourcesUtil.getColor(context, colorId));
        }
    }

    @Override
    public int getAutofillType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //禁止EditText自动填充
            return AUTOFILL_TYPE_NONE;
        } else {
            return super.getAutofillType();
        }
    }

}