package com.semisky.voicereceiveclient.manager;

import com.semisky.autoservice.manager.CarCtrlManager;
import com.semisky.voicereceiveclient.jsonEntity.CarControlEntity;

import static com.semisky.autoservice.manager.CarCtrlManager.STATUS_CLOSE;
import static com.semisky.autoservice.manager.CarCtrlManager.STATUS_OPEN;
import static com.semisky.autoservice.manager.CarCtrlManager.TYPE_WIDTH_LAMP;

/**
 * Created by chenhongrui on 2018/3/9
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * {"name":"左前车窗","operation":"OPEN","focus":"carControl","rawText":"打开左前车窗"}
 * {"name":"左前车窗","operation":"OPEN","focus":"carControl","rawText":"左前车窗打开大一点"}
 * {"name":"大一点左前车窗","operation":"LAUNCH","focus":"app","rawText":"打开大一点左前车窗"}
 * {"name":"小一点左前车窗","operation":"LAUNCH","focus":"app","rawText":"打开小一点左前车窗"}
 * 修改日期
 */
public class CarVoiceManager {

    public void setActionJson(CarControlEntity carControlEntity) {
        String name = carControlEntity.getName();
        String operation = carControlEntity.getOperation();
        switch (name) {
            case "天窗":
                setSkyLight(operation);
                break;
            case "车窗":
                setCarWindow(operation);
                break;
            case "近光灯":
                setDippedHeadlight(operation);
                break;
        }
    }

    private void setDippedHeadlight(String operation) {
        if (operation.equals("OPEN")) {//{"name":"近光灯","operation":"OPEN","focus":"carControl","rawText":"打开近光灯"}
            CarCtrlManager.getInstance().setLampStatus(TYPE_WIDTH_LAMP, STATUS_OPEN);
        } else if (operation.equals("CLOSE")) {//{"name":"近光灯","operation":"CLOSE","focus":"carControl","rawText":"关闭近光灯"}
            CarCtrlManager.getInstance().setLampStatus(TYPE_WIDTH_LAMP, STATUS_CLOSE);
        }
    }

    private void setSkyLight(String operation) {
        if (operation.equals("OPEN")) {//{"name":"天窗","operation":"OPEN","focus":"carControl","rawText":"打开天窗"}

        } else if (operation.equals("CLOSE")) {//{"name":"天窗","operation":"CLOSE","focus":"carControl","rawText":"关闭天窗"}

        }
    }

    private void setCarWindow(String operation) {

    }
}
