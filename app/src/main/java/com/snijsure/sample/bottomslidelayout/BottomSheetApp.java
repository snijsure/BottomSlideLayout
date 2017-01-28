package com.snijsure.sample.bottomslidelayout;

import android.app.Application;
import android.content.Context;

/**
 * Created by subodhnijsure on 1/26/17.
 */

public class BottomSheetApp extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

    }

    public static Context getAppContext() {
        return sContext;
    }


}
