package com.semisky.voicereceiveclient.appAidl;

import android.os.RemoteException;
import android.util.Log;

import com.semisky.voicereceiveclient.IRadioListener;
import com.semisky.voicereceiveclient.IRadioPlay;


/**
 * Created by chenhongrui on 2018/1/6
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class SetRadioListener extends IRadioPlay.Stub {

    private static final String TAG = "SetRadioListener";

    @Override
    public void setOnVoiceListener(IRadioListener listener) throws RemoteException {
        AidlManager.getInstance().setRadioListener(listener);
        Log.d(TAG, "setOnVoiceListener: " + listener);
    }
}
