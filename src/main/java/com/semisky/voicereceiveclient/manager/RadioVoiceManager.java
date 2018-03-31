package com.semisky.voicereceiveclient.manager;

import android.util.Log;

import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.jsonEntity.RadioEntity;

import static com.semisky.voicereceiveclient.constant.AppConstant.AM_TYPE;
import static com.semisky.voicereceiveclient.constant.AppConstant.FM_TYPE;

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

    public void setActionJson(RadioEntity radioEntity) {
        String code = radioEntity.getCode();
        String waveband = radioEntity.getWaveband();

        try {
            if (code == null) {
                Log.d(TAG, "setActionJson: " + waveband);
                switch (waveband) {
                    case "fm":
                        AidlManager.getInstance().getRadioListener().changeSwitch(FM_TYPE);
                        break;
                    case "am":
                        AidlManager.getInstance().getRadioListener().changeSwitch(AM_TYPE);
                        break;
                }
            } else {
                Log.d(TAG, "setActionJson: " + ((Number) (Float.parseFloat(code) * 100)).intValue());
                AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
