package com.semisky.voicereceiveclient.manager;

import android.os.RemoteException;

import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.jsonEntity.CarControlEntity;

/**
 * Created by chenhongrui on 2018/3/9
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class CarVoiceManager {

    public void setActionJson(CarControlEntity carControlEntity) {
        String name = carControlEntity.getName();
        String operation = carControlEntity.getOperation();
        try {
            switch (name) {
                case "天窗":
                    AidlManager.getInstance().getCarControlListener().openSkyLight();
                    break;
                case "车窗":
                    AidlManager.getInstance().getCarControlListener().openCarWindow();
                    break;
                case "近光灯":
                    AidlManager.getInstance().getCarControlListener().openDippedHeadlight();
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
