package com.semisky.voicereceiveclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.semisky.autoservice.aidl.IShouldEnableIflytek;
import com.semisky.autoservice.manager.AutoManager;
import com.semisky.voicereceiveclient.manager.VoiceChannelManager;
import com.semisky.voicereceiveclient.model.KWMusicAPI;
import com.semisky.voicereceiveclient.model.VoiceKeyModel;
import com.semisky.voicereceiveclient.model.XMLYApi;

public class BinderPoolService extends Service {

    private static final String TAG = "BinderPoolService";

    private IBinder iBinder = new BinderPoolImpl();

    private XMLYApi xmlyApi;
    private KWMusicAPI mKwapi;

    public BinderPoolService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        VoiceKeyModel.getInstance(this).registerOnKeyListener();

        Log.d(TAG, "onCreate: ");

        mKwapi = new KWMusicAPI();
        mKwapi.registerPlayerStatusListener();

        xmlyApi = new XMLYApi(this);
        xmlyApi.addPlayerStatusListener();

        initCloseOrOpenVoice();
    }

    private void initCloseOrOpenVoice() {
        Log.d(TAG, "initCloseOrOpenVoice: ");
        final VoiceChannelManager instance = VoiceChannelManager.getInstance(BinderPoolService.this);

        AutoManager.getInstance().registerShouldEnableIflytek(new IShouldEnableIflytek.Stub() {
            @Override
            public void ShouldEnableIflytek(boolean status) {
                Log.d(TAG, "ShouldEnableIflytek: " + status);
                if (status) {
                    instance.sendMessageWakeup();
                } else {
                    instance.sendMessageCloseVoice();
                }
            }
        });

        //默认关闭录音通道 2019年03月07日14:36:11
        instance.sendMessageCloseVoice();
    }

//    private void initBTCallStatusListener() {
//        IBtCallStatusChangeListener listener = new IBtCallStatusChangeListener.Stub() {
//            @Override
//            public void onBtCallStatusChanged(int status) {
//                Log.d(TAG, "onBtCallStatusChanged: " + status);
//                switch (status) {
//                    case AutoConstants.BtIncallState.CALL_STATE_DIALING:
//                    case AutoConstants.BtIncallState.CALL_STATE_INCOMING:
//                        VoiceChannelManager.getInstance(BinderPoolService.this).sendMessageCloseVoice(VoiceStatueModel.BT_CALL);
//                        break;
//                    case AutoConstants.BtIncallState.CALL_STATE_TERMINATED:
//                        VoiceChannelManager.getInstance(BinderPoolService.this).sendMessageWakeup(VoiceStatueModel.BT_CALL);
//                        break;
//                }
//            }
//        };
//
//        Log.d(TAG, "initBTCallStatusListener: " + listener);
//        AutoManager.getInstance().registerBtCallStatusChangeListener(listener);
//
//        VoiceChannelManager.getInstance(BinderPoolService.this);
//    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: 返回IBinder对象" + iBinder);
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int type = intent.getIntExtra("FINISH_OTHER_APP", 1);
            Log.d(TAG, "onStartCommand: " + type);
            if (type == 1) {
                mKwapi.exitApp();
                xmlyApi.exitApp();
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
