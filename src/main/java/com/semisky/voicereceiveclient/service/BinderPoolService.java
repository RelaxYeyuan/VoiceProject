package com.semisky.voicereceiveclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.semisky.voicereceiveclient.model.KWMusicAPI;

public class BinderPoolService extends Service {

    private static final String TAG = "BinderPoolService";

    private IBinder iBinder = new BinderPoolImpl();

    public BinderPoolService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        KWMusicAPI mKwapi = new KWMusicAPI();
        mKwapi.registerPlayerStatusListener();
    }

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
