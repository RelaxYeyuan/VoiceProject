package com.semisky.voicereceiveclient.manager;

import android.util.Log;

import com.semisky.voicereceiveclient.appAidl.AidlManager;

import org.json.JSONObject;

/**
 * Created by chenhongrui on 2018/3/8
 * <p>
 * 内容摘要：用于收音机相关操作 focus = radio
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class RadioVoiceManager {

    private static final String TAG = "RadioVoiceManager";

    public RadioVoiceManager() {
    }

    public void setActionJson(JSONObject actionJson) {
        String code;
        try {
            code = actionJson.getString("code");
            Log.d(TAG, "setActionJson: " + ((Number) (Float.parseFloat(code) * 100)).intValue());
            AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
