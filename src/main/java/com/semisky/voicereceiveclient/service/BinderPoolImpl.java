package com.semisky.voicereceiveclient.service;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.semisky.voicereceiveclient.IBinderPool;
import com.semisky.voicereceiveclient.appAidl.SetBTCallListener;
import com.semisky.voicereceiveclient.appAidl.SetBTMusicListener;
import com.semisky.voicereceiveclient.appAidl.SetCarControlListener;
import com.semisky.voicereceiveclient.appAidl.SetRadioListener;
import com.semisky.voicereceiveclient.appAidl.SetSystemControlListener;
import com.semisky.voicereceiveclient.appAidl.SetUSBMusicListener;

import static com.semisky.voicereceiveclient.constant.AppConstant.BT_CALL_BINDER;
import static com.semisky.voicereceiveclient.constant.AppConstant.BT_CAR_CONTROL_BINDER;
import static com.semisky.voicereceiveclient.constant.AppConstant.BT_MUSIC_BINDER;
import static com.semisky.voicereceiveclient.constant.AppConstant.RADIO_BINDER;
import static com.semisky.voicereceiveclient.constant.AppConstant.SYSTEM_CONTROL_BINDER;
import static com.semisky.voicereceiveclient.constant.AppConstant.USB_MUSIC_BINDER;


/**
 * Created by chenhongrui on 2018/2/8
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BinderPoolImpl extends IBinderPool.Stub {

    private static final String TAG = "BinderPoolImpl";

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder = null;
        Log.d(TAG, "queryBinder: 客户端获取自己所需要的binder  " + binderCode);
        switch (binderCode) {
            case RADIO_BINDER:
                binder = new SetRadioListener();
                Log.d(TAG, "queryBinder: 返回给客户端收音机binder  " + binder);
                break;
            case USB_MUSIC_BINDER:
                binder = new SetUSBMusicListener();
                Log.d(TAG, "queryBinder: 返回给客户端本地音乐binder  " + binder);
                break;
            case BT_CALL_BINDER:
                binder = new SetBTCallListener();
                Log.d(TAG, "queryBinder: 返回给客户端蓝牙电话binder  " + binder);
                break;
            case BT_MUSIC_BINDER:
                binder = new SetBTMusicListener();
                Log.d(TAG, "queryBinder: 返回给客户端蓝牙音乐binder  " + binder);
                break;
            case BT_CAR_CONTROL_BINDER:
                binder = new SetCarControlListener();
                Log.d(TAG, "queryBinder: 返回给客户端车辆设置binder  " + binder);
                break;
            case SYSTEM_CONTROL_BINDER:
                binder = new SetSystemControlListener();
                Log.d(TAG, "queryBinder: 返回给客户端系统设置binder  " + binder);
                break;
            default:
                break;
        }
        return binder;
    }
}