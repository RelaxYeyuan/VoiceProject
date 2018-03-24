package com.semisky.voicereceiveclient;

// Declare any non-default types here with import statements

interface IPodListener {

   //暂停
    int pause();

    //播放
    int play();

    //上一个节目
    int lastProgram();

    //下一个节目
    int NextProgram();

}