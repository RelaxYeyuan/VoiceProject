package com.semisky.voicereceiveclient.model;

import android.util.Log;

import com.iflytek.platform.type.PlatformCode;
import com.iflytek.platformservice.PlatformService;

/**
 * Created by chenhongrui on 2018/3/31
 * <p>
 * 内容摘要：关闭语音唤醒
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class VoiceWakeupScenes {

    private static final String TAG = "VoiceWakeupScenes";

    /**
     * 通知语音助理，恢复之前的状态
     */
    public static boolean wakeupVoice(int statue) {
        if (PlatformService.platformCallback == null) {
            Log.e(TAG, "phoneStateChange: platformCallback == null");
            return false;
        }

        try {
            PlatformService.platformCallback.systemStateChange(PlatformCode.STATE_SPEECHON);
            VoiceStatueModel.getInstance().setWakeupVoice(statue);
            Log.d(TAG, "wakeupVoice: ");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "wakeupVoice: " + e.getLocalizedMessage());
            return false;
        }

    }

    /**
     * 通知语音助理，释放录音通道，并且界面退出
     */
    public static boolean closeVoice(int statue) {
        if (PlatformService.platformCallback == null) {
            Log.e(TAG, "phoneStateChange: platformCallback == null");
            return false;
        }

        try {
            PlatformService.platformCallback.systemStateChange(PlatformCode.STATE_SPEECHOFF);
            VoiceStatueModel.getInstance().setCloseVoice(statue);
            Log.d(TAG, "closeVoice: ");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "closeVoice: " + e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * 通知语音助理，当前界面退出
     */
    public static void closeVoiceActivity() {
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

    /**
     * 恢复唤醒词
     */
    public static void restoreWakeWord() {
        if (PlatformService.platformCallback == null) {
            Log.e(TAG, "phoneStateChange: platformCallback == null");
            return;
        }

        try {
            int flag = PlatformService.platformCallback.systemStateChange(101);
            Log.d(TAG, "restoreWakeWord: flag " + flag);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "restoreWakeWord: " + e.getLocalizedMessage());
        }
    }

}
