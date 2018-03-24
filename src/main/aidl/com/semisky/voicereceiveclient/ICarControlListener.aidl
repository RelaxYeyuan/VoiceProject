// ISetListener.aidl
package com.semisky.voicereceiveclient;

// Declare any non-default types here with import statements

interface ICarControlListener {

    //打开车窗
    int openCarWindow();

    //打开天窗
    int openSkyLight();

    //打开近光灯
    int openDippedHeadlight();
}
