package cn.dahuoji.body_temperature.skinview;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import cn.dahuoji.body_temperature.R;
import cn.dahuoji.body_temperature.util.ThemeUtil;

/**
 * Created by Yang Jiwei on 2020/4/27 0027.
 */

public class SkinRecyclerView extends RecyclerView {

    private Context context;
    private int currentBackgroundResourceId;
    private int maxHeight;

    public SkinRecyclerView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public SkinRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SkinRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        this.context = context;
        if (isInEditMode()) return;
        if (attrs != null) {
            currentBackgroundResourceId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", 0);
            TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SkinRecyclerView);
            maxHeight = arr.getLayoutDimension(R.styleable.SkinRecyclerView_maxHeight, 0);
            arr.recycle();
        }
        updateTheme();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (maxHeight > 0) {
            heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightSpec);
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

}
