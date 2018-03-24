package com.semisky.voicereceiveclient.appAidl;

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
        return radioListener;
    }

    public void setRadioListener(IRadioListener radioListener) {
        this.radioListener = radioListener;
    }

    public IUSBMusicListener getUsbMusicListener() {
        return usbMusicListener;
    }

    public void setUsbMusicListener(IUSBMusicListener usbMusicListener) {
        this.usbMusicListener = usbMusicListener;
    }

    public IBTMusicListener getBTMusicListener() {
        return BTMusicListener;
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
        return BTCallListener;
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
