package com.semisky.voicereceiveclient.appAidl;

import android.os.RemoteException;

import com.semisky.voicereceiveclient.IBTCallListener;
import com.semisky.voicereceiveclient.IBTCallPlay;

/**
 * Created by chenhongrui on 2018/3/19
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class SetBTCallListener extends IBTCallPlay.Stub {
    @Override
    public void setOnVoiceListener(IBTCallListener listener) throws RemoteException {
        AidlManager.getInstance().setBTCallListener(listener);
    }
}
