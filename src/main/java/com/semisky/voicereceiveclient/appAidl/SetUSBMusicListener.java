package com.semisky.voicereceiveclient.appAidl;

import android.os.RemoteException;

import com.semisky.voicereceiveclient.IUSBMusicListener;
import com.semisky.voicereceiveclient.IUSBMusicPlay;
import com.semisky.voicereceiveclient.manager.MusicVoiceManager;

/**
 * Created by chenhongrui on 2018/3/8
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class SetUSBMusicListener extends IUSBMusicPlay.Stub {

    @Override
    public void setOnVoiceListener(IUSBMusicListener listener) throws RemoteException {
        AidlManager.getInstance().setUsbMusicListener(listener);
    }

    @Override
    public void resultCode(int code) throws RemoteException {
        MusicVoiceManager.getInstance().setResultCode(code);
    }
}
