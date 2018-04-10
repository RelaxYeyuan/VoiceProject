// ISetListener.aidl
package com.semisky.voicereceiveclient;

// Declare any non-default types here with import statements

interface ISystemControlListener {

    //打开设置应用
    int openActivity();

    //关闭设置应用
    int closeActivity();

    //切换模式 1=白天 2=黑夜 3=自动
    int changeLightMode(int type);

    //亮度+
    int lowerLight();

    //亮度-
    int raiseLight();
}
