package com.semisky.voicereceiveclient.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.semisky.voicereceiveclient.model.VoiceBTModel;
import com.semisky.voicereceiveclient.model.VoiceKeyModel;
import com.semisky.voicereceiveclient.model.VoiceWakeupScenes;

import static android.util.Log.d;

public class VoiceProxyReceiver extends BroadcastReceiver {

    private static final String TAG = "VoiceProxyReceiver";

    //蓝牙通话状态
    public static final String ACTION_CALL_STATE_CHANGED =
            "com.semisky.cx62.bluetooth.adapter.action.ACTION_CALL_STATE_CHANGED";
    public static final String EXTRA_CALL_ACTIVE =
            "com.semisky.cx62.bluetooth.adapter.extra.EXTRA_CALL_ACTIVE";

    //蓝牙开关状态
    public static final String ACTION_STATE_CHANGED =
            "com.semisky.cx62.bluetooth.adapter.action.STATE_CHANGED";
    public static final String EXTRA_STATE =
            "com.semisky.cx62.bluetooth.adapter.extra.STATE";

    //蓝牙连接状态
    public static final String ACTION_CONNECTION_STATE_CHANGED =
            "com.semisky.cx62.bluetooth.adapter.action.CONNECTION_STATE_CHANGED";
    public static final String EXTRA_CONNECTION_STATE =
            "com.semisky.cx62.bluetooth.adapter.extra.CONNECTION_STATE";

    public static final String ACTION_START_SCREEN = "com.semisky.broadcast.SCREEN_START_ACTIVITY";
    public static final String START_SCREEN_FLAG = "start_screen_flag";

    public static final String ACTION_START_BASEBOARD = "com.semisky.broadcast.BASEBOARD_START_ACTIVITY";
    public static final String START_BASEBOARD_FLAG = "start_baseboard_flag";

    public static final String ACTION_CARLIFE_VOICE = "com.semisky.broadcast.CARLIFE_VOICE";
    public static final String START_CARLIFE_FLAG = "start_carlife_voice_flag";


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
                    VoiceWakeupScenes.closeVoice();
                    VoiceKeyModel.getInstance(context).unregisterOnKeyListener();
                } else {
                    VoiceWakeupScenes.wakeupVoice();
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
                    VoiceWakeupScenes.closeVoice();
                    VoiceKeyModel.getInstance(context).unregisterOnKeyListener();
                } else {
                    VoiceWakeupScenes.wakeupVoice();
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
                    VoiceWakeupScenes.closeVoice();
                    VoiceKeyModel.getInstance(context).unregisterOnKeyListener();
                } else {
                    VoiceWakeupScenes.wakeupVoice();
                    if (!VoiceKeyModel.getInstance(context).isRegister()) {
                        VoiceKeyModel.getInstance(context).registerOnKeyListener();
                    }
                }
                break;

            case ACTION_CARLIFE_VOICE:
                boolean carlifeVoiceStatus = intent.getBooleanExtra(START_CARLIFE_FLAG, false);
                Log.d(TAG, "ACTION_CARLIFE_VOICE:carlifeVoiceStatus " + carlifeVoiceStatus);
                if (carlifeVoiceStatus) {
                    VoiceWakeupScenes.closeVoice();
                    VoiceKeyModel.getInstance(context).unregisterOnKeyListener();
                } else {
                    VoiceWakeupScenes.wakeupVoice();
                    if (!VoiceKeyModel.getInstance(context).isRegister()) {
                        VoiceKeyModel.getInstance(context).registerOnKeyListener();
                    }
                }
                break;
        }
    }
}
