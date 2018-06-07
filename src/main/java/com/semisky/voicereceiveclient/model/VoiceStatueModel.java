package com.semisky.voicereceiveclient.model;

/**
 * Created by chenhongrui on 2018/6/7
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class VoiceStatueModel {

    private static final String TAG = "VoiceStatueModel";

    private static VoiceStatueModel INSTANCE;

    private int closeVoice;
    private int wakeupVoice;

    public static final int BT_CALL = 1;
    public static final int CLOCK_SCREEN = 2;
    public static final int BACKLIGHT_SCREEN = 3;
    public static final int CARLIFE = 4;
    public static final int BACK_CAR = 5;

    public static VoiceStatueModel getInstance() {
        if (INSTANCE == null) {
            synchronized (VoiceStatueModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VoiceStatueModel();
                }
            }
        }
        return INSTANCE;
    }

    private VoiceStatueModel() {
    }

    public int getCloseVoice() {
        return closeVoice;
    }

    public void setCloseVoice(int closeVoice) {
        this.closeVoice = closeVoice;
    }

    public int getWakeupVoice() {
        return wakeupVoice;
    }

    public void setWakeupVoice(int wakeupVoice) {
        this.wakeupVoice = wakeupVoice;
    }
}
