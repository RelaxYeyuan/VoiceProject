package com.semisky.voicereceiveclient.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.semisky.voicereceiveclient.model.VoiceBTModel;
import com.semisky.voicereceiveclient.model.VoiceKeyModel;
import com.semisky.voicereceiveclient.model.VoiceStatueModel;
import com.semisky.voicereceiveclient.model.VoiceWakeupScenes;

import static android.util.Log.d;

public class VoiceProxyReceiver extends BroadcastReceiver {

    private static final String TAG = "VoiceProxyReceiver";

    //蓝牙通话状态
    private static final String ACTION_CALL_STATE_CHANGED =
            "com.semisky.cx62.bluetooth.adapter.action.ACTION_CALL_STATE_CHANGED";
    private static final String EXTRA_CALL_ACTIVE =
            "com.semisky.cx62.bluetooth.adapter.extra.EXTRA_CALL_ACTIVE";

    //蓝牙开关状态
    private static final String ACTION_STATE_CHANGED =
            "com.semisky.cx62.bluetooth.adapter.action.STATE_CHANGED";
    private static final String EXTRA_STATE =
            "com.semisky.cx62.bluetooth.adapter.extra.STATE";

    //蓝牙连接状态
    private static final String ACTION_CONNECTION_STATE_CHANGED =
            "com.semisky.cx62.bluetooth.adapter.action.CONNECTION_STATE_CHANGED";
    private static final String EXTRA_CONNECTION_STATE =
            "com.semisky.cx62.bluetooth.adapter.extra.CONNECTION_STATE";

    private static final String ACTION_START_SCREEN = "com.semisky.broadcast.SCREEN_START_ACTIVITY";
    private static final String START_SCREEN_FLAG = "start_screen_flag";

    private static final String ACTION_START_BASEBOARD = "com.semisky.broadcast.BASEBOARD_START_ACTIVITY";
    private static final String START_BASEBOARD_FLAG = "start_baseboard_flag";

    private static final String ACTION_CARLIFE_VOICE = "com.semisky.broadcast.CARLIFE_VOICE";
    private static final String START_CARLIFE_FLAG = "start_carlife_voice_flag";

    private static final String ACTION_LOAD_DATA = "com.semisky.action.MEDIA_LOAD_STATE_CHANGE";
    private static final String START_LOAD_DATA_FLAG = "state";

    //进入倒车
    private static final String ACTION_START_OPEN_BACK_CAR = "com.semisky.IS_AVM";
    private static final String KEY_IS_AVM = "isAVM";

    //退出倒车
    private static final String ACTION_START_CLOSE_BACK_CAR = "com.semisky.IS_AD_CLOSE";
    private static final String KEY_AD_CLOSE = "isClose";

    // 音乐服务状态广播
    private static final String ACTION_MUSIC_SERVICE_STATE_CHANGE = "com.semisky.broadcast.ACTION_MUSIC_SERVICE_STATE_CHANGE";
    private static final String KEY_MUSIC_SERVICE_STATE = "state";// true ：表示音乐服务启动，false 表示音乐服务未启动。

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        d(TAG, "onReceive() action==>" + action);
        if (action == null) return;
        switch (action) {
            case ACTION_CALL_STATE_CHANGED:
                //true: 正在进行通话; false: 蓝牙通话结束
                if (!intent.hasExtra(EXTRA_CALL_ACTIVE)) return;
                boolean callState = intent.getBooleanExtra(EXTRA_CALL_ACTIVE, false);
                int iCallState = callState ? 1 : 0;
                d(TAG, "蓝牙电话:callState " + callState);
                VoiceBTModel.getInstance().setCallState(iCallState);
                VoiceBTModel.getInstance().notifyObserversBtCallStateChanged(callState);
                if (callState) {
                    VoiceWakeupScenes.closeVoice(VoiceStatueModel.BT_CALL);
                    VoiceKeyModel.getInstance(context).unregisterOnKeyListener();
                } else {
                    VoiceWakeupScenes.wakeupVoice(VoiceStatueModel.BT_CALL);
                    if (!VoiceKeyModel.getInstance(context).isRegister()) {
                        VoiceKeyModel.getInstance(context).registerOnKeyListener();
                    }
                }
                break;
            case ACTION_CONNECTION_STATE_CHANGED:
                //连接状态---断开：BluetoothAdapter.STATE_DISCONNECTED
                //蓝牙开关状态---开启：BluetoothAdapter.STATE_ON
                int state = intent.getIntExtra(EXTRA_CONNECTION_STATE,
                        BluetoothAdapter.STATE_DISCONNECTED);
                d(TAG, "蓝牙连接状态" + state);
                VoiceBTModel.getInstance().setConnectState(state);
                if (state == BluetoothAdapter.STATE_CONNECTED) {
                    VoiceBTModel.getInstance().notifyObserversBtConnectionStateChanged(true);
                } else if (state == BluetoothAdapter.STATE_DISCONNECTED) {
                    VoiceBTModel.getInstance().notifyObserversBtConnectionStateChanged(false);
                }
                break;

            case ACTION_START_SCREEN:
                //1 = 进入 ; 0 = 退出 屏保模式下
                int screenStatus = intent.getIntExtra(START_SCREEN_FLAG, 1);
                Log.d(TAG, "ACTION_START_SCREEN: screenStatus " + screenStatus);
                if (screenStatus == 1) {
                    VoiceWakeupScenes.closeVoice(VoiceStatueModel.CLOCK_SCREEN);
                    VoiceKeyModel.getInstance(context).unregisterOnKeyListener();
                } else {
                    VoiceWakeupScenes.wakeupVoice(VoiceStatueModel.CLOCK_SCREEN);
                    if (!VoiceKeyModel.getInstance(context).isRegister()) {
                        VoiceKeyModel.getInstance(context).registerOnKeyListener();
                    }
                }
                break;

            case ACTION_START_BASEBOARD:
                //1 = 进入 ; 0 = 退出
                int baseboardStatus = intent.getIntExtra(START_BASEBOARD_FLAG, 1);
                Log.d(TAG, "ACTION_START_SCREEN: baseboardStatus " + baseboardStatus);
                if (baseboardStatus == 1) {
                    VoiceWakeupScenes.closeVoice(VoiceStatueModel.BACKLIGHT_SCREEN);
                    VoiceKeyModel.getInstance(context).unregisterOnKeyListener();
                } else {
                    VoiceWakeupScenes.wakeupVoice(VoiceStatueModel.BACKLIGHT_SCREEN);
                    if (!VoiceKeyModel.getInstance(context).isRegister()) {
                        VoiceKeyModel.getInstance(context).registerOnKeyListener();
                    }
                }
                break;

            case ACTION_CARLIFE_VOICE:
                boolean carlifeVoiceStatus = intent.getBooleanExtra(START_CARLIFE_FLAG, false);
                Log.d(TAG, "ACTION_CARLIFE_VOICE:carlifeVoiceStatus " + carlifeVoiceStatus);
                if (carlifeVoiceStatus) {
                    VoiceWakeupScenes.closeVoice(VoiceStatueModel.CARLIFE);
                    //bug 2321 2006
//                    VoiceKeyModel.getInstance(context).unregisterOnKeyListener();
                } else {
                    VoiceWakeupScenes.wakeupVoice(VoiceStatueModel.CARLIFE);
//                    if (!VoiceKeyModel.getInstance(context).isRegister()) {
//                        VoiceKeyModel.getInstance(context).registerOnKeyListener();
//                    }
                }
                break;

            case ACTION_LOAD_DATA:
                boolean loadDataStatus = intent.getBooleanExtra(START_LOAD_DATA_FLAG, false);
                Log.d(TAG, "onReceive: " + loadDataStatus);
                if (loadDataStatus) {
                    VoiceBTModel.getInstance().setLoadData(true);
                } else {
                    VoiceBTModel.getInstance().setLoadData(false);
                }
                break;

            case ACTION_START_OPEN_BACK_CAR:
                Log.d(TAG, "ACTION_START_OPEN_BACK_CAR: ");
                VoiceWakeupScenes.closeVoice(VoiceStatueModel.BACK_CAR);
                VoiceKeyModel.getInstance(context).unregisterOnKeyListener();
                break;

            case ACTION_START_CLOSE_BACK_CAR:
                Log.d(TAG, "ACTION_START_CLOSE_BACK_CAR: ");
                VoiceWakeupScenes.wakeupVoice(VoiceStatueModel.BACK_CAR);
                if (!VoiceKeyModel.getInstance(context).isRegister()) {
                    VoiceKeyModel.getInstance(context).registerOnKeyListener();
                }
                break;

            case ACTION_MUSIC_SERVICE_STATE_CHANGE:
                boolean isServiceStart = intent.getBooleanExtra(KEY_MUSIC_SERVICE_STATE, false);
                Log.d(TAG, "ACTION_MUSIC_SERVICE_STATE_CHANGE: " + isServiceStart);
                if (isServiceStart) {
                    VoiceBTModel.getInstance().setMediaService(true);
                } else {
                    VoiceBTModel.getInstance().setMediaService(false);
                }
                break;
        }
    }
}
