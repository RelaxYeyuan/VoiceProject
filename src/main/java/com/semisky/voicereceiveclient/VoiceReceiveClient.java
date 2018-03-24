package com.semisky.voicereceiveclient;

import android.content.Context;
import android.media.AudioManager;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.iflytek.platform.PlatformClientListener;
import com.iflytek.platform.type.PlatformCode;
import com.iflytek.platformservice.PlatformService;
import com.semisky.voicereceiveclient.jsonEntity.AirControlEntity;
import com.semisky.voicereceiveclient.jsonEntity.AppEntity;
import com.semisky.voicereceiveclient.jsonEntity.CMDEntity;
import com.semisky.voicereceiveclient.jsonEntity.CallEntity;
import com.semisky.voicereceiveclient.jsonEntity.CarControlEntity;
import com.semisky.voicereceiveclient.jsonEntity.MusicEntity;
import com.semisky.voicereceiveclient.manager.AirVoiceManager;
import com.semisky.voicereceiveclient.manager.AppVoiceManager;
import com.semisky.voicereceiveclient.manager.BTCallVoiceManager;
import com.semisky.voicereceiveclient.manager.CMDVoiceManager;
import com.semisky.voicereceiveclient.manager.CarVoiceManager;
import com.semisky.voicereceiveclient.manager.MusicVoiceManager;
import com.semisky.voicereceiveclient.manager.RadioVoiceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenhongrui on 2018/1/29
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class VoiceReceiveClient implements PlatformClientListener {

    private static final String TAG = "VoiceReceiveClient";

    private AudioManager audioManager = null;
    private Context mContext;
    private RadioVoiceManager radioVoiceManager;
    private AppVoiceManager appManager;
    private MusicVoiceManager musicVoiceManager;
    private CMDVoiceManager cmdVoiceManager;
    private AirVoiceManager airVoiceManager;
    private CarVoiceManager carVoiceManager;
    private BTCallVoiceManager btCallVoiceManager;

    VoiceReceiveClient(Context context) {
        super();
        this.mContext = context;
        radioVoiceManager = new RadioVoiceManager();
        appManager = new AppVoiceManager();
        musicVoiceManager = new MusicVoiceManager();
        cmdVoiceManager = new CMDVoiceManager();
        airVoiceManager = new AirVoiceManager();
        carVoiceManager = new CarVoiceManager();
        btCallVoiceManager = new BTCallVoiceManager();
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
                    appManager.setActionJson(appEntity);
                } else if ("radio".equals(action.getString("focus"))) {
                    radioVoiceManager.setActionJson(action);
                } else if ("music".equals(action.getString("focus"))) {
                    MusicEntity musicEntity = gson.fromJson(actionJson, MusicEntity.class);
                    musicVoiceManager.setActionJson(musicEntity);
                } else if ("cmd".equals(action.getString("focus"))) {
                    CMDEntity cmdEntity = gson.fromJson(actionJson, CMDEntity.class);
                    cmdVoiceManager.setActionJson(cmdEntity);
                } else if ("cmd".equals(action.getString("airControl"))) {
                    AirControlEntity airEntity = gson.fromJson(actionJson, AirControlEntity.class);
                    airVoiceManager.setActionJson(airEntity);
                } else if ("cmd".equals(action.getString("carControl"))) {
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
                        Log.d(TAG, "call number =" + action.getString("param1"));
                        Gson gson = new Gson();
                        CallEntity callEntity = gson.fromJson(actionJson, CallEntity.class);
                        btCallVoiceManager.setActionJson(callEntity);
                    }
                    resultJson.put("status", "success");
                    return resultJson.toString();
                } else if ("startspeechrecord".equals(action
                        .getString("action"))) {
                    Log.i(TAG, "Action_StartSpeechRecord ");
                    resultJson.put("status", "success");
                    return resultJson.toString();
                } else if ("stopspeechrecord"
                        .equals(action.getString("action"))) {
                    Log.i(TAG, "Action_StopSpeechRecord ");
                    resultJson.put("status", "success");
                    return resultJson.toString();
                } else if ("startwakerecord".equals(action.getString("action"))) {
                    Log.i(TAG, "Action_StartWakeRecord ");
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultJson.toString();
    }

    @Override
    public int onGetState(int arg0) {
        Log.d(TAG, "onGetState: ");
        // 实际状态 需要客户读取提供
        if (arg0 == PlatformCode.STATE_BLUETOOTH_PHONE) {
            // 返回蓝牙电话状态
            return PlatformCode.STATE_OK;
        } else if (arg0 == PlatformCode.STATE_SENDSMS) {
            // 返回短信功能是否可用
            return PlatformCode.STATE_OK;
        } else {
            // 不存在此种状态请求
            return PlatformCode.FAILED;
        }
    }

    @Override
    public String onGetLocation() {
        Log.d(TAG, "onGetLocation: ");
        return "";
    }

    @Override
    public int onRequestAudioFocus(int streamType, int nDuration) {
        Log.d(TAG, "onRequestAudioFocus: ");
        // 这里使用的 android AudioFocus的音频协调机制
        return audioManager.requestAudioFocus(afChangeListener,
                streamType, nDuration);
    }

    @Override
    public void onAbandonAudioFocus() {
        Log.d(TAG, "onAbandonAudioFocus: ");
        audioManager.abandonAudioFocus(afChangeListener);
    }

    private AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            AudioFocusChange(focusChange);
        }
    };

    /**
     * 客户主动回调的方法
     */

    private void AudioFocusChange(int focusChange) {
        Log.d(TAG, "AudioFocusChange: ");
        if (PlatformService.platformCallback == null) {
            Log.e(TAG, "PlatformService.platformCallback == null");
            return;
        }
        try {
            PlatformService.platformCallback.audioFocusChange(focusChange);
        } catch (RemoteException e) {
            Log.e(TAG,
                    "platformCallback audioFocusChange error:" + e.getMessage());
        }
    }

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
}
