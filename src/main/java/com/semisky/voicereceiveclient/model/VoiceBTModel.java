package com.semisky.voicereceiveclient.model;

import android.bluetooth.BluetoothAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhongrui
 * <p>
 * 内容摘要：蓝牙管理
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class VoiceBTModel {

    private static VoiceBTModel instance;
    private List<OnBtStateChangeListener> mBtStateListenerList = null;

    private VoiceBTModel() {
        mBtStateListenerList = new ArrayList<>();
    }

    public static VoiceBTModel getInstance() {
        if (instance == null) {
            synchronized (VoiceBTModel.class) {
                if (instance == null) {
                    instance = new VoiceBTModel();
                }
            }
        }
        return instance;
    }

    /**
     * 注册蓝牙状态监听
     */
    public void setOnBtStateChangeListener(OnBtStateChangeListener listener) {
        if (null != mBtStateListenerList && !mBtStateListenerList.contains(listener)) {
            mBtStateListenerList.add(listener);
        }
    }

    /**
     * 反注册蓝牙状态监听
     */
    public void unRegisterOnBtStateChangeListener(OnBtStateChangeListener listener) {
        if (null != mBtStateListenerList && mBtStateListenerList.contains(listener)) {
            mBtStateListenerList.remove(listener);
        }
    }

    /**
     * 通知观察者蓝牙连接状态改变
     */
    public void notifyObserversBtConnectionStateChanged(boolean isConnect) {
        if (null != mBtStateListenerList) {
            for (OnBtStateChangeListener l : mBtStateListenerList) {
                l.onConnectionStateChanged(isConnect);
            }
        }
    }

    /**
     * 通知观察者蓝牙打电话状态改变
     */
    public void notifyObserversBtCallStateChanged(boolean isCall) {
        if (null != mBtStateListenerList) {
            for (OnBtStateChangeListener l : mBtStateListenerList) {
                l.onCallStateChanged(isCall);
            }
        }
    }

    // 蓝牙是连接状态
    private int mConnectState = -1;

    public int getConnectState() {
        return mConnectState;
    }

    public void setConnectState(int mConnectState) {
        this.mConnectState = mConnectState;
    }

    public boolean isConnectionState() {
        return getConnectState() > 0;
    }

    public boolean isConnected() {
        return BluetoothAdapter.STATE_CONNECTED == getCallState();
    }

    // 蓝牙打电话状态 1 : 通话中 0 : 未通话中
    private int mCallState = -1;

    private int getCallState() {
        return mCallState;
    }

    public void setCallState(int mCallState) {
        this.mCallState = mCallState;

    }

    public boolean isCall() {
        // 0表示：未打开电话，1表示：打开电话
        return getCallState() > 0;
    }

    //栈顶是否是收音机页面
    //用于判断蓝牙电话挂断后收音机是否前台播放
    private boolean isRunningActivity;

    public boolean isRunningActivity() {
        return isRunningActivity;
    }

    public void setRunningActivity(boolean runningActivity) {
        isRunningActivity = runningActivity;
    }
}
