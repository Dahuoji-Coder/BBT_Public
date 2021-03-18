package cn.dahuoji.body_temperature.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import cn.dahuoji.body_temperature.LocalApplication;

/**
 * Created by 10732 on 2018/5/11.
 */

public class WindowUtil {

    public static void setNavigationBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(color);
        }
    }

    public static void setNavigationBarStyle(Activity activity, boolean light) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int vis = activity.getWindow().getDecorView().getSystemUiVisibility();
            if (light) {
                //黑色
                vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                //白色
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(vis);
        }
    }

    public static void setStatusBarHeight(View view) {
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) view.getLayoutParams();
        layoutParams.height = getStatusBarHeight(LocalApplication.getContext());
        view.setLayoutParams(layoutParams);
    }

    //获取状态栏高度 如果系统版本4.4以下，返回 0
    public static int getStatusBarHeight(Context context) {
        if (context == null) return 50;

        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                    "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    //Activity视图高度
    public static int getActivityHeight(Activity context) {
        if (context == null || context.isFinishing()) return 1920;

        Rect outRect1 = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        int activityHeight = outRect1.height();
        return activityHeight;
    }

    public static void setStatusBarStyle(Activity activity, boolean isLight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //android6.0以后可以对状态栏文字颜色和图标进行修改
            if (isLight) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    public static int[] getScreenValues() {
        DisplayMetrics dm = LocalApplication.getContext().getResources().getDisplayMetrics();
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    public static int getRealScreenHeight(Activity context) {
        if (context == null || context.isFinishing()) return getScreenValues()[1];

        WindowManager windowManager =
                (WindowManager) context.getApplication().getSystemService(Context.WINDOW_SERVICE);
        final Display display;
        if (windowManager != null) {
            display = windowManager.getDefaultDisplay();
            Point outPoint = new Point();
            if (Build.VERSION.SDK_INT >= 19) {
                // 可能有虚拟按键的情况
                display.getRealSize(outPoint);
            } else {
                // 不可能有虚拟按键
                display.getSize(outPoint);
            }
            int mRealSizeWidth;//手机屏幕真实宽度
            int mRealSizeHeight;//手机屏幕真实高度
            mRealSizeHeight = outPoint.y;
            mRealSizeWidth = outPoint.x;
            return mRealSizeHeight;
        }
        return getScreenValues()[1];
    }

    public static int getScreenHeightExceptGuide(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int height = outMetrics.heightPixels;
        return height;
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
