package cn.dahuoji.body_temperature.skinview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

/**
 * Created by 10732 on 2018/11/8.
 */

public class TTFCheckBox extends AppCompatCheckBox {
    public TTFCheckBox(Context context) {
        super(context);
    }

    public TTFCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TTFCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
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
}
