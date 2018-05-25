package com.semisky.voicereceiveclient.appAidl;

import android.util.Log;

import com.semisky.voicereceiveclient.IBTCallListener;
import com.semisky.voicereceiveclient.IBTMusicListener;
import com.semisky.voicereceiveclient.ICarControlListener;
import com.semisky.voicereceiveclient.IRadioListener;
import com.semisky.voicereceiveclient.ISystemControlListener;
import com.semisky.voicereceiveclient.IUSBMusicListener;

/**
 * Created by chenhongrui on 2018/1/10
 * <p>
 * 内容摘要：listener管理
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class AidlManager {

    private static final String TAG = "AidlManager";

    private static AidlManager mInstance;

    private IRadioListener radioListener;
    private IUSBMusicListener usbMusicListener;
    private IBTMusicListener BTMusicListener;
    private ISystemControlListener systemControlListener;
    private IBTCallListener BTCallListener;
    private ICarControlListener carControlListener;

    private AidlManager() {

    }

    public static AidlManager getInstance() {
        if (mInstance == null) {
            synchronized (AidlManager.class) {
                if (mInstance == null) {
                    mInstance = new AidlManager();
                }
            }
        }
        return mInstance;
    }

    public IRadioListener getRadioListener() {
        if (radioListener == null) {
            Log.e(TAG, "getUsbMusicListener: 收音机未连接语音代理服务！！！");
            return radioListener;
        } else {
            Log.d(TAG, "getRadioListener: " + radioListener);
            return radioListener;
        }
    }

    public void setRadioListener(IRadioListener radioListener) {
        this.radioListener = radioListener;
    }

    public IUSBMusicListener getUsbMusicListener() {
        if (usbMusicListener == null) {
            Log.e(TAG, "getUsbMusicListener: 本地音乐未连接语音代理服务！！！");
            return usbMusicListener;
        } else {
            Log.d(TAG, "getUsbMusicListener: " + usbMusicListener);
            return usbMusicListener;
        }
    }

    public void setUsbMusicListener(IUSBMusicListener usbMusicListener) {
        this.usbMusicListener = usbMusicListener;
    }

    public IBTMusicListener getBTMusicListener() {
        if (BTMusicListener == null) {
            Log.e(TAG, "getUsbMusicListener: 蓝牙音乐未连接语音代理服务！！！");
            return BTMusicListener;
        } else {
            Log.d(TAG, "getBTMusicListener: " + BTMusicListener);
            return BTMusicListener;
        }
    }

    public void setBTMusicListener(IBTMusicListener BTMusicListener) {
        this.BTMusicListener = BTMusicListener;
    }

    public ISystemControlListener getSystemListener() {
        return systemControlListener;
    }

    public void setSystemListener(ISystemControlListener mSetListener) {
        this.systemControlListener = mSetListener;
    }

    public IBTCallListener getBTCallListener() {
        if (BTCallListener == null) {
            Log.e(TAG, "getUsbMusicListener: 蓝牙电话未连接语音代理服务！！！");
            return BTCallListener;
        } else {
            Log.d(TAG, "getBTCallListener: " + BTCallListener);
            return BTCallListener;
        }
    }

    public void setBTCallListener(IBTCallListener BTCallListener) {
        this.BTCallListener = BTCallListener;
    }

    public ICarControlListener getCarControlListener() {
        return carControlListener;
    }

    public void setCarControlListener(ICarControlListener carControlListener) {
        this.carControlListener = carControlListener;
    }
}
