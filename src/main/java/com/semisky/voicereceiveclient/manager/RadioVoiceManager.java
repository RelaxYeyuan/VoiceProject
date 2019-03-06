package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.semisky.autoservice.manager.RadioManager;
import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.constant.AppConstant;
import com.semisky.voicereceiveclient.jsonEntity.RadioEntity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.semisky.voicereceiveclient.constant.AppConstant.AM_TYPE;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_RADIO;
import static com.semisky.voicereceiveclient.constant.AppConstant.FM_TYPE;
import static com.semisky.voicereceiveclient.constant.AppConstant.Numerical.FM_MAX_FREQ;
import static com.semisky.voicereceiveclient.constant.AppConstant.Numerical.FM_MAX_FREQ_INT;
import static com.semisky.voicereceiveclient.constant.AppConstant.Numerical.FM_MIN_FREQ;
import static com.semisky.voicereceiveclient.constant.AppConstant.Numerical.FM_MIN_FREQ_INT;
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

    public int setActionJson(Context context, RadioEntity radioEntity) {
        try {
            Log.d(TAG, "setActionJson: ");
            mContext = context;
            String code = radioEntity.getCode();
            String waveband = radioEntity.getWaveband();
            String category = radioEntity.getCategory();
            String rawText = radioEntity.getRawText();

            //{"code":"555","waveband":"am","focus":"radio","rawText":"打开AM五五五"}
            //{"waveband":"fm","focus":"radio","rawText":"打开fm"}
            //{"code":"104.3","focus":"radio","rawText":"幺零四点三"}
            //{"category":"收藏","focus":"radio","rawText":"我想听我收藏的电台。"}
            //{"category":"收藏","focus":"radio","rawText":"我想听收藏的电台"}
            //{"code":"87.5","waveband":"fm","focus":"radio","rawText":"调频八七点五"}
            //{"code":"87.5","waveband":"am","focus":"radio","rawText":"调幅87.5。"}
            //{"code":"102.9","waveband":"fm","focus":"radio","rawText":"调频幺零二九"}
            //{"code":"1629","waveband":"am","focus":"radio","rawText":"调幅1629。"}
            //{"focus":"radio","rawText":"关闭FM"}

            //{"focus":"radio","rawText":"播放收音机"}
            //{"focus":"radio","rawText":"听收音机"}
            //{"focus":"radio","rawText":"关闭FM"}
            //{"focus":"radio","rawText":"播放电台"}
            //{"focus":"radio","rawText":"听广播。"}
            //{"focus":"radio","rawText":"我要听电台。"}
            //{"focus":"radio","rawText":"我想听电台。"}
            //{"focus":"radio","rawText":"我要听无线广播。"}
            //{"focus":"radio","rawText":"我想听无线广播。"}

            if (rawText.contains("。")) {
                rawText = rawText.replace("。", "");
            }

            Log.d(TAG, "setActionJson: " + rawText);
            if (rawText.equals("播放收音机")
                    || rawText.equals("听收音机")
                    || rawText.equals("播放电台")
                    || rawText.equals("我要听电台")
                    || rawText.equals("我想听电台")
                    || rawText.equals("我想听广播")
                    || rawText.equals("我要听广播")
                    || rawText.equals("我要听无线广播")
                    || rawText.equals("我想听无线广播")
                    || rawText.equals("听广播")) {
                Log.d(TAG, "听收音机");
                startActivity(PKG_RADIO, CLS_RADIO);
                return AppConstant.RADIO_TYPE_SUCCESS;
            } else if (rawText.contains("关闭FM")) {
                Log.d(TAG, "关闭FM: ");
                return AppConstant.RADIO_TYPE_FAIL;
            }


            if (waveband != null) {
                switch (waveband) {
                    case "fm":
                        int fmFreq;
                        if (code != null) {
                            if (code.contains(".")) {
                                fmFreq = ((Number) (Float.parseFloat(code) * 100)).intValue();
                                Log.d(TAG, "setActionJson:fm " + fmFreq);
                            } else {
                                fmFreq = Integer.valueOf(code);
                            }
                            if (fmFreq <= FM_MAX_FREQ && fmFreq >= FM_MIN_FREQ) {
                                AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
                                startActivity(PKG_RADIO, CLS_RADIO);
                                Log.d(TAG, "setActionJson: 播放fm频点 " + fmFreq);
                                return AppConstant.RADIO_TYPE_SUCCESS;
                            } else if (fmFreq <= FM_MAX_FREQ_INT && fmFreq >= FM_MIN_FREQ_INT) {
                                AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
                                startActivity(PKG_RADIO, CLS_RADIO);
                                Log.d(TAG, "setActionJson: 播放fm频点 " + fmFreq);
                                return AppConstant.RADIO_TYPE_SUCCESS;
                            } else {
                                Log.d(TAG, "setActionJson: 超出fm频点范围 " + fmFreq);
                                return AppConstant.RADIO_TYPE_SCOPE;
                            }
                        } else {
                            AidlManager.getInstance().getRadioListener().changeSwitch(FM_TYPE);
                            startActivity(PKG_RADIO, CLS_RADIO);
                            Log.d(TAG, "setActionJson: 切换FM ");
                            return AppConstant.RADIO_TYPE_SUCCESS;
                        }
                    case "am":
                        int amFreq;
                        if (code != null) {
                            if (code.contains(".")) {
                                Log.d(TAG, "setActionJson: 超出am频点范围 " + code);
                                return AppConstant.RADIO_TYPE_SCOPE;
                            } else {
                                amFreq = Integer.valueOf(code);
                            }
                            Log.d(TAG, "setActionJson:am " + amFreq);
                            if (amFreq <= AppConstant.Numerical.AM_MAX_FREQ && amFreq >= AppConstant.Numerical.AM_MIN_FREQ
                                    && amFreq % 9 == 0) {
                                AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
                                startActivity(PKG_RADIO, CLS_RADIO);
                                Log.d(TAG, "setActionJson: 播放am频点 " + amFreq);
                                return AppConstant.RADIO_TYPE_SUCCESS;
                            } else {
                                Log.d(TAG, "setActionJson: 超出am频点范围 " + amFreq);
                                return AppConstant.RADIO_TYPE_SCOPE;
                            }
                        } else {
                            AidlManager.getInstance().getRadioListener().changeSwitch(AM_TYPE);
                            startActivity(PKG_RADIO, CLS_RADIO);
                            Log.d(TAG, "setActionJson: 切换AM");
                            return AppConstant.RADIO_TYPE_SUCCESS;
                        }
                }
            } else {
                //{"code":"90.8","focus":"radio","rawText":"打开九零点八"}
                if (code != null) {
                    int freq;
                    if (code.contains(".")) {//fm
                        freq = ((Number) (Float.parseFloat(code) * 100)).intValue();
                        Log.d(TAG, "setActionJson:fm " + freq);
                        if (freq <= FM_MAX_FREQ && freq >= FM_MIN_FREQ) {
                            AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
                            startActivity(PKG_RADIO, CLS_RADIO);
                            Log.d(TAG, "setActionJson: 播放fm频点 " + freq);
                            return AppConstant.RADIO_TYPE_SUCCESS;
                        } else {
                            Log.d(TAG, "setActionJson: 超出fm频点范围 " + freq);
                            return AppConstant.RADIO_TYPE_SCOPE;
                        }
                    } else {//am
                        freq = Integer.valueOf(code);
                        Log.d(TAG, "setActionJson:am " + freq);
                        if (freq <= AppConstant.Numerical.AM_MAX_FREQ && freq >= AppConstant.Numerical.AM_MIN_FREQ
                                && freq % 9 == 0) {
                            AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
                            startActivity(PKG_RADIO, CLS_RADIO);
                            Log.d(TAG, "setActionJson: 播放am频点 " + freq);
                            return AppConstant.RADIO_TYPE_SUCCESS;
                        } else if (freq <= FM_MAX_FREQ_INT && freq >= FM_MIN_FREQ_INT) {
                            AidlManager.getInstance().getRadioListener().radioPlayFreq(code);
                            startActivity(PKG_RADIO, CLS_RADIO);
                            Log.d(TAG, "setActionJson: 播放fm频点 " + freq);
                            return AppConstant.RADIO_TYPE_SUCCESS;
                        } else {
                            Log.d(TAG, "setActionJson: 超出am频点范围 " + freq);
                            return AppConstant.RADIO_TYPE_SCOPE;
                        }
                    }
                } else {
                    try {
                        if (category != null && category.equals("收藏")) {
                            Log.d(TAG, "听收藏电台: ");
                            AidlManager.getInstance().getRadioListener().radioPlayCollect();
                            startActivity(PKG_RADIO, CLS_RADIO);
                            return AppConstant.RADIO_TYPE_SUCCESS;
                        } else {
                            return AppConstant.RADIO_TYPE_FAIL;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return AppConstant.RADIO_TYPE_FAIL;
    }

    private void startActivity(String packageName, String className) {
        setUnMute();
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void setMute() {
        if (!RadioManager.getInstance().FmGetVolumeMute()) {
            Log.d(TAG, "setMute: 静音");
            RadioManager.getInstance().FmVolumeMute(1);
        }
    }

    private void setUnMute() {
        if (RadioManager.getInstance().FmGetVolumeMute()) {
            Log.d(TAG, "setUnMute: 解除静音");
            RadioManager.getInstance().FmVolumeMute(0);
        }
    }
}
