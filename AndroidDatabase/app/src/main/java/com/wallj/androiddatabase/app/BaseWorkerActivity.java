
package com.wallj.androiddatabase.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 描述：
 *
 * @author walljiang
 * @since 2017/06/26 16:57
 */

public class BaseWorkerActivity extends BaseActivity {
    protected HandlerThread mHandlerThread;

    protected BackgroundHandler mBackgroundHandler;

    // 后台Handler
    private static class BackgroundHandler extends Handler {

        private final WeakReference<BaseWorkerActivity> mFragmentReference;

        BackgroundHandler(BaseWorkerActivity fragment, Looper looper) {
            super(looper);
            mFragmentReference = new WeakReference<BaseWorkerActivity>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mFragmentReference.get() != null) {
                mFragmentReference.get().handleBackgroundMessage(msg);
            }
        }

        public void release() {
            if (getLooper() != null) {
                getLooper().quit();
            }
            removeCallbacksAndMessages(null);
            mFragmentReference.clear();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandlerThread = new HandlerThread(
                "BaseWorkerActivity worker:" + getClass().getSimpleName());
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandler(this, mHandlerThread.getLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            mBackgroundHandler.release();
        }
    }

    /**
     * 处理后台操作
     */
    public void handleBackgroundMessage(Message msg) {
    }

    /**
     * 发送后台操作
     *
     * @param msg
     */
    protected void sendBackgroundMessage(Message msg) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.sendMessage(msg);
        }
    }

    protected void sendBackgroundMessageDelayed(Message msg, long delayMillis) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.sendMessageDelayed(msg, delayMillis);
        }
    }

    /**
     * 发送后台操作
     *
     * @param what
     */
    protected void sendEmptyBackgroundMessage(int what) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.sendEmptyMessage(what);
        }
    }

    protected void sendEmptyBackgroundMessageDelayed(int what, long delayMillis) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.sendEmptyMessageDelayed(what, delayMillis);
        }
    }

    protected void removeBackgroundMessage(int what) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.removeMessages(what);
        }
    }

    protected Message obtainBackgroundMessage() {
        return mBackgroundHandler.obtainMessage();
    }

    protected Message obtainBackgroundMessage(int what) {
        return mBackgroundHandler.obtainMessage(what);
    }
}
