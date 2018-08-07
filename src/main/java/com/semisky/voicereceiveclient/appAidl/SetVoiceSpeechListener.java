package com.semisky.voicereceiveclient.appAidl;

import android.os.RemoteException;

import com.semisky.voicereceiveclient.ISetVoiceSpeechListener;
import com.semisky.voicereceiveclient.IVoiceSpeechListener;

/**
 * Created by chenhongrui on 2018/8/7
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class SetVoiceSpeechListener extends ISetVoiceSpeechListener.Stub {
    @Override
    public void setOnVoiceSpeechListener(IVoiceSpeechListener listener) throws RemoteException {
        VoiceSpeechManager.getInstance().addVoiceSpeechListener(listener);
    }

    @Override
    public void unregisterVoiceSpeechListener(IVoiceSpeechListener listener) throws RemoteException {
        VoiceSpeechManager.getInstance().removeVoiceSpeechListener(listener);
    }
}
