package com.semisky.voicereceiveclient;

import com.semisky.voicereceiveclient.IBTCallListener;

// Declare any non-default types here with import statements

interface IBTCallPlay {

    //蓝牙电话注册语音监听
    void setOnVoiceListener(IBTCallListener listener);
}
