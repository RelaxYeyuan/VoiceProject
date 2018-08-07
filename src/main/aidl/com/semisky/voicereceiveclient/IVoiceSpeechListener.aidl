package com.semisky.voicereceiveclient;

// Declare any non-default types here with import statements

interface IVoiceSpeechListener {

    //开始识别语音
    void startSpeechRecord();

    //结束识别语音
    void stopSpeechRecord();

    //开始唤醒语音
    void startWakeRecord();

    //结束唤醒语音
    void stopWakeRecord();

}