package com.semisky.voicereceiveclient.manager;

import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.jsonEntity.CallEntity;

/**
 * Created by chenhongrui on 2018/3/8
 * <p>
 * 内容摘要：用于收音机相关操作 focus = radio
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BTCallVoiceManager {

    private static final String TAG = "RadioVoiceManager";

    public BTCallVoiceManager() {
    }

    public void setActionJson(CallEntity callEntity) {
        String action = callEntity.getAction();
        String param1 = callEntity.getParam1();
        try {
            switch (action) {
                case "call":
                    AidlManager.getInstance().getBTCallListener().callByNumber(param1);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
