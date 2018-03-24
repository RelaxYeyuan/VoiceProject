package com.semisky.voicereceiveclient;

import com.semisky.voicereceiveclient.ICarControlListener;

// Declare any non-default types here with import statements

interface ICarControlPlay {

    //本地音乐注册语音监听
    void setOnVoiceListener(ICarControlListener listener);
}
