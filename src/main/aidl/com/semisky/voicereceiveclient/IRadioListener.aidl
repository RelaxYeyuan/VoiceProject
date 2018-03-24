package com.semisky.voicereceiveclient;

// Declare any non-default types here with import statements

interface IRadioListener {

    //打开收音机应用
    int openActivity();

    //关闭收音机应用
    int closeActivity();

    //播放指定频率
    int radioPlayFreq(String freq);

    //改变播放波段fm/am
    int changeSwitch(int type);

    //收藏当前电台
    int collectFreq();

    //播放已收藏电台
    int radioPlayCollect();

    //上一个强信号
    int seekUp();

    //下一个强信号
    int seekDown();

    //搜索电台
    int seekFreq();

    //返回主页面
    int backLauncher();

}