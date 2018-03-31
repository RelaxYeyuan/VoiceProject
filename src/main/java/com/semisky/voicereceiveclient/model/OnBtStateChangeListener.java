package com.semisky.voicereceiveclient.model;

public interface OnBtStateChangeListener {
    /**
     * 蓝牙连接状态
     *
     * @param isConnect
     */
    void onConnectionStateChanged(boolean isConnect);

    /**
     * 蓝牙电话状态
     *
     * @param isCall 正在通话 true,挂断电话 false
     */
    void onCallStateChanged(boolean isCall);
}
