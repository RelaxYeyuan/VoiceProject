package com.semisky.voicereceiveclient.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.semisky.autoservice.manager.AudioManager;
import com.semisky.autoservice.manager.AutoManager;
import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.jsonEntity.CMDEntity;
import com.semisky.voicereceiveclient.model.KWMusicAPI;
import com.semisky.voicereceiveclient.model.VoiceWakeupScenes;
import com.semisky.voicereceiveclient.utils.ToolUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_BTCALL;
import static com.semisky.voicereceiveclient.constant.AppConstant.LOOP_PLAY;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_BTCALL;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_VOICE;
import static com.semisky.voicereceiveclient.constant.AppConstant.RANDOM_PLAY;
import static com.semisky.voicereceiveclient.constant.AppConstant.SINGLE_PLAY;

/**
 * Created by chenhongrui on 2018/3/8
 * <p>
 * 内容摘要：用于CMD相关操作 focus = cmd
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class CMDVoiceManager {

    private static final String TAG = "CMDVoiceManager";
    private Context mContext;
    private KWMusicAPI kwMusicAPI;

    public CMDVoiceManager() {
        kwMusicAPI = new KWMusicAPI();
    }

    public void setActionJson(CMDEntity cmdEntity, Context context) {
        mContext = context;
        String category = cmdEntity.getCategory();
        String name = cmdEntity.getName();
        String nameValue = cmdEntity.getNameValue();

        try {
            switch (name) {
                case "切换模式":
                    changeLightMode();
                    return;
                case "返回主菜单":
                    checkVoiceActivity();
                    skipHome();
                    return;
                case "断开连接":
                    //{"name":"断开连接","focus":"cmd","rawText":"断开手机连接"}
                    AidlManager.getInstance().getBTCallListener().DisconnectThePhone();
                    return;
            }

            switch (category) {
                case "收音机控制":
                    radioControl(name);
                    break;
                case "收藏":
                    AidlManager.getInstance().getRadioListener().radioPlayCollect();
                    break;
                case "屏幕控制":
                    setControl(name, nameValue);
                    break;
                case "音量控制":
                    setControl(name, nameValue);
                    break;
                case "曲目控制":
                    videoControl(name);
                    break;
                case "播放模式":
                    videoControl(name);
                    break;
                case "汽车控制":
                    carCommand(name);
                    break;
                case "电话控制":
                    btCallOperation(name);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //{"category":"电话控制","name":"通话记录","focus":"cmd","rawText":"通话记录"}
    private void btCallOperation(String name) {
        switch (name) {
            case "通话记录":
                openBTCallConnection(5);
                break;
        }
    }

    //{"category":"收音机控制","name":"收藏本台","focus":"cmd","rawText":"收藏这个电台"}
    //{"category":"收音机控制","name":"收藏本台","focus":"cmd","rawText":"收藏这个台"}
    private void radioControl(String name) {
        try {
            switch (name) {
                case "上一频道":
                    AidlManager.getInstance().getRadioListener().seekUp();
                    break;
                case "下一频道":
                    AidlManager.getInstance().getRadioListener().seekDown();
                    break;
                case "收藏本台":
                    AidlManager.getInstance().getRadioListener().collectFreq();
                    break;
                case "扫描电台":
                    AidlManager.getInstance().getRadioListener().seekFreq();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setControl(String name, String nameValue) {
        try {
            Log.d(TAG, "setControl: " + name);
            switch (name) {
                case "亮度+":
                    Log.d(TAG, "setControl: 亮度+");
                    updateLightAdjustValue(0);
                    break;
                case "亮度-":
                    Log.d(TAG, "setControl: 亮度-");
                    updateLightAdjustValue(1);
                    break;
                case "音量+":
                    AudioManager.getInstance().upAndDownVolume(AudioManager.VR_UP_VOLUME);
                    break;
                case "音量-":
                    AudioManager.getInstance().upAndDownVolume(AudioManager.VR_DOWN_VOLUME);
                    break;
                case "音量max+":
                    AudioManager.getInstance().setVolumeTo(31);
                    break;
                case "音量min-":
                    AudioManager.getInstance().setVolumeTo(0);
                    break;
                case "音量调节":
                    int volume = Integer.valueOf(nameValue);
                    if (volume < 0) {//如果语义调节音量小于0，就默认最小
                        AudioManager.getInstance().setVolumeTo(0);
                    } else if (volume > 31) {//如果语义调节音量大于31，就默认最大
                        AudioManager.getInstance().setVolumeTo(31);
                    } else {
                        AudioManager.getInstance().setVolumeTo(volume);
                    }
                    break;
                case "静音":
                    AudioManager.getInstance().setVolumeMute(true);
                    break;
                case "打开音量":
                    AudioManager.getInstance().setVolumeMute(false);
                    break;
                case "关闭屏幕":
                    //{"category":"屏幕控制","name":"关闭屏幕","focus":"cmd","rawText":"关闭屏幕"}
                    cliseBackLight();
                    break;
                case "开启屏幕":
                    //{"category":"屏幕控制","name":"开启屏幕","focus":"cmd","rawText":"打开屏幕"}

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String SCREENSAVER_ACTION = "com.semisky.broadcast.SCREENSAVER";
    private static final String KEY_POWER_EVENT = "PowerEvent";
    private static final int ACTION_LONG_PRESS = 1;

    private void cliseBackLight() {
        Intent intent = new Intent(SCREENSAVER_ACTION);
        intent.putExtra(KEY_POWER_EVENT, ACTION_LONG_PRESS);
        mContext.sendBroadcast(intent);
    }

    private void videoControl(String name) {
        try {
            Log.d(TAG, "videoControl: " + name);
            switch (name) {
                case "暂停":
                    pausePlay();
                    break;
                case "播放":
                    continuePlay();
                    break;
                case "单曲循环":
                    AidlManager.getInstance().getUsbMusicListener().changePlayOrder(SINGLE_PLAY);
                    break;
                case "随机播放":
                    AidlManager.getInstance().getUsbMusicListener().changePlayOrder(RANDOM_PLAY);
                    break;
                case "循环播放":
                    AidlManager.getInstance().getUsbMusicListener().changePlayOrder(LOOP_PLAY);
                    break;
                case "下一首":
                    mediaNext();
                    break;
                case "上一首":
                    mediaLast();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carCommand(String name) {
        if ("即刻出发".equals(name)) {

        }

    }

    private void pausePlay() {
        try {
            int currentAudioType = AudioManager.getInstance().getCurrentAudioType();
            Log.d(TAG, "pausePlay: " + currentAudioType);
            switch (currentAudioType) {
                case AudioManager.STREAM_RADIO:
                    AidlManager.getInstance().getRadioListener().mute();
                    break;
                case AudioManager.STREAM_BT_MUSIC:
                    AidlManager.getInstance().getBTMusicListener().pause();
                    break;
                case AudioManager.STREAM_ANDROID:
                    AidlManager.getInstance().getUsbMusicListener().pause();
                    break;
                case AudioManager.CARLIFE:

                    break;
                case AudioManager.STREAM_NETMUSIC:
                    kwMusicAPI.pause();
                    break;
                case AudioManager.STREAM_NAVI:

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void continuePlay() {
        try {
            int currentAudioType = AudioManager.getInstance().getCurrentAudioType();
            Log.d(TAG, "continuePlay: " + currentAudioType);
            switch (currentAudioType) {
                case AudioManager.STREAM_RADIO:
                    AidlManager.getInstance().getRadioListener().Unmute();
                    break;
                case AudioManager.STREAM_BT_MUSIC:
                    AidlManager.getInstance().getBTMusicListener().play();
                    break;
                case AudioManager.STREAM_ANDROID:
                    AidlManager.getInstance().getUsbMusicListener().play();
                    break;
                case AudioManager.CARLIFE:

                    break;
                case AudioManager.STREAM_NETMUSIC:
                    kwMusicAPI.play();
                    break;
                case AudioManager.STREAM_NAVI:

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mediaNext() {
        try {
            int currentAudioType = AudioManager.getInstance().getCurrentAudioType();
            Log.d(TAG, "mediaNext: " + currentAudioType);
            switch (currentAudioType) {
                case AudioManager.STREAM_BT_MUSIC:
                    AidlManager.getInstance().getBTMusicListener().NextProgram();
                    break;
                case AudioManager.STREAM_ANDROID:
                    AidlManager.getInstance().getUsbMusicListener().NextProgram();
                    break;
                case AudioManager.STREAM_NETMUSIC:
                    kwMusicAPI.nextMusic();
                    break;
                case AudioManager.CARLIFE:

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mediaLast() {
        try {
            int currentAudioType = AudioManager.getInstance().getCurrentAudioType();
            Log.d(TAG, "mediaLast: " + currentAudioType);
            switch (currentAudioType) {
                case AudioManager.STREAM_BT_MUSIC:
                    AidlManager.getInstance().getBTMusicListener().lastProgram();
                    break;
                case AudioManager.STREAM_ANDROID:
                    AidlManager.getInstance().getUsbMusicListener().lastProgram();
                    break;
                case AudioManager.STREAM_NETMUSIC:
                    kwMusicAPI.lastMusic();
                    break;
                case AudioManager.CARLIFE:

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int LIGHT_MODE_AUTO = 0;
    private static final int LIGHT_MODE_DAY = 1;
    private static final int LIGHT_MODE_NIGHT = 2;

    //亮度模式KEY
    private static final String KEY_LIGHT_MODE = "light_mode";
    //白天亮度值KEY
    private static final String KEY_LIGHT_DAY_VALUE = "key_light_day_value";
    // 黑夜亮度值KEY
    private static final String KEY_LIGHT_NIGHT_VALUE = "key_light_night_value";

    private void updateLightAdjustValue(int type) {
        ContentResolver contentResolver = mContext.getContentResolver();
        int lightAdjustDayValue = Settings.System.getInt(contentResolver, KEY_LIGHT_DAY_VALUE, 7);
        int lightAdjustNightValue = Settings.System.getInt(contentResolver, KEY_LIGHT_NIGHT_VALUE, 3);
        int lightModeValue = Settings.System.getInt(contentResolver, KEY_LIGHT_MODE, LIGHT_MODE_AUTO);

        Log.d(TAG, "updateLightAdjustValue:lightAdjustDayValue " + lightAdjustDayValue);
        Log.d(TAG, "updateLightAdjustValue:lightAdjustNightValue " + lightAdjustNightValue);
        Log.d(TAG, "updateLightAdjustValue:lightModeValue " + lightModeValue);

        //增加亮度
        if (type == 0) {
            if (lightModeValue == LIGHT_MODE_DAY) {
                if (lightAdjustDayValue >= 10) return;
                int light = lightAdjustDayValue + 1;
                //设置亮度
                AutoManager.getInstance().setBackLight(LIGHT_MODE_DAY, light);
                Settings.System.putInt(contentResolver, KEY_LIGHT_DAY_VALUE, light);
                Log.d(TAG, "setBackLight: " + light);
            } else if (lightModeValue == LIGHT_MODE_NIGHT) {
                if (lightAdjustNightValue >= 10) return;
                int light = lightAdjustNightValue + 1;
                //设置亮度
                AutoManager.getInstance().setBackLight(LIGHT_MODE_NIGHT, light);
                Settings.System.putInt(contentResolver, KEY_LIGHT_NIGHT_VALUE, light);
                Log.d(TAG, "setBackLight: " + light);
            }
            //降低亮度
        } else if (type == 1) {
            if (lightModeValue == LIGHT_MODE_DAY) {
                if (lightAdjustDayValue <= 0) return;
                int light = lightAdjustDayValue - 1;
                //设置亮度
                AutoManager.getInstance().setBackLight(LIGHT_MODE_DAY, light);
                Settings.System.putInt(contentResolver, KEY_LIGHT_DAY_VALUE, light);
                Log.d(TAG, "setBackLight: " + light);
            } else if (lightModeValue == LIGHT_MODE_NIGHT) {
                if (lightAdjustNightValue <= 0) return;
                int light = lightAdjustNightValue - 1;
                //设置亮度
                AutoManager.getInstance().setBackLight(LIGHT_MODE_NIGHT, light);
                Settings.System.putInt(contentResolver, KEY_LIGHT_NIGHT_VALUE, light);
                Log.d(TAG, "setBackLight: " + light);
            }
        }
    }

    private void changeLightMode() {
//        AutoManager.getInstance().setLightMode(CarCtrlManager.LIGHT_MODE_DAY);
        // TODO
    }

    private void skipHome() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        mContext.startActivity(homeIntent);
    }

    private void checkVoiceActivity() {
        boolean topActivityName = ToolUtils.getTopActivityName(mContext, PKG_VOICE);
        if (topActivityName) {
            VoiceWakeupScenes.closeVoiceActivity();
        }
    }

    private void openBTCallConnection(int type) {
        Intent intent = new Intent();
        intent.setClassName(PKG_BTCALL, CLS_BTCALL);
        intent.putExtra("swhichfragmentid", type);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
