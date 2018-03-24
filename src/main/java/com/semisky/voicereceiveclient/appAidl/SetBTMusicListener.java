package com.semisky.voicereceiveclient.appAidl;

import android.os.RemoteException;

import com.semisky.voicereceiveclient.IBTMusicListener;
import com.semisky.voicereceiveclient.IBTMusicPlay;

/**
 * Created by chenhongrui on 2018/3/19
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class SetBTMusicListener extends IBTMusicPlay.Stub {
    @Override
    public void setOnVoiceListener(IBTMusicListener listener) throws RemoteException {
        AidlManager.getInstance().setBTMusicListener(listener);
    }
}
