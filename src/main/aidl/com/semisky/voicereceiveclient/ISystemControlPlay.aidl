package com.semisky.voicereceiveclient;

import com.semisky.voicereceiveclient.ISystemControlListener;

// Declare any non-default types here with import statements

interface ISystemControlPlay {

    //设置注册语音监听
    void setOnVoiceListener(ISystemControlListener listener);
}
