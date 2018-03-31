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

    public static void wakeupVoice() {
        if (PlatformService.platformCallback == null) {
            Log.e(TAG, "phoneStateChange: platformCallback == null");
            return;
        }

        try {
            PlatformService.platformCallback.systemStateChange(PlatformCode.STATE_SPEECHON);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeVoice() {
        if (PlatformService.platformCallback == null) {
            Log.e(TAG, "phoneStateChange: platformCallback == null");
            return;
        }

        try {
            PlatformService.platformCallback.systemStateChange(PlatformCode.STATE_SPEECHOFF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
