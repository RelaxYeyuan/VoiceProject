package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iflytek.platform.type.PlatformCode;
import com.iflytek.platformservice.PlatformService;
import com.semisky.voicereceiveclient.model.VoiceKeyModel;
import com.semisky.voicereceiveclient.model.VoiceStatueModel;

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

    public void sendMessageWakeup(int status) {
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        message.setData(bundle);
        message.what = WAKEUP_VOICE;
        myHandler.sendMessage(message);
        Log.d(TAG, "sendMessageWakeup: " + status);
    }

    public void sendMessageCloseVoice(int status) {
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        message.setData(bundle);
        message.what = CLOSE_VOICE;
        myHandler.sendMessage(message);
        Log.d(TAG, "sendMessageCloseVoice: " + status);
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
                    voiceChannelManager.wakeupVoice(msg.getData().getInt("status"));
                    VoiceKeyModel.getInstance(voiceChannelManager.context).registerOnKeyListener();
                    break;
                case CLOSE_VOICE:
                    voiceChannelManager.closeVoice(msg.getData().getInt("status"));
                    VoiceKeyModel.getInstance(voiceChannelManager.context).unregisterOnKeyListener();
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
    private void wakeupVoice(int statue) {
        if (PlatformService.platformCallback == null) {
            Log.e(TAG, "phoneStateChange: platformCallback == null");
            return;
        }

        try {
            PlatformService.platformCallback.systemStateChange(PlatformCode.STATE_SPEECHON);
            VoiceStatueModel.getInstance().setWakeupVoice(statue);
            Log.d(TAG, "wakeupVoice: ");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "wakeupVoice: " + e.getLocalizedMessage());
        }

    }

    /**
     * 通知语音助理，释放录音通道，并且界面退出
     */
    private void closeVoice(int statue) {
        if (PlatformService.platformCallback == null) {
            Log.e(TAG, "phoneStateChange: platformCallback == null");
            return;
        }

        try {
            PlatformService.platformCallback.systemStateChange(PlatformCode.STATE_SPEECHOFF);
            VoiceStatueModel.getInstance().setCloseVoice(statue);
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
