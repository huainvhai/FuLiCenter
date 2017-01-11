package cn.ucai.fulicenter.application;

import android.app.Application;

/**
 * Created by Administrator on 2017/1/10.
 */

public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;

    public static FuLiCenterApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
