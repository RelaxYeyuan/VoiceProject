package com.semisky.voicereceiveclient;

import android.app.Application;
import android.util.Log;

import com.iflytek.platformservice.PlatformHelp;
import com.semisky.voicereceiveclient.model.RadioKeyModel;

import cn.kuwo.autosdk.api.KWAPI;

/**
 * Created by chenhongrui on 2018/1/29
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";

    public KWAPI mKwapi;

    private static BaseApplication mApp;

    public static synchronized BaseApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //给助理传递 实现 PlatformClientListener 接口的对象
        //Application启动的时候，必须给NavigationService.naviClient赋值
        VoiceReceiveClient testClient = new VoiceReceiveClient(this);
        PlatformHelp.getInstance().setPlatformClient(testClient);
        Log.d(TAG, "onCreate: setPlatformClient");

        //酷我实例
        mKwapi = KWAPI.createKWAPI(this, "auto");

        if (!RadioKeyModel.getInstance(this).isRegister()) {
            RadioKeyModel.getInstance(this).registerOnKeyListener();
        }
    }
}
