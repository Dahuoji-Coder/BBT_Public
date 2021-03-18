package cn.dahuoji.body_temperature.util;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by 10732 on 2019/12/19 0019.
 */

public class ResourcesUtil {

    public static String getString(Context context, int resourceId) {
        if (context != null) {
            return context.getResources().getString(resourceId);
        }
        return "";
    }

    public static int getColor(Context context, int resourceId) {
        if (context != null) {
            return context.getResources().getColor(resourceId);
        }
        return Color.BLACK;
    }

    public static float getDimen(Context context, int resourceId) {
        if (context != null) {
            return context.getResources().getDimension(resourceId);
        }
        return 10;
    }
}
