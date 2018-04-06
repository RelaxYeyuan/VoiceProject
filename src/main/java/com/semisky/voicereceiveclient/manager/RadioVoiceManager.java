package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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

        try {
            if (code == null) {
                Log.d(TAG, "setActionJson: " + waveband);
                switch (waveband) {
                    case "fm":
                        AidlManager.getInstance().getRadioListener().changeSwitch(FM_TYPE);
                        startActivity(PKG_RADIO, CLS_RADIO);
                        return true;
                    case "am":
                        AidlManager.getInstance().getRadioListener().changeSwitch(AM_TYPE);
                        startActivity(PKG_RADIO, CLS_RADIO);
                        return true;
                    default:
                        return false;
                }
            } else {
                Log.d(TAG, "setActionJson: " + ((Number) (Float.parseFloat(code) * 100)).intValue());
                AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void startActivity(@NonNull String packageName, @NonNull String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
