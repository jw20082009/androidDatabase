
package com.wallj.androiddatabase.app;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * 描述：
 *
 * @author walljiang
 * @since 2017/06/26 16:57
 */

public class BaseActivity extends Activity {

    protected UiHandler mUiHandler = new UiHandler(this);

    private static class UiHandler extends Handler {
        private final WeakReference<BaseActivity> mReference;

        public UiHandler(BaseActivity activity) {
            mReference = new WeakReference<BaseActivity>(activity);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mReference.get() != null) {
                mReference.get().handleUiMessage(msg);
            }
        };

        public void release() {
            removeCallbacksAndMessages(null);
            mReference.clear();
        }
    }

    private final int MSG_UI_SHOWTOAST = 0xaaa;

    /**
     * 处理更新UI任务
     *
     * @param msg
     */
    protected void handleUiMessage(Message msg) {
        switch (msg.what) {
            case MSG_UI_SHOWTOAST: {
                Object obj = msg.obj;
                if (obj != null) {
                    String text = (String) obj;
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
            }
                break;
        }
    }

    /**
     * 发送UI更新操作
     *
     * @param msg
     */
    protected void sendUiMessage(Message msg) {
        mUiHandler.sendMessage(msg);
    }

    protected void sendUiMessageDelayed(Message msg, long delayMillis) {
        mUiHandler.sendMessageDelayed(msg, delayMillis);
    }

    /**
     * 发送UI更新操作
     *
     * @param what
     */
    protected void sendEmptyUiMessage(int what) {
        mUiHandler.sendEmptyMessage(what);
    }

    protected void sendEmptyUiMessageDelayed(int what, long delayMillis) {
        mUiHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    protected void removeUiMessage(int what) {
        mUiHandler.removeMessages(what);
    }

    protected Message obtainUiMessage() {
        return mUiHandler.obtainMessage();
    }

    protected Message obtainUiMessage(int what) {
        return mUiHandler.obtainMessage(what);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUiHandler.release();
    }

    protected void showToast(String text) {
        Message toastMsg = obtainUiMessage(MSG_UI_SHOWTOAST);
        toastMsg.obj = text;
        toastMsg.sendToTarget();
    }
}
