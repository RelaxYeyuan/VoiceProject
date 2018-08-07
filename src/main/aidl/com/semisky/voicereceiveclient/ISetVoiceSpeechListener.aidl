package com.semisky.voicereceiveclient;

import com.semisky.voicereceiveclient.IVoiceSpeechListener;

// Declare any non-default types here with import statements

interface ISetVoiceSpeechListener {

    //注册录音状态监听
    void setOnVoiceSpeechListener(IVoiceSpeechListener listener);

    //反注册录音状态监听
    void unregisterVoiceSpeechListener(IVoiceSpeechListener listener);

}
