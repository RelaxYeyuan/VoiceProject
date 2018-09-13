package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iflytek.platform.type.PlatformCode;
import com.iflytek.platformservice.PlatformService;

import java.lang.ref.WeakReference;

/**
 * Created by chenhongrui on 2018/9/10
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class VoiceChannelManager {

    private static VoiceChannelManager instance;
    private MyHandler myHandler;
    private Context context;

    private static final String TAG = "VoiceChannelManager";

    private VoiceChannelManager(Context context) {
        Log.d(TAG, "VoiceChannelManager: ");
        this.context = context;
        myHandler = new MyHandler(this);

    }

    public static VoiceChannelManager getInstance(Context context) {
        if (instance == null) {
            synchronized (VoiceChannelManager.class) {
                if (instance == null) {
                    instance = new VoiceChannelManager(context);
                }
            }
        }
        return instance;
    }

    public void sendMessageWakeup() {
        myHandler.sendEmptyMessage(WAKEUP_VOICE);
        Log.d(TAG, "sendMessageWakeup: ");
    }

    public void sendMessageCloseVoice() {
        myHandler.sendEmptyMessage(CLOSE_VOICE);
        Log.d(TAG, "sendMessageCloseVoice: ");
    }

    public void sendMessageCloseActivity() {
        myHandler.sendEmptyMessage(CLOSE_VOICE_ACTIVITY);
    }

    private static final int WAKEUP_VOICE = 1;
    private static final int CLOSE_VOICE = 2;
    private static final int CLOSE_VOICE_ACTIVITY = 3;

    private static class MyHandler extends Handler {
        WeakReference<VoiceChannelManager> mReference;

        MyHandler(VoiceChannelManager voiceChannelManager) {
            this.mReference = new WeakReference<>(voiceChannelManager);
        }

        @Override
        public void handleMessage(Message msg) {
            VoiceChannelManager voiceChannelManager = mReference.get();
            switch (msg.what) {
                case WAKEUP_VOICE:
                    voiceChannelManager.wakeupVoice();
                    break;
                case CLOSE_VOICE:
                    voiceChannelManager.closeVoice();
                    break;
                case CLOSE_VOICE_ACTIVITY:
                    voiceChannelManager.closeVoiceActivity();
                    break;
            }
        }
    }

    /**
     * 通知语音助理，恢复之前的状态
     */
    private void wakeupVoice() {
        if (PlatformService.platformCallback == null) {
            Log.e(TAG, "phoneStateChange: platformCallback == null");
            return;
        }

        try {
            PlatformService.platformCallback.systemStateChange(PlatformCode.STATE_SPEECHON);
            Log.d(TAG, "wakeupVoice: ");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "wakeupVoice: " + e.getLocalizedMessage());
        }

    }

    /**
     * 通知语音助理，释放录音通道，并且界面退出
     */
    private void closeVoice() {
        if (PlatformService.platformCallback == null) {
            Log.e(TAG, "phoneStateChange: platformCallback == null");
            return;
        }

        try {
            PlatformService.platformCallback.systemStateChange(PlatformCode.STATE_SPEECHOFF);
            Log.d(TAG, "closeVoice: ");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "closeVoice: " + e.getLocalizedMessage());
        }
    }

    /**
     * 通知语音助理，当前界面退出
     */
    private void closeVoiceActivity() {
        if (PlatformService.platformCallback == null) {
            Log.e(TAG, "phoneStateChange: platformCallback == null");
            return;
        }

        try {
            PlatformService.platformCallback.systemStateChange(PlatformCode.STATE_VIEWOFF);
            Log.d(TAG, "closeVoiceActivity: ");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "closeVoiceActivity: " + e.getLocalizedMessage());
        }
    }
}
