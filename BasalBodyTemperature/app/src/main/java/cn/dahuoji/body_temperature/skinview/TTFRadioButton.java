package cn.dahuoji.body_temperature.skinview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRadioButton;

import cn.dahuoji.body_temperature.util.SPUtil;
import cn.dahuoji.body_temperature.util.WindowUtil;

/**
 * Created by 10732 on 2018/5/28.
 */

public class TTFRadioButton extends AppCompatRadioButton {

    public TTFRadioButton(Context context) {
        super(context);
        init();
    }

    public TTFRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TTFRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int sH = WindowUtil.getScreenValues()[1];
        int width = (int) (sH / 32);
        if (width > 60) width = 60;
        Drawable top = getCompoundDrawables()[1];
        if (top != null) {
            top.setBounds(0, 0, width, width);
            setCompoundDrawables(null, top, null, null);
        }
//        if (SPUtil.getLanguage().startsWith("zh")) {
//            setTypeface(LocalApplication.getPf_normal());
//        } else {
//            setTypeface(LocalApplication.getPf_en());
//        }
    }
}
