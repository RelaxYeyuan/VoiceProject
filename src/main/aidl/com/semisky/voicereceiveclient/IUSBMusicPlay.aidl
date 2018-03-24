package com.semisky.voicereceiveclient;

import com.semisky.voicereceiveclient.IUSBMusicListener;

// Declare any non-default types here with import statements

interface IUSBMusicPlay {

    //本地音乐注册语音监听
    void setOnVoiceListener(IUSBMusicListener listener);
}
