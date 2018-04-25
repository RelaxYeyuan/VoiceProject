package com.semisky.voicereceiveclient;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.iflytek.platform.PlatformClientListener;
import com.iflytek.platform.type.PlatformCode;
import com.iflytek.platformservice.PlatformService;
import com.semisky.autoservice.manager.AutoConstants;
import com.semisky.autoservice.manager.AutoManager;
import com.semisky.voicereceiveclient.constant.AppConstant;
import com.semisky.voicereceiveclient.jsonEntity.AirControlEntity;
import com.semisky.voicereceiveclient.jsonEntity.AppEntity;
import com.semisky.voicereceiveclient.jsonEntity.CMDEntity;
import com.semisky.voicereceiveclient.jsonEntity.CallEntity;
import com.semisky.voicereceiveclient.jsonEntity.CarControlEntity;
import com.semisky.voicereceiveclient.jsonEntity.MusicEntity;
import com.semisky.voicereceiveclient.jsonEntity.RadioEntity;
import com.semisky.voicereceiveclient.manager.AirVoiceManager;
import com.semisky.voicereceiveclient.manager.AppVoiceManager;
import com.semisky.voicereceiveclient.manager.BTCallVoiceManager;
import com.semisky.voicereceiveclient.manager.CMDVoiceManager;
import com.semisky.voicereceiveclient.manager.CarVoiceManager;
import com.semisky.voicereceiveclient.manager.GPSManager;
import com.semisky.voicereceiveclient.manager.MusicVoiceManager;
import com.semisky.voicereceiveclient.manager.RadioVoiceManager;
import com.semisky.voicereceiveclient.model.VoiceBTModel;

import org.json.JSONException;
import org.json.JSONObject;

import static com.semisky.autoservice.manager.AudioManager.STREAM_VR;
import static com.semisky.voicereceiveclient.constant.AppConstant.ACTION_START_VOICE;
import static com.semisky.voicereceiveclient.constant.AppConstant.START_VOICE_FLAG;

/**
 * Created by chenhongrui on 2018/1/29
 * <p>
 * 内容摘要：
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class VoiceReceiveClient implements PlatformClientListener {

    private static final String TAG = "VoiceReceiveClient";

    private AudioManager audioManager;
    private Context mContext;
    private RadioVoiceManager radioVoiceManager;
    private AppVoiceManager appManager;
    private MusicVoiceManager musicVoiceManager;
    private CMDVoiceManager cmdVoiceManager;
    private AirVoiceManager airVoiceManager;
    private CarVoiceManager carVoiceManager;
    private BTCallVoiceManager btCallVoiceManager;
    private GPSManager gpsManager;

    VoiceReceiveClient(Context context) {
        super();
        this.mContext = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        radioVoiceManager = new RadioVoiceManager();
        appManager = new AppVoiceManager();
        musicVoiceManager = new MusicVoiceManager(mContext);
        cmdVoiceManager = new CMDVoiceManager();
        airVoiceManager = new AirVoiceManager();
        carVoiceManager = new CarVoiceManager();
        btCallVoiceManager = new BTCallVoiceManager(mContext);
        gpsManager = GPSManager.getInstance(mContext);
    }

    /**
     * 语音助理语义通知,可以根据语义消息，操控相关的应用程序。
     * 语音助理通知操作其他应用程序：如打开空调，音乐播放周杰伦的歌，收音机的频道点播、FM点播、AM点播等。
     *
     * @param actionJson - 识别结果 ，为jsonobject格式
     * @return success|fail
     */
    @Override
    public String onNLPResult(String actionJson) {
//        mContext.sendBroadcast(new Intent("com.semisky.VOICE_PROXY_BINDER"));
        Gson gson = new Gson();
        Log.d(TAG, "onNLPResult: " + actionJson);
        JSONObject resultJson = new JSONObject();
        if (actionJson == null) {
            try {
                resultJson.put("status", "fail");
                resultJson.put("message", "抱歉，没有可处理的操作");
            } catch (JSONException e) {
                Log.e(TAG, "onNLPResult: ");
            }
            return resultJson.toString();
        } else {
            try {
                JSONObject action = new JSONObject(actionJson);
                if ("app".equals(action.getString("focus"))) {
                    AppEntity appEntity = gson.fromJson(actionJson, AppEntity.class);
                    int type = appManager.setActionJson(appEntity, mContext);
                    if (type == AppConstant.MUSIC_TYPE_SUCCESS) {
                        resultJson.put("status", "success");
                        return resultJson.toString();
                    } else if (type == AppConstant.MUSIC_TYPE_DISK_MISSING) {
                        resultJson.put("status", "fail");
                        resultJson.put("message", "U盘未连接，请先连接U盘");
                        return resultJson.toString();
                    } else if (type == AppConstant.MUSIC_TYPE_FAIL) {
                        resultJson.put("status", "fail");
                        resultJson.put("message", "抱歉，没有可处理的操作");
                        return resultJson.toString();
                    } else if (type == AppConstant.BT_TYPE_NOT_CONNECTED) {
                        resultJson.put("status", "fail");
                        resultJson.put("message", "蓝牙电话未连接，请连接后重试");
                        return resultJson.toString();
                    }
                } else if ("radio".equals(action.getString("focus"))) {
                    RadioEntity radioEntity = gson.fromJson(actionJson, RadioEntity.class);
                    if (radioVoiceManager.setActionJson(mContext, radioEntity)) {
                        resultJson.put("status", "success");
                        return resultJson.toString();
                    } else {
                        resultJson.put("status", "fail");
                        resultJson.put("message", "抱歉，没有可处理的操作");
                        return resultJson.toString();
                    }
                } else if ("music".equals(action.getString("focus"))) {
                    MusicEntity musicEntity = gson.fromJson(actionJson, MusicEntity.class);
                    int type = musicVoiceManager.setActionJson(musicEntity);
                    if (type == AppConstant.MUSIC_TYPE_SUCCESS) {
                        resultJson.put("status", "success");
                        return resultJson.toString();
                    } else if (type == AppConstant.MUSIC_TYPE_DISK_MISSING) {
                        resultJson.put("status", "fail");
                        resultJson.put("message", "U盘未连接，请先连接U盘");
                        return resultJson.toString();
                    } else if (type == AppConstant.MUSIC_TYPE_FAIL) {
                        resultJson.put("status", "fail");
                        resultJson.put("message", "抱歉，没有可处理的操作");
                        return resultJson.toString();
                    } else if (type == AppConstant.MUSIC_TYPE_NOT_CONNECTED) {
                        resultJson.put("status", "fail");
                        resultJson.put("message", "抱歉，网络未连接，没有找到相关歌曲");
                        return resultJson.toString();
                    } else if (type == AppConstant.BT_TYPE_NOT_CONNECTED) {
                        resultJson.put("status", "fail");
                        resultJson.put("message", "蓝牙电话未连接，请连接后重试");
                        return resultJson.toString();
                    }
                } else if ("cmd".equals(action.getString("focus"))) {
                    CMDEntity cmdEntity = gson.fromJson(actionJson, CMDEntity.class);
                    String category = cmdEntity.getCategory();
                    String name = cmdEntity.getName();
                    if ("汽车控制".equals(category) & "即刻出发".equals(name)) {
                        try {
                            resultJson.put("status", "fail");
                            resultJson.put("message", "正在为您启动发动机");
                        } catch (JSONException e) {
                            Log.e(TAG, "onNLPResult: " + e.getMessage());
                        }
                        return resultJson.toString();
                    }
                    cmdVoiceManager.setActionJson(cmdEntity, mContext);

                } else if ("airControl".equals(action.getString("focus"))) {
                    AirControlEntity airEntity = gson.fromJson(actionJson, AirControlEntity.class);
                    airVoiceManager.setActionJson(airEntity);
                } else if ("carControl".equals(action.getString("focus"))) {
                    CarControlEntity carEntity = gson.fromJson(actionJson, CarControlEntity.class);
                    carVoiceManager.setActionJson(carEntity);
                }
            } catch (JSONException e) {
                Log.e(TAG, "onNLPResult: ");
            }

            try {
                resultJson.put("status", "success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultJson.toString();
        }
    }


    /**
     * 调用系统平台，请求其执行某个动作，或者语音助理自身状态的通知。例如：拨打电话、发送短信、告知系统语音助理在录音等。
     *
     * @param actionJson 含义：
     *                   call:请求拨打电话 。 (param1此时保存的是电话号码)
     *                   sendsms：请求发送短信。(param1此时保存的是电话号码，param2此时保存的是短信内容)
     * @return 能否响应  success | fail
     * resultJson.put("status", "success");
     * <p>
     * resultJson.put("status", "fail");
     * resultJson.put("message", "抱歉，无法处理此操作");
     */
    @Override
    public String onDoAction(String actionJson) {
        Log.d(TAG, "onDoAction: " + actionJson);
        /* 伪代码，实际需要客户实现 */
        JSONObject resultJson = new JSONObject();
        if (actionJson == null) {
            try {
                resultJson.put("status", "fail");
                resultJson.put("message", "抱歉，没有可处理的操作");
            } catch (JSONException e) {
                Log.e(TAG, "onDoAction: ");
            }
            return resultJson.toString();
        } else {
            try {
                JSONObject action = new JSONObject(actionJson);

                if ("call".equals(action.getString("action"))) {
                    if (action.getString("param1") != null) {
                        Log.d(TAG, "call number = " + action.getString("param1"));
                        Gson gson = new Gson();
                        CallEntity callEntity = gson.fromJson(actionJson, CallEntity.class);
                        btCallVoiceManager.setActionJson(callEntity);
                    }
                    resultJson.put("status", "success");
                    return resultJson.toString();
                } else if ("startspeechrecord".equals(action
                        .getString("action"))) {
                    Log.i(TAG, "Action_StartSpeechRecord ");
                    changRecordMode(0);
                    resultJson.put("status", "success");
                    return resultJson.toString();
                } else if ("stopspeechrecord"
                        .equals(action.getString("action"))) {
                    Log.i(TAG, "Action_StopSpeechRecord ");
                    resultJson.put("status", "success");
                    return resultJson.toString();
                } else if ("startwakerecord".equals(action.getString("action"))) {
                    Log.i(TAG, "Action_StartWakeRecord ");
                    changRecordMode(1);
                    resultJson.put("status", "success");
                    return resultJson.toString();
                } else if ("stopwakerecord".equals(action.getString("action"))) {
                    Log.i(TAG, "Action_StopWakeRecord ");
                    resultJson.put("status", "success");
                    return resultJson.toString();
                }
            } catch (JSONException e) {
                Log.e(TAG, "Fail to do action:" + e.getMessage());
            }

        }
        try {
            resultJson.put("status", "fail");
            resultJson.put("message", "抱歉，无法处理此操作");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultJson.toString();
    }

    private void changRecordMode(int type) {
        if (type == 0) {//降噪模式
            AutoManager.getInstance().setVRvoice(AutoConstants.VrMode.VR_DEFAULT);
        } else {//唤醒模式
            AutoManager.getInstance().setVRvoice(AutoConstants.VrMode.VR_WARKUP);
        }
    }

    @Override
    public int onGetState(int arg0) {
        Log.d(TAG, "onGetState: ");
        // 实际状态 需要客户读取提供
        if (arg0 == PlatformCode.STATE_BLUETOOTH_PHONE) {
            // 返回蓝牙电话状态
            if (VoiceBTModel.getInstance().isConnectionState()) {
                return PlatformCode.STATE_OK;
            } else {
                Log.d(TAG, "onGetState: 蓝牙状态不可用");
                return PlatformCode.STATE_NO;
            }
        } else if (arg0 == PlatformCode.STATE_SENDSMS) {
            // 返回短信功能是否可用
            return PlatformCode.STATE_NO;
        } else {
            // 不存在此种状态请求
            return PlatformCode.FAILED;
        }
    }

    @Override
    public String onGetLocation() {
        Log.d(TAG, "onGetLocation: ");
        gpsManager.getLngAndLat();
        double latitude = gpsManager.getLatitude();
        double longitude = gpsManager.getLongitude();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("longitude", longitude);
            jsonObject.put("latitude", latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onGetLocation:latitude " + latitude);
        Log.d(TAG, "onGetLocation:longitude " + longitude);
        return jsonObject.toString();
    }

    @Override
    public int onRequestAudioFocus(int streamType, int nDuration) {
        Log.d(TAG, "onRequestAudioFocus: ");
        // 这里使用的 android AudioFocus的音频协调机制
        com.semisky.autoservice.manager.AudioManager.getInstance().openStreamVolume(STREAM_VR);
        sendOpenVoiceBroadcast();
        return requestFocus();
    }

    @Override
    public void onAbandonAudioFocus() {
        Log.d(TAG, "onAbandonAudioFocus: ");
        com.semisky.autoservice.manager.AudioManager.getInstance().closeStreamVolume(STREAM_VR);
        abandonFocus();
        sendCloseVoiceBroadcast();
    }

    private AudioManager.OnAudioFocusChangeListener mFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            Log.d(TAG, "AudioFocusChange: " + focusChange);

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS://永久失去焦点 -1
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://暂时失去焦点 -2
                    /*
                    1.正在播放收音机，车机连接上蓝牙
                    2.进入语音识别页面
                    3.手机拨打蓝牙电话，挂断之后。
                    4.不会走onAbandonAudioFocus回调方法，导致收音机没有声音
                     */
                    abandonFocus();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://duck机制 -3

                    break;
                case AudioManager.AUDIOFOCUS_GAIN://重获焦点 1

                    break;
            }

            if (PlatformService.platformCallback == null) {
                Log.e(TAG, "PlatformService.platformCallback == null");
            } else {
                try {
                    /*
                     * 1.收音机前台播放，导航后台模拟导航
                     2.唤醒语音，等待一会(当收到AudioFocusChange DUCK -3)
                     3.点击back键退出语音
                     4.收音机没有声音
                     */
                    if (focusChange != AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        PlatformService.platformCallback.audioFocusChange(focusChange);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "platformCallback audioFocusChange error:" + e.getMessage());
                }
            }
        }
    };

    @Override
    public void onServiceUnbind() {
        Log.d(TAG, "onServiceUnbind: ");
    }

    @Override
    public int changePhoneState(int code) {
        Log.d(TAG, "changePhoneState: " + code);
        return 0;
    }

    @Override
    public String onGetCarNumbersInfo() {
        Log.d(TAG, "onGetCarNumbersInfo: ");
        return "";
    }

    @Override
    public boolean onSearchPlayList(String actionJson) {
        Log.d(TAG, "onSearchPlayList: " + actionJson);
        return false;
    }

    @Override
    public String onGetContacts(String actionJson) {
        Log.d(TAG, "onGetContacts: " + actionJson);
        return "";
    }

    /**
     * 申请音频焦点
     *
     * @return true 申请成功
     */
    private int requestFocus() {
        Log.d(TAG, "requestFocus: ");
        return audioManager.requestAudioFocus(mFocusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    /**
     * 释放音频焦点
     *
     * @return true 释放成功
     */
    private boolean abandonFocus() {
        Log.d(TAG, "abandonFocus: ");
        return mFocusChangeListener != null && AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(mFocusChangeListener);
    }

    private void sendOpenVoiceBroadcast() {
        Log.d(TAG, "sendOpenVoiceBroadcast: 语音进入广播");
        Intent intent = new Intent(ACTION_START_VOICE);
        intent.putExtra(START_VOICE_FLAG, true);
        mContext.sendBroadcast(intent);
    }

    private void sendCloseVoiceBroadcast() {
        Log.d(TAG, "sendOpenVoiceBroadcast: 语音退出广播");
        Intent intent = new Intent(ACTION_START_VOICE);
        intent.putExtra(START_VOICE_FLAG, false);
        mContext.sendBroadcast(intent);
    }
}
