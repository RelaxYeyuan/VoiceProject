package com.semisky.voicereceiveclient.manager;

import android.util.Log;

import com.semisky.autoservice.manager.AudioManager;
import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.jsonEntity.CMDEntity;

import static com.semisky.voicereceiveclient.constant.AppConstant.ORDER_PLAY;
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

    public CMDVoiceManager() {
    }

    public void setActionJson(CMDEntity cmdEntity) {
        String category = cmdEntity.getCategory();
        String name = cmdEntity.getName();
        String nameValue = cmdEntity.getNameValue();

        try {
            if (name.equals("切换模式")) {
//            AidlManager.getInstance().getSystemListener().changeLightMode();
            } else if (name.equals("返回主菜单")) {
//                AidlManager.getInstance().getRadioListener().backLauncher();
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
                    videoControl(name, nameValue);
                    break;
                case "播放模式":
                    videoControl(name, nameValue);
                    break;
                case "汽车控制":
                    carCommand(name, nameValue);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
//                    AidlManager.getInstance().getSystemListener().raiseLight();
                    break;
                case "亮度-":
//                    AidlManager.getInstance().getSystemListener().lowerLight();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void videoControl(String name, String nameValue) {
        try {
            Log.d(TAG, "videoControl: " + name);
            switch (name) {
                case "暂停":
                    pausePlay();
                    break;
                case "播放":
                    continuePlay();
                    break;
                case "顺序循环":
                    AidlManager.getInstance().getUsbMusicListener().changePlayOrder(SINGLE_PLAY);
                    break;
                case "单曲循环":
                    AidlManager.getInstance().getUsbMusicListener().changePlayOrder(ORDER_PLAY);
                    break;
                case "随机播放":
                    AidlManager.getInstance().getUsbMusicListener().changePlayOrder(RANDOM_PLAY);
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

    private void carCommand(String name, String nameValue) {
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
                case AudioManager.CARLIFE:

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
