package com.semisky.voicereceiveclient;

import com.semisky.voicereceiveclient.IRadioListener;

// Declare any non-default types here with import statements

interface IRadioPlay {

    //收音机注册语音监听
    void setOnVoiceListener(IRadioListener listener);
}
