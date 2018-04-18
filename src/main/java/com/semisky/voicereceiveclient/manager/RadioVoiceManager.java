package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.jsonEntity.RadioEntity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.semisky.voicereceiveclient.constant.AppConstant.AM_TYPE;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_RADIO;
import static com.semisky.voicereceiveclient.constant.AppConstant.FM_TYPE;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_RADIO;

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
    private Context mContext;

    public RadioVoiceManager() {
    }

    public boolean setActionJson(Context context, RadioEntity radioEntity) {
        mContext = context;
        String code = radioEntity.getCode();
        String waveband = radioEntity.getWaveband();
        String category = radioEntity.getCategory();

        //{"code":"555","waveband":"am","focus":"radio","rawText":"打开AM五五五"}
        //{"waveband":"fm","focus":"radio","rawText":"打开fm"}
        //{"code":"104.3","focus":"radio","rawText":"幺零四点三"}
        //{"category":"收藏","focus":"radio","rawText":"我想听我收藏的电台。"}
        //{"category":"收藏","focus":"radio","rawText":"我想听收藏的电台"}
        try {
            if (waveband == null && code != null) {
                Log.d(TAG, "setActionJson:freq " + ((Number) (Float.parseFloat(code) * 100)).intValue());
                AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
                startActivity(PKG_RADIO, CLS_RADIO);
                return true;
            } else if (category != null && category.equals("收藏")) {
                Log.d(TAG, "听收藏电台: ");
                AidlManager.getInstance().getRadioListener().radioPlayCollect();
                startActivity(PKG_RADIO, CLS_RADIO);
                return true;
            } else if (waveband != null) {
                Log.d(TAG, "setActionJson:waveband " + waveband);
                switch (waveband) {
                    case "fm":
                        AidlManager.getInstance().getRadioListener().changeSwitch(FM_TYPE);
                        if (code != null) {
                            AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
                        }
                        startActivity(PKG_RADIO, CLS_RADIO);
                        return true;
                    case "am":
                        AidlManager.getInstance().getRadioListener().changeSwitch(AM_TYPE);
                        if (code != null) {
                            AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
                        }
                        startActivity(PKG_RADIO, CLS_RADIO);
                        return true;
                    default:
                        return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void startActivity(String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
