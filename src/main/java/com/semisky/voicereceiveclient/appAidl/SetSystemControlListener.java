package com.semisky.voicereceiveclient.appAidl;

import android.os.RemoteException;

import com.semisky.voicereceiveclient.ISystemControlListener;
import com.semisky.voicereceiveclient.ISystemControlPlay;

/**
 * Created by chenhongrui on 2018/3/19
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class SetSystemControlListener extends ISystemControlPlay.Stub{
    @Override
    public void setOnVoiceListener(ISystemControlListener listener) throws RemoteException {
        AidlManager.getInstance().setSystemListener(listener);
    }
}
