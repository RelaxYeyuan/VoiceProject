package com.semisky.voicereceiveclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.semisky.voicereceiveclient.model.KWMusicAPI;
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

        Log.d(TAG, "onCreate: ");

        mKwapi = new KWMusicAPI();
        mKwapi.registerPlayerStatusListener();

        xmlyApi = new XMLYApi(this);
        xmlyApi.addPlayerStatusListener();
//
//        VoiceBTModel.getInstance().setOnBtStateChangeListener(new OnBtStateChangeListener() {
//            @Override
//            public void onConnectionStateChanged(boolean isConnect) {
//
//            }
//
//            @Override
//            public void onCallStateChanged(boolean isCall) {
//                if (isCall) {
//                    new Thread(runnable).start();
//                    Log.d(TAG, "onCallStateChanged: ");
//                }
//            }
//        });
//
//        Log.d(TAG, "setOnBtStateChangeListener: ");
    }

//    private Handler handler = new Handler();
//
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            boolean topActivityName = ToolUtils.getTopActivityName(BinderPoolService.this, PKG_BTCALL);
//            Log.d(TAG, "run: " + topActivityName);
//            if (!topActivityName) {
//                VoiceWakeupScenes.wakeupVoice();
//                handler.removeCallbacks(runnable);
//            } else {
//                handler.postDelayed(runnable, 3000);
//            }
//        }
//    };

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
