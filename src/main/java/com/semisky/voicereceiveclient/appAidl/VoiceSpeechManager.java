package com.semisky.voicereceiveclient.appAidl;

import android.os.RemoteException;

import com.semisky.voicereceiveclient.IVoiceSpeechListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhongrui on 2018/8/7
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class VoiceSpeechManager {

    private List<IVoiceSpeechListener> listenerData;

    private VoiceSpeechManager() {
        listenerData = new ArrayList<>();
    }

    public static VoiceSpeechManager getInstance() {
        return VoiceSpeechManagerHolder.INSTANCE;
    }

    private static class VoiceSpeechManagerHolder {
        private static VoiceSpeechManager INSTANCE = new VoiceSpeechManager();
    }

    void addVoiceSpeechListener(IVoiceSpeechListener listener) {
        if (!listenerData.contains(listener)) {
            listenerData.add(listener);
        }
    }

    void removeVoiceSpeechListener(IVoiceSpeechListener listener) {
        if (listenerData.contains(listener)) {
            listenerData.remove(listener);
        }
    }

    public void informStartSpeech() {
        if (listenerData != null && listenerData.size() > 0) {
            for (IVoiceSpeechListener listener : listenerData) {
                try {
                    listener.startSpeechRecord();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void informStopSpeech() {
        if (listenerData != null && listenerData.size() > 0) {
            for (IVoiceSpeechListener listener : listenerData) {
                try {
                    listener.stopSpeechRecord();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void informStartWake() {
        if (listenerData != null && listenerData.size() > 0) {
            for (IVoiceSpeechListener listener : listenerData) {
                try {
                    listener.startWakeRecord();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void informStopWake() {
        if (listenerData != null && listenerData.size() > 0) {
            for (IVoiceSpeechListener listener : listenerData) {
                try {
                    listener.stopWakeRecord();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
