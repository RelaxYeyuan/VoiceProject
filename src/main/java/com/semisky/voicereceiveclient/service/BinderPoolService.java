package com.semisky.voicereceiveclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.semisky.voicereceiveclient.model.KWMusicAPI;
import com.semisky.voicereceiveclient.model.OnBtStateChangeListener;
import com.semisky.voicereceiveclient.model.VoiceBTModel;
import com.semisky.voicereceiveclient.model.VoiceWakeupScenes;
import com.semisky.voicereceiveclient.model.XMLYApi;
import com.semisky.voicereceiveclient.utils.ToolUtils;

import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_BTCALL;

public class BinderPoolService extends Service {

    private static final String TAG = "BinderPoolService";

    private IBinder iBinder = new BinderPoolImpl();

    public BinderPoolService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ");

        KWMusicAPI mKwapi = new KWMusicAPI();
        mKwapi.registerPlayerStatusListener();

        XMLYApi xmlyApi = new XMLYApi(this);
        xmlyApi.addPlayerStatusListener();

        VoiceBTModel.getInstance().setOnBtStateChangeListener(new OnBtStateChangeListener() {
            @Override
            public void onConnectionStateChanged(boolean isConnect) {

            }

            @Override
            public void onCallStateChanged(boolean isCall) {
//                if (isCall) {
//                    new Thread(runnable).start();
//                    Log.d(TAG, "onCallStateChanged: ");
//                }
            }
        });

//        Log.d(TAG, "setOnBtStateChangeListener: ");
    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            boolean topActivityName = ToolUtils.getTopActivityName(BinderPoolService.this, PKG_BTCALL);
            Log.d(TAG, "run: " + topActivityName);
            if (!topActivityName) {
                VoiceWakeupScenes.wakeupVoice();
                handler.removeCallbacks(runnable);
            } else {
                handler.postDelayed(runnable, 3000);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: 返回IBinder对象" + iBinder);
        return iBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
