package cn.dahuoji.body_temperature;

import android.app.Application;
import android.content.Context;

public class LocalApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
