package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.RemoteException;
import android.util.Log;

import com.semisky.autoservice.manager.ACManager;
import com.semisky.autoservice.manager.AudioManager;
import com.semisky.autoservice.manager.AutoConstants;
import com.semisky.autoservice.manager.AutoManager;
import com.semisky.voicereceiveclient.R;
import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.constant.AppConstant;
import com.semisky.voicereceiveclient.jsonEntity.AppEntity;
import com.semisky.voicereceiveclient.model.KWMusicAPI;
import com.semisky.voicereceiveclient.model.VoiceBTModel;
import com.semisky.voicereceiveclient.utils.ToolUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.semisky.autoservice.manager.ACManager.DEFROST_MODE_REAR;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_BTCALL;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_BTMUSIC;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_CAR_SETTINGS;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_HELP;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_MEDIA_MUSIC;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_MEDIA_PICTURE;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_MEDIA_VIDEO;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_NAVI;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_RADIO;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_SETTINGS;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_BTCALL;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_BTMUSIC;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_CAR_SETTINGS;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_HELP;
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
                return btMusicOperation(operation);
            case "蓝牙电话":
                return openBTCall(operation);
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
                return pictureOperation(name, operation);
            case "图片列表":
                return mediaOperation(name, operation);
            case "导航":
                return naviOperation(operation);
            case "空调":
                return airOperation(operation);
            case "网络音乐"://{"name":"网络音乐","operation":"LAUNCH","focus":"app","rawText":"打开网络音乐"}
                return netMusicOperation(operation);
            case "酷我音乐"://{"name":"酷我音乐","operation":"LAUNCH","focus":"app","rawText":"打开酷我音乐"}
                return netMusicOperation(operation);
            case "wifi":
                return wifiOperation(operation);
            case "在线电台":
                return radioOnlineOperation(operation);
            case "车辆设置":
                return carOperation(operation);
            case "帮助手册":
                return openHelperManual(operation);
            case "前除霜":
                //{"name":"前除霜","operation":"EXIT","focus":"app","rawText":"关闭前除霜"}
                //{"name":"后除霜","operation":"EXIT","focus":"app","rawText":"关闭后除霜"}
//                ACManager.getInstance().enableAirConditionerDefrost(DEFROST_MODE_FRONT, false);
                Log.d(TAG, "closeAirMode: 前除霜");
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "后除霜":
                ACManager.getInstance().enableAirConditionerDefrost(DEFROST_MODE_REAR, false);
                Log.d(TAG, "closeAirMode: 后除霜");
                return AppConstant.MUSIC_TYPE_SUCCESS;
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    ////{"name":"帮助手册","operation":"LAUNCH","focus":"app","rawText":"打开帮助手册"}
    private int openHelperManual(String operation) {
        switch (operation) {
            case "EXIT":
                Log.d(TAG, "openHelperManual: EXIT");
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                Log.d(TAG, "openHelperManual: LAUNCH");
                startActivity(PKG_HELP, CLS_HELP);
                return AppConstant.MUSIC_TYPE_SUCCESS;
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private static final String APP_VEH_SETTING = "com.semisky.autovehicalsetting.VehicalSetingActivity";

    private int carOperation(String operation) {
        switch (operation) {
            case "EXIT":
                Log.d(TAG, "carOperation: EXIT");

                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                Log.d(TAG, "carOperation: LAUNCH");
                AutoManager.getInstance().setAppStatus(APP_VEH_SETTING,
                        mContext.getString(R.string.car_setting_name), AutoConstants.AppStatus.RUN_FOREGROUND);
                startActivity(PKG_CAR_SETTINGS, CLS_CAR_SETTINGS);
                return AppConstant.MUSIC_TYPE_SUCCESS;
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    /**
     * {"name":"蓝牙音乐","operation":"LAUNCH","focus":"app","rawText":"打开蓝牙音乐"}
     */
    private int btMusicOperation(String operation) {
        switch (operation) {
            case "EXIT":
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                if (VoiceBTModel.getInstance().isConnectionState()) {
                    startActivity(PKG_BTMUSIC, CLS_BTMUSIC);
                } else {
                    return AppConstant.BT_TYPE_NOT_CONNECTED;
                }
                return AppConstant.MUSIC_TYPE_SUCCESS;

            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    /**
     * {"name":"在线电台","operation":"LAUNCH","focus":"app","rawText":"打开在线电台"}
     * {"name":"在线电台","operation":"EXIT","focus":"app","rawText":"关闭在线电台"}
     */
    private int radioOnlineOperation(String operation) {
        switch (operation) {
            case "EXIT":
                Log.d(TAG, "radioOnlineOperation: exit");

                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                Log.d(TAG, "radioOnlineOperation: launch");
                return AppConstant.MUSIC_TYPE_SUCCESS;
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    /**
     * {"name":"wifi","operation":"LAUNCH","focus":"app","rawText":"打开wifi"}
     * {"name":"wifi","operation":"EXIT","focus":"app","rawText":"关闭wifi"}
     */
    private int wifiOperation(String operation) {
        switch (operation) {
            case "EXIT":
                Log.d(TAG, "wifiOperation: exit");
                closeWifiFunction();
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                Log.d(TAG, "wifiOperation: launch");
                openWifiFunction();
                return AppConstant.MUSIC_TYPE_SUCCESS;
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private int netMusicOperation(String operation) {
        switch (operation) {
            case "EXIT":
                kwMusicAPI.exitApp();
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                kwMusicAPI.startApp();
                AutoManager.getInstance().setAppStatus(AutoConstants.PackageName.CLASS_KUWO,
                        mContext.getString(R.string.kw_music_name), AutoConstants.AppStatus.RUN_FOREGROUND);
                return AppConstant.MUSIC_TYPE_SUCCESS;
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private int mediaOperation(String name, String operation) {
        Log.d(TAG, "mediaOperation: " + name);
        if (!ToolUtils.isSdOrUsbMounted(mContext, "/storage/udisk")) {
            return AppConstant.MUSIC_TYPE_DISK_MISSING;
        }
        switch (operation) {
            case "EXIT":
                //{"name":"音乐","operation":"EXIT","focus":"app","rawText":"关闭音乐。"}
                muteApp();
                skipHome();
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                return openMediaOperation(name);
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private int pictureOperation(String name, String operation) {
        if (!ToolUtils.isSdOrUsbMounted(mContext, "/storage/udisk")) {
            return AppConstant.MUSIC_TYPE_DISK_MISSING;
        }
        switch (operation) {
            case "EXIT":
                //{"name":"图片","operation":"EXIT","focus":"app","rawText":"关闭图片"}
                skipHome();
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "LAUNCH":
                return openMediaOperation(name);
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private int openMediaOperation(String name) {
        switch (name) {
            case "视频":
                startActivity(PKG_MEDIA, CLS_MEDIA_VIDEO);
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "音乐":
                try {
                    AidlManager.getInstance().getUsbMusicListener().play();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                startActivity(PKG_MEDIA, CLS_MEDIA_MUSIC);
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "图片":
                startActivity(PKG_MEDIA, CLS_MEDIA_PICTURE);
                return AppConstant.MUSIC_TYPE_SUCCESS;
            case "图片列表":
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
                skipHome();
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
                muteApp();
                skipHome();
                return AppConstant.MUSIC_TYPE_SUCCESS;
            //进入app
            case "LAUNCH":
                startActivity(PKG_RADIO, CLS_RADIO);
                try {
                    AidlManager.getInstance().getRadioListener().Unmute();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return AppConstant.MUSIC_TYPE_SUCCESS;
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    //{"name":"蓝牙","operation":"EXIT","focus":"app","rawText":"关闭蓝牙"}
    private int btCallOperation(String operation) {
        try {
            switch (operation) {
                //断开蓝牙连接
                case "EXIT":
                    if (VoiceBTModel.getInstance().isConnectionState()) {
                        AidlManager.getInstance().getBTCallListener().cutBTCallConnect();
                        Log.d(TAG, "btCallOperation: cutBTCallConnect");
                        return AppConstant.MUSIC_TYPE_SUCCESS;
                    } else {
                        return AppConstant.BT_TYPE_NOT_CONNECTED;
                    }
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

    private int openBTCall(String operation) {
        switch (operation) {
            case "LAUNCH":
                openBTCallConnection();
                return AppConstant.MUSIC_TYPE_SUCCESS;
            default:
                return AppConstant.MUSIC_TYPE_FAIL;
        }
    }

    private static final String APP_SETTING = "com.semisky.autovehicalsetting.VehicalSetingActivity";

    private int setOperation(String operation) {
        try {
            switch (operation) {
                //退出app
                case "EXIT":
                    skipHome();
                    return AppConstant.MUSIC_TYPE_SUCCESS;
                //进入app
                case "LAUNCH":
                    AutoManager.getInstance().setAppStatus(APP_SETTING,
                            mContext.getString(R.string.setting_name), AutoConstants.AppStatus.RUN_FOREGROUND);
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
        if (VoiceBTModel.getInstance().isConnectionState()) {
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
        if (VoiceBTModel.getInstance().isConnectionState()) {
            startActivity(PKG_BTCALL, CLS_BTCALL);
        } else {
            Intent intent = new Intent();
            intent.putExtra("LINK_BT_CONNECTION", true);
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
        Log.d(TAG, "startActivity:packageName " + packageName);
        Log.d(TAG, "startActivity:className " + className);
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void muteApp() {
        try {
            int currentAudioType = AudioManager.getInstance().getCurrentAudioType();
            Log.d(TAG, "muteApp: " + currentAudioType);
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

    private void unMuteApp() {
        try {
            int currentAudioType = AudioManager.getInstance().getCurrentAudioType();
            Log.d(TAG, "unMuteApp: " + currentAudioType);
            switch (currentAudioType) {
                case AudioManager.RADIO:
                    AidlManager.getInstance().getRadioListener().Unmute();
                    break;
                case AudioManager.STREAM_BT_MUSIC:
                    AidlManager.getInstance().getBTMusicListener().play();
                    break;
                case AudioManager.STREAM_ANDROID:
                    AidlManager.getInstance().getUsbMusicListener().play();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WifiManager mWifiManager;

    private void openWifiFunction() {
        if (mWifiManager == null) {
            mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }

        if (mWifiManager != null && !mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }

        Intent intent = new Intent();
        intent.putExtra("from", 1);
        intent.setClassName(PKG_SETTINGS, CLS_SETTINGS);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void closeWifiFunction() {
        if (mWifiManager == null) {
            mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }

        if (mWifiManager != null && mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

}
