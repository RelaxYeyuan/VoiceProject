package com.semisky.voicereceiveclient.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.semisky.autoservice.manager.CarCtrlManager;
import com.semisky.voicereceiveclient.constant.AppConstant;
import com.semisky.voicereceiveclient.jsonEntity.CarControlEntity;
import com.semisky.voicereceiveclient.ManagerHandler.MessageHandler;

import org.json.JSONException;
import org.json.JSONObject;

import static com.semisky.autoservice.manager.CarCtrlManager.POSITION_FL;
import static com.semisky.autoservice.manager.CarCtrlManager.POSITION_FR;
import static com.semisky.autoservice.manager.CarCtrlManager.POSITION_RL;
import static com.semisky.autoservice.manager.CarCtrlManager.POSITION_RR;
import static com.semisky.autoservice.manager.CarCtrlManager.SKY_WINDOW_CLOSE;
import static com.semisky.autoservice.manager.CarCtrlManager.SKY_WINDOW_OPEN;
import static com.semisky.autoservice.manager.CarCtrlManager.SKY_WINDOW_RISE;
import static com.semisky.autoservice.manager.CarCtrlManager.STATE_CLOSE_80;
import static com.semisky.autoservice.manager.CarCtrlManager.STATE_OPEN_80;
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
 * {"name":"左前车窗","operation":"OPEN","focus":"carControl","rawText":"打开左前门车窗"}
 * {"name":"左前车窗","operation":"CLOSE","focus":"carControl","rawText":"关闭左前门车窗"}
 * {"name":"左前车窗","operation":"OPEN","focus":"carControl","rawText":"左前车窗打开一点"}
 * {"name":"天窗翘起","operation":"OPEN","focus":"carControl","rawText":"翘起天窗"}
 * <p>
 * <p>
 * {"level":"大一点","name":"左前车窗","operation":"OPEN","focus":"carControl","rawText":"打开大一点左前门车窗"}
 * {"level":"小一点","name":"左前车窗","operation":"OPEN","focus":"carControl","rawText":"打开小一点左前门车窗"}
 */
public class CarVoiceManager extends MessageHandler<String> {

    private static final String TAG = "CarVoiceManager";
    private Gson gson = new Gson();

    @Override
    public JSONObject action(int cmd, String actionJson) {
        JSONObject resultJson = new JSONObject();

        if (cmd == AppConstant.CAR_CONTROL_HANDLE) {
            try {
                CarControlEntity carEntity = gson.fromJson(actionJson, CarControlEntity.class);
                int type = setActionJson(carEntity);
                if (type == AppConstant.CAR_TYPE_SUCCESS) {
                    resultJson.put("status", "success");
                    return resultJson;
                } else if (type == AppConstant.CAR_TYPE_FAIL) {
                    resultJson.put("status", "fail");
                    resultJson.put("message", "抱歉，没有可处理的操作");
                    return resultJson;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (nextHandler != null) {
            nextHandler.action(cmd, actionJson);
        }

        //如果出现超出范围的语义，不作处理，默认返回成功
        try {
            resultJson.put("status", "success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultJson;
    }

    private int setActionJson(CarControlEntity carControlEntity) {
        String name = carControlEntity.getName();
        String operation = carControlEntity.getOperation();
        String level = carControlEntity.getLevel();
        switch (name) {
            case "天窗":
                setSkyLight(operation);
                return CAR_TYPE_SUCCESS;
            case "天窗翘起":
                setSkyLight();
                return CAR_TYPE_SUCCESS;
            case "车窗":
                setCarWindow(operation);
                return CAR_TYPE_SUCCESS;
            case "近光灯":
                setDippedHeadlight(operation);
                return CAR_TYPE_SUCCESS;
            case "左前车窗":
                setFLWindow(operation, level);
                return CAR_TYPE_SUCCESS;
            case "右前车窗":
                setFRWindow(operation, level);
                return CAR_TYPE_SUCCESS;
            case "左后车窗":
                setRLWindow(operation, level);
                return CAR_TYPE_SUCCESS;
            case "右后车窗":
                setRRWindow(operation, level);
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

    private void setCarWindow(String operation) {
        if (operation.equals("OPEN")) {
            CarCtrlManager.getInstance().setAllSideWindows(true);
            Log.d(TAG, "setCarWindow: OPEN");
        } else if (operation.equals("CLOSE")) {
            CarCtrlManager.getInstance().setAllSideWindows(false);
            Log.d(TAG, "setCarWindow: CLOSE");
        }
    }

    private void setFLWindow(String operation, String level) {
        Log.d(TAG, "setFLWindow: " + operation + " level " + level);
        if (operation.equals("OPEN")) {
            if (level == null) {
                CarCtrlManager.getInstance().setSideWindow(POSITION_FL, STATUS_OPEN);
                Log.d(TAG, "setFLWindow: 打开");
                return;
            }
            switch (level) {
                case "大一点":
                    CarCtrlManager.getInstance().setSideWindow(POSITION_FL, STATE_OPEN_80);
                    Log.d(TAG, "setFLWindow: 大一点");
                    break;
                case "小一点":
                    CarCtrlManager.getInstance().setSideWindow(POSITION_FL, STATE_CLOSE_80);
                    Log.d(TAG, "setFLWindow: 小一点");
                    break;
                default:

                    break;
            }
        } else if (operation.equals("CLOSE")) {
            CarCtrlManager.getInstance().setSideWindow(POSITION_FL, STATUS_CLOSE);
            Log.d(TAG, "setFLWindow: 关闭");
        }
    }

    private void setFRWindow(String operation, String level) {
        Log.d(TAG, "setFRWindow: " + operation + " level " + level);
        if (operation.equals("OPEN")) {
            if (level == null) {
                CarCtrlManager.getInstance().setSideWindow(POSITION_FR, STATUS_OPEN);
                Log.d(TAG, "setFRWindow: 打开");
                return;
            }
            switch (level) {
                case "大一点":
                    CarCtrlManager.getInstance().setSideWindow(POSITION_FR, STATE_OPEN_80);
                    Log.d(TAG, "setFRWindow: 大一点");
                    break;
                case "小一点":
                    CarCtrlManager.getInstance().setSideWindow(POSITION_FR, STATE_CLOSE_80);
                    Log.d(TAG, "setFRWindow: 小一点");
                    break;
                default:

                    break;
            }
        } else if (operation.equals("CLOSE")) {
            CarCtrlManager.getInstance().setSideWindow(POSITION_FR, STATUS_CLOSE);
            Log.d(TAG, "setFRWindow: 关闭");
        }
    }

    private void setRLWindow(String operation, String level) {
        Log.d(TAG, "setRLWindow: " + operation + " level " + level);
        if (operation.equals("OPEN")) {
            if (level == null) {
                CarCtrlManager.getInstance().setSideWindow(POSITION_RL, STATUS_OPEN);
                Log.d(TAG, "setRLWindow: 打开");
                return;
            }
            switch (level) {
                case "大一点":
                    CarCtrlManager.getInstance().setSideWindow(POSITION_RL, STATE_OPEN_80);
                    Log.d(TAG, "setRLWindow: 大一点");
                    break;
                case "小一点":
                    CarCtrlManager.getInstance().setSideWindow(POSITION_RL, STATE_CLOSE_80);
                    Log.d(TAG, "setRLWindow: 小一点");
                    break;
                default:

                    break;
            }
        } else if (operation.equals("CLOSE")) {
            CarCtrlManager.getInstance().setSideWindow(POSITION_RL, STATUS_CLOSE);
            Log.d(TAG, "setRLWindow: 关闭");
        }
    }

    private void setRRWindow(String operation, String level) {
        Log.d(TAG, "setRRWindow: " + operation + " level " + level);
        if (operation.equals("OPEN")) {
            if (level == null) {
                CarCtrlManager.getInstance().setSideWindow(POSITION_RR, STATUS_OPEN);
                Log.d(TAG, "setRRWindow: 打开");
                return;
            }
            switch (level) {
                case "大一点":
                    CarCtrlManager.getInstance().setSideWindow(POSITION_RR, STATE_OPEN_80);
                    Log.d(TAG, "setRRWindow: 大一点");
                    break;
                case "小一点":
                    CarCtrlManager.getInstance().setSideWindow(POSITION_RR, STATE_CLOSE_80);
                    Log.d(TAG, "setRRWindow: 小一点");
                    break;
                default:

                    break;
            }
        } else if (operation.equals("CLOSE")) {
            CarCtrlManager.getInstance().setSideWindow(POSITION_RR, STATUS_CLOSE);
            Log.d(TAG, "setRRWindow: 关闭");
        }
    }

}
