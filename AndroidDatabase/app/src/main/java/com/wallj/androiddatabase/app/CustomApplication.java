
package com.wallj.androiddatabase.app;

import android.app.Application;

/**
 * 描述：
 *
 * @author walljiang
 * @since 2017/06/26 12:06
 */

public class CustomApplication extends Application {
    static CustomApplication mInstance;

    public static CustomApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
