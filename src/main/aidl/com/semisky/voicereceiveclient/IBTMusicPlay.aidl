package com.semisky.voicereceiveclient;

import com.semisky.voicereceiveclient.IBTMusicListener;

// Declare any non-default types here with import statements

interface IBTMusicPlay {

    //本地音乐注册语音监听
    void setOnVoiceListener(IBTMusicListener listener);
}
