package com.semisky.voicereceiveclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.jsonEntity.ResultCode;
import com.semisky.voicereceiveclient.service.BinderPoolService;


/**
 * 此为测试类，用于测试和各应用的连接通讯，以及显示语义
 * 服务端操作
 * 1.创建模块所需要的aidl并实现
 * 2.创建binder池aidl并实现查询方法
 * 3.创建并启动服务
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService(View view) {
        //未来放到系统启动
        startService(new Intent(this, BinderPoolService.class));
        Log.d(TAG, "startService: ");
    }

    public void RadioTest(View view) {
        try {
            IRadioListener type = AidlManager.getInstance().getRadioListener();
            if (type != null) {
                int i = type.openActivity();
                if (i == ResultCode.RESULT_SUCCESS) {
                    Log.d(TAG, "客户端收到指令打开收音机");
                }
            } else {
                Log.e(TAG, "未收到客服端注册的listener");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void CarControlTest(View view) {
        try {
            ICarControlListener carControlListener = AidlManager.getInstance().getCarControlListener();
            if (carControlListener != null) {
                int i = carControlListener.openCarWindow();
                if (i == ResultCode.RESULT_SUCCESS) {
                    Log.d(TAG, "客户端收到指令openCarWindow");
                }
            } else {
                Log.e(TAG, "未收到客服端注册的listener");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void SystemControlTest(View view) {
        try {
            ISystemControlListener systemListener = AidlManager.getInstance().getSystemListener();
            if (systemListener != null) {
                int i = systemListener.openActivity();
                if (i == ResultCode.RESULT_SUCCESS) {
                    Log.d(TAG, "客户端收到指令openActivity");
                }
            } else {
                Log.e(TAG, "未收到客服端注册的listener");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void BTCallTest(View view) {
        try {
            IBTCallListener btCallListener = AidlManager.getInstance().getBTCallListener();
            if (btCallListener != null) {
                int i = btCallListener.queryCallRecords();
                if (i == ResultCode.RESULT_SUCCESS) {
                    Log.d(TAG, "客户端收到指令queryCallRecords");
                }
            } else {
                Log.e(TAG, "未收到客服端注册的listener");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void BTMusicTest(View view) {
        try {
            IBTMusicListener btMusicListener = AidlManager.getInstance().getBTMusicListener();
            if (btMusicListener != null) {
                int i = btMusicListener.play();
                if (i == ResultCode.RESULT_SUCCESS) {
                    Log.d(TAG, "客户端收到指令play");
                }
            } else {
                Log.e(TAG, "未收到客服端注册的listener");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void USBMusicTest(View view) {
        try {
            IUSBMusicListener usbMusicListener = AidlManager.getInstance().getUsbMusicListener();
            if (usbMusicListener != null) {
                int i = usbMusicListener.play();
                if (i == ResultCode.RESULT_SUCCESS) {
                    Log.d(TAG, "客户端收到指令play");
                }
            } else {
                Log.e(TAG, "未收到客服端注册的listener");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}