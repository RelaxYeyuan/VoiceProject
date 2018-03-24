package com.semisky.voicereceiveclient.appAidl;

import android.os.RemoteException;

import com.semisky.voicereceiveclient.ICarControlListener;
import com.semisky.voicereceiveclient.ICarControlPlay;

/**
 * Created by chenhongrui on 2018/3/19
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class SetCarControlListener extends ICarControlPlay.Stub {
    @Override
    public void setOnVoiceListener(ICarControlListener listener) throws RemoteException {
        AidlManager.getInstance().setCarControlListener(listener);
    }
}
