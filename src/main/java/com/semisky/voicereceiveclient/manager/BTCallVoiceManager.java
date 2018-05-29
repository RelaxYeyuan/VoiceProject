package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.semisky.voicereceiveclient.jsonEntity.CallEntity;

/**
 * Created by chenhongrui on 2018/3/8
 * <p>
 * 内容摘要：用于打电话相关操作
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BTCallVoiceManager {

    private static final String TAG = "RadioVoiceManager";
    private Context mContext;

    public BTCallVoiceManager(Context context) {
        mContext = context;
    }

    public void setActionJson(CallEntity callEntity) {
        String action = callEntity.getAction();
        String param1 = callEntity.getParam1();
        try {
            switch (action) {
                case "call":
//                    AidlManager.getInstance().getBTCallListener().callByNumber(param1);
                    sendBroadcastForCall(param1);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBroadcastForCall(String param1) {
        Log.d(TAG, "sendBroadcastForCall: ");
        String ACTION_CALL = "com.semisky.cx62.ACTION_CALL_BY_NUMBER";
        String CALL_NUMBER = "CALL_NUMBER";
        Intent intent = new Intent(ACTION_CALL);
        intent.putExtra(CALL_NUMBER, param1);
        mContext.sendBroadcast(intent);
    }

    /**
     * 重拨号码
     */
    void redialNumber() {
        Log.d(TAG, "redialNumber: ");
        String ACTION_CALL = "com.semisky.cx62.ACTION_CALL_REDIAL_NUMBER";
        Intent intent = new Intent(ACTION_CALL);
        mContext.sendBroadcast(intent);
    }
}
