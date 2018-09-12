package com.semisky.voicereceiveclient.appAidl;

import android.os.RemoteException;

import com.semisky.voicereceiveclient.IVoiceChannelControl;
import com.semisky.voicereceiveclient.model.VoiceWakeupScenes;

/**
 * Created by chenhongrui on 2018/7/30
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class VoiceChannelControl extends IVoiceChannelControl.Stub {
    @Override
    public boolean closeVoiceChannel(int controlProject) throws RemoteException {
        return VoiceWakeupScenes.closeVoice(controlProject);
    }

    @Override
    public boolean openVoiceChannel(int controlProject) throws RemoteException {
        return VoiceWakeupScenes.wakeupVoice(controlProject);
    }

    @Override
    public void closeVoiceActivity() throws RemoteException {
        VoiceWakeupScenes.closeVoiceActivity();
    }
}
