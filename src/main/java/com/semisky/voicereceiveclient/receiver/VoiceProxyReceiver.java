package com.semisky.voicereceiveclient.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.semisky.voicereceiveclient.model.VoiceBTModel;

import static android.util.Log.d;

public class VoiceProxyReceiver extends BroadcastReceiver {

    private static final String TAG = "VoiceProxyReceiver";

    private static final String BT_PHONE_CALLSTATE = "com.semisky.broadcast.BT_PHONE_CALLSTATE"; // 语音屏保广播
    private static final String BT_PHONE_FLAG = "call_state_flag";//这个拨打的是true,挂断给你发的false.

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

    //进入carlife语音
    private static final String ACTION_CARLIFE_VOICE = "com.semisky.broadcast.CARLIFE_VOICE";
    private static final String START_CARLIFE_FLAG = "start_carlife_voice_flag";

    //进入carlife
    private static final String ACTION_CARLIFE_VIEW = "com.semisky.broadcast.CARLIFE_VIEW";
    private static final String START_CARLIFE_VIEW_FLAG = "start_carlife_view_flag";

    private static final String ACTION_LOAD_DATA = "com.semisky.action.MEDIA_LOAD_STATE_CHANGE";
    private static final String START_LOAD_DATA_FLAG = "state";

    //进入倒车
    private static final String ACTION_START_BACK_CAR = "com.semisky.BACKMODE_CHANGED";
    private static final String KEY_IS_AVM = "BackMode";

    // 音乐服务状态广播
    private static final String ACTION_MUSIC_SERVICE_STATE_CHANGE = "com.semisky.broadcast.ACTION_MUSIC_SERVICE_STATE_CHANGE";
    private static final String KEY_MUSIC_SERVICE_STATE = "state";// true ：表示音乐服务启动，false 表示音乐服务未启动。]


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;
        d(TAG, "onReceive() action==>" + action);
        switch (action) {
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

            case ACTION_LOAD_DATA:
                boolean loadDataStatus = intent.getBooleanExtra(START_LOAD_DATA_FLAG, false);
                Log.d(TAG, "onReceive: " + loadDataStatus);
                if (loadDataStatus) {
                    VoiceBTModel.getInstance().setLoadData(true);
                } else {
                    VoiceBTModel.getInstance().setLoadData(false);
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
