package com.semisky.voicereceiveclient.manager;

import android.util.Log;

import com.semisky.autoservice.manager.CarCtrlManager;
import com.semisky.voicereceiveclient.jsonEntity.CarControlEntity;

import static com.semisky.autoservice.manager.CarCtrlManager.POSITION_FL;
import static com.semisky.autoservice.manager.CarCtrlManager.POSITION_FR;
import static com.semisky.autoservice.manager.CarCtrlManager.POSITION_RL;
import static com.semisky.autoservice.manager.CarCtrlManager.POSITION_RR;
import static com.semisky.autoservice.manager.CarCtrlManager.SKY_WINDOW_CLOSE;
import static com.semisky.autoservice.manager.CarCtrlManager.SKY_WINDOW_OPEN;
import static com.semisky.autoservice.manager.CarCtrlManager.SKY_WINDOW_RISE;
import static com.semisky.autoservice.manager.CarCtrlManager.STATUS_CLOSE;
import static com.semisky.autoservice.manager.CarCtrlManager.STATUS_OPEN;
import static com.semisky.autoservice.manager.CarCtrlManager.TYPE_WIDTH_LAMP;
import static com.semisky.voicereceiveclient.constant.AppConstant.CAR_TYPE_FAIL;
import static com.semisky.voicereceiveclient.constant.AppConstant.CAR_TYPE_SUCCESS;

/**
 * Created by chenhongrui on 2018/3/9
 * <p>
 * 内容摘要：
 * 版权所有：Semisky
 * 修改内容：
 * <p>
 * {"name":"车窗","operation":"OPEN","focus":"carControl","rawText":"打开全部车窗"}
 * {"name":"车窗","operation":"CLOSE","focus":"carControl","rawText":"关闭全部车窗"}
 * {"name":"左前车窗","operation":"OPEN","focus":"carControl","rawText":"打开左前车窗"}
 * {"name":"左前车窗","operation":"CLOSE","focus":"carControl","rawText":"关闭左前车窗"}
 * {"name":"左前车窗","operation":"OPEN","focus":"carControl","rawText":"左前车窗打开一点"}
 * {"name":"大一点左前车窗","operation":"LAUNCH","focus":"app","rawText":"打开大一点左前车窗"}
 * {"name":"小一点左前车窗","operation":"LAUNCH","focus":"app","rawText":"打开小一点左前车窗"}
 * {"name":"天窗翘起","operation":"OPEN","focus":"carControl","rawText":"翘起天窗"}
 */
public class CarVoiceManager {

    private static final String TAG = "CarVoiceManager";

    public int setActionJson(CarControlEntity carControlEntity) {
        String name = carControlEntity.getName();
        String operation = carControlEntity.getOperation();
        switch (name) {
            case "天窗":
                setSkyLight(operation);
                return CAR_TYPE_SUCCESS;
            case "天窗翘起":
                setSkyLight();
                return CAR_TYPE_SUCCESS;
            case "车窗":
                setCarWindow(operation, 0);
                return CAR_TYPE_SUCCESS;
            case "近光灯":
                setDippedHeadlight(operation);
                return CAR_TYPE_SUCCESS;
            case "左前车窗":
                setCarWindow(operation, 1);
                return CAR_TYPE_SUCCESS;
            case "右前车窗":
                setCarWindow(operation, 2);
                return CAR_TYPE_SUCCESS;
            case "左后车窗":
                setCarWindow(operation, 3);
                return CAR_TYPE_SUCCESS;
            case "右后车窗":
                setCarWindow(operation, 4);
                return CAR_TYPE_SUCCESS;
            default:
                return CAR_TYPE_FAIL;
        }
    }

    private void setDippedHeadlight(String operation) {
        if (operation.equals("OPEN")) {//{"name":"近光灯","operation":"OPEN","focus":"carControl","rawText":"打开近光灯"}
            CarCtrlManager.getInstance().setLampStatus(TYPE_WIDTH_LAMP, STATUS_OPEN);
            Log.d(TAG, "setDippedHeadlight: OPEN");
        } else if (operation.equals("CLOSE")) {//{"name":"近光灯","operation":"CLOSE","focus":"carControl","rawText":"关闭近光灯"}
            CarCtrlManager.getInstance().setLampStatus(TYPE_WIDTH_LAMP, STATUS_CLOSE);
            Log.d(TAG, "setDippedHeadlight: CLOSE");
        }
    }

    private void setSkyLight() {
        //SKY_WINDOW_RISE 翘起天窗
        Log.d(TAG, "setSkyLight:翘起天窗 ");
        CarCtrlManager.getInstance().setSkyWindow(SKY_WINDOW_RISE);
    }

    private void setSkyLight(String operation) {
        if (operation.equals("OPEN")) {//{"name":"天窗","operation":"OPEN","focus":"carControl","rawText":"打开天窗"}
            CarCtrlManager.getInstance().setSkyWindow(SKY_WINDOW_OPEN);
            Log.d(TAG, "setSkyLight: OPEN");
        } else if (operation.equals("CLOSE")) {//{"name":"天窗","operation":"CLOSE","focus":"carControl","rawText":"关闭天窗"}
            CarCtrlManager.getInstance().setSkyWindow(SKY_WINDOW_CLOSE);
            Log.d(TAG, "setSkyLight: CLOSE");
        }//SKY_WINDOW_RISE 翘起天窗
    }

    private void setCarWindow(String operation, int type) {
        if (operation.equals("OPEN")) {
            Log.d(TAG, "setCarWindow: " + type);
            switch (type) {
                case 0:
                    CarCtrlManager.getInstance().setAllSideWindows(true);
                    break;
                case 1:
                    CarCtrlManager.getInstance().setSideWindow(POSITION_FL, STATUS_OPEN);
                    break;
                case 2:
                    CarCtrlManager.getInstance().setSideWindow(POSITION_FR, STATUS_OPEN);
                    break;
                case 3:
                    CarCtrlManager.getInstance().setSideWindow(POSITION_RL, STATUS_OPEN);
                    break;
                case 4:
                    CarCtrlManager.getInstance().setSideWindow(POSITION_RR, STATUS_OPEN);
                    break;
            }
        } else if (operation.equals("CLOSE")) {
            Log.d(TAG, "setCarWindow: " + type);
            switch (type) {
                case 0:
                    CarCtrlManager.getInstance().setAllSideWindows(false);
                    break;
                case 1:
                    CarCtrlManager.getInstance().setSideWindow(POSITION_FL, STATUS_CLOSE);
                    break;
                case 2:
                    CarCtrlManager.getInstance().setSideWindow(POSITION_FR, STATUS_CLOSE);
                    break;
                case 3:
                    CarCtrlManager.getInstance().setSideWindow(POSITION_RL, STATUS_CLOSE);
                    break;
                case 4:
                    CarCtrlManager.getInstance().setSideWindow(POSITION_RR, STATUS_CLOSE);
                    break;
            }
        }

    }
}
