package com.semisky.voicereceiveclient;

import com.semisky.voicereceiveclient.IPodListener;

// Declare any non-default types here with import statements

interface IPodPlay {

    //IPOD注册语音监听
    void setOnVoiceListener(IPodListener listener);
}
