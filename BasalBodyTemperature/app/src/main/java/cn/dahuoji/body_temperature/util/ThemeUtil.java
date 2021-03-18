package cn.dahuoji.body_temperature.util;

import android.text.TextUtils;

import cn.dahuoji.body_temperature.LocalApplication;

/**
 * Created by 10732 on 2019/7/1.
 */

public class ThemeUtil {

    public static final int VIP = 10001;
    public static final int DARK = 10002;

    private static int theme;
    public static final String THEME_VIP_ = "theme_vip_";
    public static final String THEME_DARK_ = "theme_dark_";
    private static boolean isCurrentUserVIP = false;

    public static int getTheme() {
        return theme;
    }

    public static int getResourceIdByTheme(int resId) {
        if (resId != 0) {
            String theme_;
            if (theme == VIP) {
                theme_ = THEME_VIP_;
            } else if (theme == DARK) {
                theme_ = THEME_DARK_;
            } else {
                return resId;
            }
            String resourceName = LocalApplication.getContext().getResources().getResourceName(resId);
            if (!TextUtils.isEmpty(resourceName)) {
                if (resourceName.contains(":")) {
                    resourceName = resourceName.split(":")[1];
                    if (resourceName.contains("/")) {
                        String imageType = resourceName.split("/")[0];
                        String imageName = resourceName.split("/")[1];
                        if (!imageName.startsWith(theme_)) {
                            int vipResourceId = LocalApplication.getContext().getResources().getIdentifier(theme_ + imageName, imageType, LocalApplication.getContext().getPackageName());
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

    public static int getThemeColorById(int colorId) {
        if (colorId != 0) {
            colorId = getResourceIdByTheme(colorId);
        }
        return ResourcesUtil.getColor(LocalApplication.getContext(), colorId);
    }
}
