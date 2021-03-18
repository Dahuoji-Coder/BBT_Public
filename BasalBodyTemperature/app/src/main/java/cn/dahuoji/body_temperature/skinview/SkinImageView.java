package cn.dahuoji.body_temperature.skinview;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import android.text.TextUtils;
import android.util.AttributeSet;

import cn.dahuoji.body_temperature.util.ThemeUtil;

/**
 * Created by 10732 on 2019/7/2.
 */
public class SkinImageView extends AppCompatImageView {

    private Context context;
    private int currentTheme;
    private int initial_src_resource;
    private int initialBackgroundResourceId;

    public SkinImageView(Context context) {
        super(context);
    }

    public SkinImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SkinImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (isInEditMode()) return;
        this.context = context;
        if (attrs != null) {
            initial_src_resource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
            initialBackgroundResourceId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", 0);
        }
        updateTheme();
    }

    @Override
    public void setImageResource(int resId) {
        currentTheme = ThemeUtil.getTheme();
        String resourceName = getResourceName(resId);
        if (!TextUtils.isEmpty(resourceName) && !resourceName.startsWith("theme_vip_") && !resourceName.startsWith("theme_dark_")) {
            initial_src_resource = resId;
        }
        if (currentTheme == ThemeUtil.VIP) {
            resId = getResourceIdByTheme(resId, ThemeUtil.THEME_VIP_);
        } else if (currentTheme == ThemeUtil.DARK) {
            resId = getResourceIdByTheme(resId, ThemeUtil.THEME_DARK_);
        }
        super.setImageResource(resId);
    }

    @Override
    public void setBackgroundResource(int resId) {
        currentTheme = ThemeUtil.getTheme();
        if (currentTheme == ThemeUtil.VIP) {
            resId = getResourceIdByTheme(resId, ThemeUtil.THEME_VIP_);
        } else if (currentTheme == ThemeUtil.DARK) {
            resId = getResourceIdByTheme(resId, ThemeUtil.THEME_DARK_);
        }
        super.setBackgroundResource(resId);
    }

    private String getResourceName(int resId) {
        if (resId != 0) {
            String resourceName = getResources().getResourceName(resId);
            if (!TextUtils.isEmpty(resourceName)) {
                if (resourceName.contains(":")) {
                    resourceName = resourceName.split(":")[1];
                    if (resourceName.contains("/")) {
                        return resourceName.split("/")[1];
                    }
                }
            }
        }

        return "";
    }

    private int getResourceIdByTheme(int resId, String theme_) {
        if (resId != 0) {
            String resourceName = getResources().getResourceName(resId);
            if (!TextUtils.isEmpty(resourceName)) {
                if (resourceName.contains(":")) {
                    resourceName = resourceName.split(":")[1];
                    if (resourceName.contains("/")) {
                        String imageType = resourceName.split("/")[0];
                        String imageName = resourceName.split("/")[1];
                        if (!imageName.startsWith(theme_)) {
                            int vipResourceId = getResources().getIdentifier(theme_ + imageName, imageType, context.getPackageName());
                            if (vipResourceId != 0) {
                                resId = vipResourceId;
                            }
                        }
                    }
                }
            }
        }

        return resId;
    }

    public void updateTheme() {
        currentTheme = ThemeUtil.getTheme();
        if (currentTheme == ThemeUtil.VIP) {
            int src_resource = initial_src_resource;
            src_resource = getResourceIdByTheme(src_resource, ThemeUtil.THEME_VIP_);
            if (src_resource != 0) {
                setImageResource(src_resource);
            }
            int backgroundResourceId = initialBackgroundResourceId;
            backgroundResourceId = getResourceIdByTheme(backgroundResourceId, ThemeUtil.THEME_VIP_);
            if (backgroundResourceId != 0) {
                setBackgroundResource(backgroundResourceId);
            }
        } else if (currentTheme == ThemeUtil.DARK) {
            int src_resource = initial_src_resource;
            src_resource = getResourceIdByTheme(src_resource, ThemeUtil.THEME_DARK_);
            if (src_resource != 0) {
                setImageResource(src_resource);
            }
            int backgroundResourceId = initialBackgroundResourceId;
            backgroundResourceId = getResourceIdByTheme(backgroundResourceId, ThemeUtil.THEME_DARK_);
            if (backgroundResourceId != 0) {
                setBackgroundResource(backgroundResourceId);
            }
        } else {
            int src_resource = initial_src_resource;
            if (src_resource != 0) {
                setImageResource(src_resource);
            }
            int backgroundResourceId = initialBackgroundResourceId;
            if (backgroundResourceId != 0) {
                setBackgroundResource(backgroundResourceId);
            }
        }
    }
}