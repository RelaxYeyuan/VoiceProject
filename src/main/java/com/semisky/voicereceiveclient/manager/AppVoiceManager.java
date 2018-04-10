package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.semisky.autoservice.manager.AudioManager;
import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.constant.AppConstant;
import com.semisky.voicereceiveclient.jsonEntity.AppEntity;
import com.semisky.voicereceiveclient.model.KWMusicAPI;
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
    private KWMusicAPI kwMusicAPI;

    public AppVoiceManager() {
        kwMusicAPI = new KWMusicAPI();
    }

    public int setActionJson(AppEntity actionJson, Context context) {
        this.mContext = context;
        String name = actionJson.getName();
        String operation = actionJson.getOperation();
        Log.d(TAG, "setActionJson: name " + name);
        Log.d(TAG, "setActionJson: operation " + operation);

        if (name == null) {
            if (operation.equals("EXIT")) {
                skipHome();
                return AppConstant.MUSIC_TYPE_SUCCESS;
            }
            return AppConstant.MUSIC_TYPE_SUCCESS;
        }

        switch (name) {
            case "收音机":
                return radioOperation(operation);
            case "蓝牙":
                return btCallOperation(operation);
            case "蓝牙音乐":
                return btCallOperation(operation);
            case "通话记录":
                return btCallOperation(operation);
            case "通讯录":
                return openAddressBook(operation);
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
            case "网络音乐"://{"name":"网络音乐","operation":"LAUNCH","focus":"app","rawText":"打开网络音乐"}
                return netMusicOperation(operation);
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private int netMusicOperation(String operation) {
        switch (operation) {
            case "EXIT":

                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                kwMusicAPI.startApp();
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private int mediaOperation(String name, String operation) {
        switch (operation) {
            case "EXIT":
                //{"name":"音乐","operation":"EXIT","focus":"app","rawText":"关闭音乐。"}
                exitApp();
                skipHome();
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                return openMediaOperation(name);
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private int openMediaOperation(String name) {
        if (!ToolUtils.isSdOrUsbMounted(mContext, "/storage/udisk")) {
            return AppConstant.MUSIC_TYPE_DISK_MISSING;
        }
        switch (name) {
            case "视频":
                startActivity(PKG_MEDIA, CLS_MEDIA_VIDEO);
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "音乐":
                startActivity(PKG_MEDIA, CLS_MEDIA_MUSIC);
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "图片":
                startActivity(PKG_MEDIA, CLS_MEDIA_PICTURE);
                return AppConstant.MUSIC_TYPE_SUCCESS;
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private int airOperation(String operation) {
        switch (operation) {
            case "OPEN":

                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                openAirConnection();
                return AppConstant.MUSIC_TYPE_SUCCESS;

            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private int naviOperation(String operation) {
        switch (operation) {
            case "EXIT":
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                startActivity(PKG_NAVI, CLS_NAVI);
                return AppConstant.MUSIC_TYPE_SUCCESS;
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private int radioOperation(String operation) {
        switch (operation) {
            //退出app
            case "EXIT":
                exitApp();
                skipHome();
                return AppConstant.MUSIC_TYPE_SUCCESS;
            //进入app
            case "LAUNCH":
                startActivity(PKG_RADIO, CLS_RADIO);
                return AppConstant.MUSIC_TYPE_SUCCESS;
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private int btCallOperation(String operation) {
        try {
            switch (operation) {
                //断开蓝牙连接
                case "EXIT":
                    AidlManager.getInstance().getBTCallListener().cutBTCallConnect();
                    return AppConstant.MUSIC_TYPE_SUCCESS;
                //打开蓝牙连接
                case "LAUNCH":
                    linkBTConnection();
                    return AppConstant.MUSIC_TYPE_SUCCESS;
                //查看通话记录 5
                case "QUERY":
                    openBTCallConnection(5);
                    return AppConstant.MUSIC_TYPE_SUCCESS;
                default:
                    return AppConstant.MUSIC_TYPE_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppConstant.MUSIC_TYPE_FAIL;
    }

    private int openAddressBook(String operation) {
        try {
            switch (operation) {
                //打开通讯录 4
                case "LAUNCH":
                    openBTCallConnection(4);
                    return AppConstant.MUSIC_TYPE_SUCCESS;
                default:
                    return AppConstant.MUSIC_TYPE_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppConstant.MUSIC_TYPE_FAIL;
    }

    private int setOperation(String operation) {
        try {
            switch (operation) {
                //退出app
                case "EXIT":
                    skipHome();
                    return AppConstant.MUSIC_TYPE_SUCCESS;
                //进入app
                case "LAUNCH":
                    startActivity(PKG_SETTINGS, CLS_SETTINGS);
                    return AppConstant.MUSIC_TYPE_SUCCESS;

                default:
                    return AppConstant.MUSIC_TYPE_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppConstant.MUSIC_TYPE_FAIL;
    }

    private void openBTCallConnection() {
        if (RadioBTModel.getInstance().isConnectionState()) {
            Log.d(TAG, "openBTCallConnection: 打开蓝牙拨号");
            //拨号界面传6
            openBTCallConnection(6);
        } else {
            Log.d(TAG, "openBTCallConnection: 打开蓝牙设置");
            //设置传3
            openBTCallConnection(3);
        }
    }

    private void openBTCallConnection(int type) {
        Intent intent = new Intent();
        intent.setClassName(PKG_BTCALL, CLS_BTCALL);
        intent.putExtra("swhichfragmentid", type);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void linkBTConnection() {
        if (RadioBTModel.getInstance().isConnectionState()) {
            startActivity(PKG_BTCALL, CLS_BTCALL);
        } else {
            Intent intent = new Intent();
            intent.putExtra("LINK_BT_CONNECTION", true);
            intent.setClassName(PKG_BTCALL, CLS_BTCALL);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    private void cutBTConnection() {
        if (RadioBTModel.getInstance().isConnectionState()) {
            startActivity(PKG_BTCALL, CLS_BTCALL);
        } else {
            Intent intent = new Intent();
            intent.putExtra("CUT_BT_CONNECTION", false);
            intent.setClassName(PKG_BTCALL, CLS_BTCALL);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    private void openAirConnection() {

    }

    private void skipHome() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        mContext.startActivity(homeIntent);
    }

    private void startActivity(String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void exitApp() {
        try {
            int currentAudioType = AudioManager.getInstance().getCurrentAudioType();
            Log.d(TAG, "exitApp: " + currentAudioType);
            switch (currentAudioType) {
                case AudioManager.RADIO:
                    AidlManager.getInstance().getRadioListener().mute();
                    break;
                case AudioManager.STREAM_BT_MUSIC:
                    AidlManager.getInstance().getBTMusicListener().pause();
                    break;
                case AudioManager.STREAM_ANDROID:
                    AidlManager.getInstance().getUsbMusicListener().pause();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
