package com.semisky.voicereceiveclient.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.semisky.voicereceiveclient.model.RadioBTModel;
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

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        d(TAG, "onReceive() action==>" + action);
        if (action == null) return;
        switch (action) {
            case ACTION_CALL_STATE_CHANGED:
                //true: 正在进行通话; false: 蓝牙通话结束
                boolean callState = intent.getBooleanExtra(EXTRA_CALL_ACTIVE, false);
                int iCallState = callState ? 1 : 0;
                d(TAG, "蓝牙电话:callState " + callState);
                RadioBTModel.getInstance().setCallState(iCallState);
                RadioBTModel.getInstance().notifyObserversBtCallStateChanged(callState);
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
                RadioBTModel.getInstance().setConnectState(state);
                if (state == BluetoothAdapter.STATE_CONNECTED) {
                    RadioBTModel.getInstance().notifyObserversBtConnectionStateChanged(true);
                } else if (state == BluetoothAdapter.STATE_DISCONNECTED) {
                    RadioBTModel.getInstance().notifyObserversBtConnectionStateChanged(false);
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
        }
    }
}
