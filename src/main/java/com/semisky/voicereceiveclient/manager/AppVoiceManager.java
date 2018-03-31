package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.semisky.voicereceiveclient.jsonEntity.AppEntity;
import com.semisky.voicereceiveclient.model.RadioBTModel;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_BTCALL;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_NAVI;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_RADIO;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_SETTINGS;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_BTCALL;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_NAVI;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_RADIO;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_SETTINGS;

/**
 * Created by chenhongrui on 2018/3/8
 * <p>
 * 内容摘要：处理应用操作 focus = app
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class AppVoiceManager {

    private static final String TAG = "AppVoiceManager";

    private Context mContext;

    public AppVoiceManager() {
    }

    public void setActionJson(AppEntity actionJson, Context context) {
        this.mContext = context;
        String name = actionJson.getName();
        String operation = actionJson.getOperation();
        if (name != null) {
            switch (name) {
                case "收音机":
                    radioOperation(operation);
                    break;
                case "蓝牙":
                    BTCallOperation(operation);
                    break;
                case "蓝牙音乐":
                    BTCallOperation(operation);
                    break;
                case "通话记录":
                    BTCallOperation(operation);
                    break;
                case "设置":
                    setOperation(operation);
                    break;
                case "多媒体":

                    break;
                case "导航":
                    naviOperation(operation);
                    break;
            }
        }
    }

    private void naviOperation(String operation) {
        switch (operation) {
            case "EXIT":
//                        AidlManager.getInstance().getSystemListener().closeBTCallConnect();
                break;
            case "LAUNCH":
                openNaviConnection();
                break;
        }
    }

    private void radioOperation(String operation) {
        try {
            if (operation != null) {
                switch (operation) {
                    case "EXIT"://退出app
//                        AidlManager.getInstance().getRadioListener().closeActivity();
                        skipHome();
                        break;
                    case "LAUNCH"://进入app
//                        AidlManager.getInstance().getRadioListener().openActivity();
                        Intent intent = new Intent();
                        intent.setClassName(PKG_RADIO, CLS_RADIO);
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BTCallOperation(String operation) {
        try {
            if (operation != null) {
                switch (operation) {
                    case "EXIT"://断开蓝牙连接
//                        AidlManager.getInstance().getSystemListener().closeBTCallConnect();
                        break;
                    case "LAUNCH"://打开蓝牙连接
//                        AidlManager.getInstance().getSystemListener().openBTCallConnect();
                        openBTCallConnection();
                        break;
                    case "QUERY"://查看通话记录
//                        AidlManager.getInstance().getBTCallListener().queryCallRecords();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOperation(String operation) {
        try {
            if (operation != null) {
                switch (operation) {
                    case "EXIT"://退出app
//                        AidlManager.getInstance().getSystemListener().closeActivity();
                        skipHome();
                        break;
                    case "LAUNCH"://进入app
//                        AidlManager.getInstance().getSystemListener().openActivity();
                        startSettingActivity();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSettingActivity() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(PKG_SETTINGS, CLS_SETTINGS);
        mContext.startActivity(intent);
    }

    private void openBTCallConnection() {
        if (RadioBTModel.getInstance().isConnectionState()) {
            Log.d(TAG, "openBTCallConnection: 打开蓝牙拨号");
            //拨号界面传6
            Intent intent = new Intent();
            intent.setClassName(PKG_BTCALL, CLS_BTCALL);
            intent.putExtra("swhichfragmentid", 6);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            Log.d(TAG, "openBTCallConnection: 打开蓝牙设置");
            //设置传3
            Intent intent = new Intent();
            intent.setClassName(PKG_BTCALL, CLS_BTCALL);
            intent.putExtra("swhichfragmentid", 3);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    private void openNaviConnection() {
        Intent intent = new Intent();
        intent.setClassName(PKG_NAVI, CLS_NAVI);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * finish的同时 intent launcher应用
     */
    private void skipHome() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        mContext.startActivity(homeIntent);
    }
}
