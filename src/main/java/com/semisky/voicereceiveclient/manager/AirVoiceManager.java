package com.semisky.voicereceiveclient.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.semisky.autoservice.manager.ACManager;
import com.semisky.voicereceiveclient.constant.AppConstant;
import com.semisky.voicereceiveclient.jsonEntity.AirControlEntity;
import com.semisky.voicereceiveclient.ManagerHandler.MessageHandler;

import org.json.JSONException;
import org.json.JSONObject;

import static com.semisky.autoservice.manager.ACManager.AIR_WORKING_OFF;
import static com.semisky.autoservice.manager.ACManager.AIR_WORKING_ON;
import static com.semisky.autoservice.manager.ACManager.CIR_MODE_INNER;
import static com.semisky.autoservice.manager.ACManager.CIR_MODE_OUTSIDE;
import static com.semisky.autoservice.manager.ACManager.CTRL_MODE_COOL;
import static com.semisky.autoservice.manager.ACManager.CTRL_MODE_HEAT;
import static com.semisky.autoservice.manager.ACManager.DEFROST_MODE_FRONT;
import static com.semisky.autoservice.manager.ACManager.DEFROST_MODE_REAR;
import static com.semisky.autoservice.manager.ACManager.SIDE_FL;
import static com.semisky.autoservice.manager.ACManager.WIND_EXIT_MODE_FACE;
import static com.semisky.autoservice.manager.ACManager.WIND_EXIT_MODE_FACE_FOOT;
import static com.semisky.autoservice.manager.ACManager.WIND_EXIT_MODE_FOOT;
import static com.semisky.autoservice.manager.ACManager.WIND_EXIT_MODE_FOOT_DEFROST;
import static com.semisky.voicereceiveclient.constant.AppConstant.AIR_TYPE_FAIL;
import static com.semisky.voicereceiveclient.constant.AppConstant.AIR_TYPE_SUCCESS;

/**
 * Created by chenhongrui on 2018/3/9
 * <p>
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class AirVoiceManager extends MessageHandler<String> {

    private static final String TAG = "AirVoiceManager";
    private Gson gson = new Gson();

    @Override
    public JSONObject action(int cmd, String actionJson) {
        JSONObject resultJson = new JSONObject();

        if (cmd == AppConstant.AIR_CONTROL_HANDLE) {
            try {
                AirControlEntity airEntity = gson.fromJson(actionJson, AirControlEntity.class);
                int type = setActionJson(airEntity);
                if (type == AppConstant.AIR_TYPE_SUCCESS) {
                    resultJson.put("status", "success");
                    return resultJson;
                } else if (type == AppConstant.AIR_TYPE_FAIL) {
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

    private int setActionJson(AirControlEntity airEntity) {
        String airflow_direction = airEntity.getAirflow_direction();
        String device = airEntity.getDevice();
        String fan_speed = airEntity.getFan_speed();
        String mode = airEntity.getMode();
        String operation = airEntity.getOperation();
        String temperature = airEntity.getTemperature();

        if (operation != null) {
            switch (operation) {
                case "SET":
                    return openAirMode(mode, temperature, fan_speed, airflow_direction);
                case "OPEN":
                    //{"device":"空调","operation":"OPEN","focus":"airControl","rawText":"打开空调"}
                    ACManager.getInstance().setAirConditionerWorking(AIR_WORKING_ON);
                    Log.d(TAG, "OPEN:");
                    return AIR_TYPE_SUCCESS;
                case "CLOSE":
                    //{"device":"空调","operation":"CLOSE","focus":"airControl","rawText":"关闭空调"}
                    ACManager.getInstance().setAirConditionerWorking(AIR_WORKING_OFF);
                    Log.d(TAG, "CLOSE");
                    return AIR_TYPE_SUCCESS;

                default:
                    return AIR_TYPE_FAIL;
            }
        }
        Log.d(TAG, "setActionJson: operation null");
        return AIR_TYPE_FAIL;
    }

    //{"mode":"前除霜","operation":"SET","focus":"airControl","rawText":"打开前除霜"}
    //{"mode":"后除霜","operation":"SET","focus":"airControl","rawText":"打开后除霜模式"}
    //{"name":"后除霜","operation":"EXIT","focus":"app","rawText":"关闭后除霜"}
    //{"name":"后除霜模式","operation":"EXIT","focus":"app","rawText":"关闭后除霜模式"}
    //{"mode":"制冷","operation":"SET","focus":"airControl","rawText":"打开制冷模式"}
    //{"mode":"内循环","operation":"SET","focus":"airControl","rawText":"开启内循环模式"}
    //{"mode":"外循环","operation":"SET","focus":"airControl","rawText":"开启外循环模式"}

    //{"mode":"制冷","operation":"SET","focus":"airControl","rawText":"我要制冷"}
    //{"mode":"制热","operation":"SET","focus":"airControl","rawText":"我要制热"}

    private int openAirMode(String mode, String temperature, String fanSpeed, String airflowDirection) {
        if (mode != null) {
            switch (mode) {
                case "制冷":
                    ACManager.getInstance().setAirCoolOrHeatMode(CTRL_MODE_COOL);
                    Log.d(TAG, "openAirMode: 制冷");
                    return AIR_TYPE_SUCCESS;

                case "制热":
                    ACManager.getInstance().setAirCoolOrHeatMode(CTRL_MODE_HEAT);
                    Log.d(TAG, "openAirMode: 制热");
                    return AIR_TYPE_SUCCESS;

                case "内循环":
                    ACManager.getInstance().setAirConditionerCirMode(CIR_MODE_INNER);
                    Log.d(TAG, "openAirMode: 内循环");
                    return AIR_TYPE_SUCCESS;

                case "外循环":
                    ACManager.getInstance().setAirConditionerCirMode(CIR_MODE_OUTSIDE);
                    Log.d(TAG, "openAirMode: 外循环");
                    return AIR_TYPE_SUCCESS;

                case "前除霜":
                    ACManager.getInstance().enableAirConditionerDefrost(DEFROST_MODE_FRONT, true);
                    Log.d(TAG, "openAirMode: 打开前除霜");
                    return AIR_TYPE_SUCCESS;

                case "后除霜":
                    ACManager.getInstance().enableAirConditionerDefrost(DEFROST_MODE_REAR, true);
                    Log.d(TAG, "openAirMode: 打开后除霜");
                    return AIR_TYPE_SUCCESS;

                case "除霜":
                    if (airflowDirection.equals("脚")) {
                        ACManager.getInstance().setAirConditionerWindExitMode(WIND_EXIT_MODE_FOOT_DEFROST);
                        Log.d(TAG, "openAirMode: 吹脚除霜");
                    }
                    return AIR_TYPE_SUCCESS;
                default:
                    return AIR_TYPE_FAIL;
            }
        }

        //{"airflow_direction":"面","operation":"SET","focus":"airControl","rawText":"打开吹面"}
        //{"airflow_direction":"面","device":"空调","operation":"SET","focus":"airControl","rawText":"打开空调吹面模式"}
        //{"airflow_direction":"吹面吹脚","operation":"SET","focus":"airControl","rawText":"打开吹面吹脚"}
        //{"airflow_direction":"脚","operation":"SET","focus":"airControl","rawText":"打开吹脚"}
        //{"airflow_direction":"脚","mode":"除霜","operation":"SET","focus":"airControl","rawText":"打开吹脚除霜模式"}
        if (airflowDirection != null) {
            switch (airflowDirection) {
                case "面":
                    ACManager.getInstance().setAirConditionerWindExitMode(WIND_EXIT_MODE_FACE);
                    Log.d(TAG, "openAirMode: 吹面");
                    return AIR_TYPE_SUCCESS;
                case "吹面吹脚":
                    ACManager.getInstance().setAirConditionerWindExitMode(WIND_EXIT_MODE_FACE_FOOT);
                    Log.d(TAG, "openAirMode: 吹面吹脚");
                    return AIR_TYPE_SUCCESS;
                case "脚":
                    ACManager.getInstance().setAirConditionerWindExitMode(WIND_EXIT_MODE_FOOT);
                    Log.d(TAG, "openAirMode: 吹脚");
                    return AIR_TYPE_SUCCESS;

                default:
                    return AIR_TYPE_FAIL;
            }
        }

        //{"device":"空调","operation":"SET","temperature":"7","focus":"airControl","rawText":"空调温度调到七档"}
        if (temperature != null) {
            Log.d(TAG, "openAirMode:temperature " + temperature);
            switch (temperature) {
                case "+"://{"operation":"SET","temperature":"+","focus":"airControl","rawText":"有点冷"}
//                    setAirTemperatureUp();
                    return AIR_TYPE_FAIL;
                case "-"://{"operation":"SET","temperature":"-","focus":"airControl","rawText":"有点热"}
//                    setAirTemperatureDown();
                    return AIR_TYPE_FAIL;
                case "+1"://{"operation":"SET","temperature":"+1","focus":"airControl","rawText":"温度调高一度"}
//                    setAirTemperatureUp();
                    return AIR_TYPE_FAIL;
                case "-1"://{"operation":"SET","temperature":"+1","focus":"airControl","rawText":"温度调高一度"}
//                    setAirTemperatureDown();
                    return AIR_TYPE_FAIL;
                case "最高":
                    //{"device":"空调","operation":"SET","temperature":"30","focus":"airControl","rawText":"空调温度调到最高"}
                    //{"device":"空调","operation":"SET","temperature":"最高","focus":"airControl","rawText":"空调温度调到最高"}
                    //{"operation":"SET","temperature":"最高","focus":"airControl","rawText":"温度调到最大"}
                    ACManager.getInstance().setAirConditionerTemp(SIDE_FL, 17);
                    Log.d(TAG, "openAirMode: 30");
                    return AIR_TYPE_SUCCESS;
                case "最低":
                    //{"device":"空调","operation":"SET","temperature":"16","focus":"airControl","rawText":"空调温度调到最低"}
                    //{"device":"空调","operation":"SET","temperature":"最低","focus":"airControl","rawText":"空调温度调到最低"}
                    //{"device":"空调","operation":"SET","temperature":"最低","focus":"airControl","rawText":"将空调温度调到最低"}
                    ACManager.getInstance().setAirConditionerTemp(SIDE_FL, 1);
                    Log.d(TAG, "openAirMode: 16");
                    return AIR_TYPE_SUCCESS;
                default:
                    Log.d(TAG, "openAirMode:temperatureInt " + temperature);
//                    int temperatureInt = Integer.valueOf(temperature);
//                    if (temperatureInt <= 17 && temperatureInt >= 0) {
//                        //调节到具体档位
//                        ACManager.getInstance().setAirConditionerTemp(SIDE_FL, temperatureInt);
//                    }
                    return AIR_TYPE_FAIL;
            }
        }

        //{"fan_speed":"最大","operation":"SET","focus":"airControl","rawText":"风速调到最大"}
        //{"fan_speed":"最小","operation":"SET","focus":"airControl","rawText":"风速调到最小"}
        if (fanSpeed != null) {
            switch (fanSpeed) {
                case "+"://{"fan_speed":"+","operation":"SET","focus":"airControl","rawText":"风速大一点"}
                    setFanSpeedUp();
                    return AIR_TYPE_SUCCESS;
                case "-"://{"fan_speed":"-","operation":"SET","focus":"airControl","rawText":"风速小一点"}
                    setFanSpeedDown();
                    return AIR_TYPE_SUCCESS;
                case "最大":
                    ACManager.getInstance().setAirConditionerWindValue(7);
                    Log.d(TAG, "openAirMode: 最大");
                    return AIR_TYPE_SUCCESS;
                case "最小":
                    ACManager.getInstance().setAirConditionerWindValue(1);
                    Log.d(TAG, "openAirMode: 最小");
                    return AIR_TYPE_SUCCESS;
                default:
//                    Log.d(TAG, "openAirMode:fanSpeed " + fanSpeed);
//                    int fanSpeedInt = Integer.valueOf(fanSpeed);
//                    if (fanSpeedInt <= 7 && fanSpeedInt >= 0) {
//                        //调节到具体档位
//                        ACManager.getInstance().setAirConditionerWindValue(fanSpeedInt);
//                    }
                    return AIR_TYPE_FAIL;
            }
        }
        return AIR_TYPE_FAIL;
    }

    private void setAirTemperatureUp() {
        int CurrentTemperature = ACManager.getInstance().getAirConditionerTemp(SIDE_FL);//当前温度
        Log.d(TAG, "setAirTemperatureDown: " + CurrentTemperature);
        if (CurrentTemperature < 17 && CurrentTemperature >= 0) {
            ACManager.getInstance().setAirConditionerTemp(SIDE_FL, CurrentTemperature + 1);
        }
    }

    private void setAirTemperatureDown() {
        int CurrentTemperature = ACManager.getInstance().getAirConditionerTemp(SIDE_FL);//当前温度
        Log.d(TAG, "setAirTemperatureDown: " + CurrentTemperature);
        if (CurrentTemperature <= 17 && CurrentTemperature > 0) {
            ACManager.getInstance().setAirConditionerTemp(SIDE_FL, CurrentTemperature - 1);
        }
    }

    private void setFanSpeedUp() {
        int fanSpeed = ACManager.getInstance().getAirConditionerFanLevel();
        Log.d(TAG, "setFanSpeedUp: " + fanSpeed);
        if (fanSpeed < 7 && fanSpeed >= 0) {
            ACManager.getInstance().setAirConditionerWindValue(fanSpeed + 1);
        }
    }

    private void setFanSpeedDown() {
        int fanSpeed = ACManager.getInstance().getAirConditionerFanLevel();
        Log.d(TAG, "setFanSpeedDown: " + fanSpeed);
        if (fanSpeed <= 7 && fanSpeed > 0) {
            ACManager.getInstance().setAirConditionerWindValue(fanSpeed - 1);
        }
    }


}