package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.semisky.voicereceiveclient.jsonEntity.AppEntity;
import com.semisky.voicereceiveclient.model.RadioBTModel;
import com.semisky.voicereceiveclient.utils.ToolUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_BTCALL;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_MEDIA_MUSIC;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_MEDIA_PICTURE;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_MEDIA_VIDEO;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_NAVI;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_RADIO;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_SETTINGS;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_BTCALL;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_MEDIA;
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

    public boolean setActionJson(AppEntity actionJson, Context context) {
        this.mContext = context;
        String name = actionJson.getName();
        String operation = actionJson.getOperation();
        Log.d(TAG, "setActionJson: name " + name);
        Log.d(TAG, "setActionJson: operation " + operation);
        switch (name) {
            case "收音机":
                return radioOperation(operation);
            case "蓝牙":
                return btCallOperation(operation);
            case "蓝牙音乐":
                return btCallOperation(operation);
            case "通话记录":
                return btCallOperation(operation);
            case "设置":
                return setOperation(operation);
            case "视频":
                return mediaOperation(name, operation);
            case "音乐":
                return mediaOperation(name, operation);
            case "图片":
                return mediaOperation(name, operation);
            case "导航":
                return naviOperation(operation);
            case "空调":
                return airOperation(operation);
            default:
                return false;
        }
    }

    private boolean mediaOperation(String name, String operation) {
        switch (operation) {
            case "EXIT":

                return true;
            case "LAUNCH":
                return openMediaOperation(name);
            default:
                return false;
        }
    }

    private boolean openMediaOperation(String name) {
        if (!ToolUtils.isSdOrUsbMounted(mContext, "/storage/udisk")) {
            return false;
        }
        switch (name) {
            case "视频":
                startActivity(PKG_MEDIA, CLS_MEDIA_VIDEO);
                return true;
            case "音乐":
                startActivity(PKG_MEDIA, CLS_MEDIA_MUSIC);
                return true;
            case "图片":
                startActivity(PKG_MEDIA, CLS_MEDIA_PICTURE);
                return true;
            default:
                return false;
        }
    }

    private boolean airOperation(String operation) {
        switch (operation) {
            case "OPEN":

                return true;
            case "LAUNCH":
                openAirConnection();
                return true;

            default:
                return false;
        }
    }

    private boolean naviOperation(String operation) {
        switch (operation) {
            case "EXIT":
                return true;
            case "LAUNCH":
                startActivity(PKG_NAVI, CLS_NAVI);
                return true;
            default:
                return false;
        }
    }

    private boolean radioOperation(String operation) {
        switch (operation) {
            //退出app
            case "EXIT":
                skipHome();
                return true;
            //进入app
            case "LAUNCH":
                startActivity(PKG_RADIO, CLS_RADIO);
                return true;
            default:
                return false;
        }
    }

    private boolean btCallOperation(String operation) {
        try {
            switch (operation) {
                //断开蓝牙连接
                case "EXIT":
//                        AidlManager.getInstance().getSystemListener().closeBTCallConnect();
                    return true;
                //打开蓝牙连接
                case "LAUNCH":
//                        AidlManager.getInstance().getSystemListener().openBTCallConnect();
                    openBTCallConnection();
                    return true;
                //查看通话记录
                case "QUERY":
//                        AidlManager.getInstance().getBTCallListener().queryCallRecords();
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean setOperation(String operation) {
        try {
            switch (operation) {
                //退出app
                case "EXIT":
                    skipHome();
                    return true;
                //进入app
                case "LAUNCH":
                    startActivity(PKG_SETTINGS, CLS_SETTINGS);
                    return true;

                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

    private void openAirConnection() {

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


    private void startActivity(@NonNull String packageName, @NonNull String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
