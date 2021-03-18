package cn.dahuoji.body_temperature.skinview;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ScrollView;

import cn.dahuoji.body_temperature.util.ThemeUtil;

/**
 * Created by 10732 on 2019/5/14.
 */

public class SkinScrollView extends ScrollView {

    private Context context;
    private int currentTheme;
    private int currentBackgroundResourceId;

    public SkinScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public SkinScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SkinScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
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
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    private OnScrollChangedListener onScrollChangedListener;

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
