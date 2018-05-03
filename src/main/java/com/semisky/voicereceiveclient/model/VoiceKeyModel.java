package com.semisky.voicereceiveclient.model;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.semisky.autoservice.aidl.IKeyListener;
import com.semisky.autoservice.manager.KeyManager;
import com.semisky.voicereceiveclient.utils.ToolUtils;

import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_VOICE;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_VOICE;

/**
 * Created by chenhongrui on 2017/8/23
 * <p>
 * 内容摘要：按键
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class VoiceKeyModel {

    private static final String TAG = "VoiceKeyModel";

    private static VoiceKeyModel INSTANCE;
    private IKeyListener iKeyListener;
    private Context mContext;

    public static VoiceKeyModel getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (VoiceKeyModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VoiceKeyModel(context);
                }
            }
        }
        return INSTANCE;
    }

    private VoiceKeyModel(Context context) {
        this.mContext = context;
        setOnKeyManagerListener();
    }

    /**
     * 按钮
     */
    private void setOnKeyManagerListener() {
        iKeyListener = new IKeyListener.Stub() {
            @Override
            public void onKey(int keyCode, int action) throws RemoteException {
                Log.d(TAG, "keyCode: " + keyCode + " action: " + action);
                switch (keyCode) {
                    case KeyManager.KEYCODE_VR:
                        if (action == 0) {
                            if (checkVoiceActivity()) return;
                            Intent intent = new Intent();
                            intent.setClassName(PKG_VOICE, CLS_VOICE);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }
                        break;
                }
            }
        };
    }

    public void registerOnKeyListener() {
        KeyManager.getInstance().setOnKeyListener(iKeyListener);
        setRegister(true);
    }

    public void unregisterOnKeyListener() {
        KeyManager.getInstance().unregisterOnKeyListener(iKeyListener);
        setRegister(false);
    }

    private boolean isRegister;

    public boolean isRegister() {
        return isRegister;
    }

    private void setRegister(boolean register) {
        isRegister = register;
    }

    /**
     * 如果当前处于语音识别页面，再点击退出页面
     */
    private boolean checkVoiceActivity() {
        boolean topActivityName = ToolUtils.getTopActivityName(mContext, PKG_VOICE);
        if (topActivityName) {
            VoiceWakeupScenes.closeVoiceActivity();
            return true;
        }
        return false;
    }
}
