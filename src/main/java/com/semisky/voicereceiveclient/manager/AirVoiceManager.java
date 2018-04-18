package com.semisky.voicereceiveclient.manager;

import android.util.Log;

import com.semisky.autoservice.manager.ACManager;
import com.semisky.voicereceiveclient.jsonEntity.AirControlEntity;

import static com.semisky.autoservice.manager.ACManager.AIR_WORKING_OFF;
import static com.semisky.autoservice.manager.ACManager.AIR_WORKING_ON;
import static com.semisky.autoservice.manager.ACManager.CIR_MODE_INNER;
import static com.semisky.autoservice.manager.ACManager.CIR_MODE_OUTSIDE;
import static com.semisky.autoservice.manager.ACManager.DEFROST_MODE_FRONT;
import static com.semisky.autoservice.manager.ACManager.DEFROST_MODE_REAR;
import static com.semisky.autoservice.manager.ACManager.SIDE_FL;
import static com.semisky.autoservice.manager.ACManager.WIND_EXIT_MODE_FACE;
import static com.semisky.autoservice.manager.ACManager.WIND_EXIT_MODE_FACE_FOOT;
import static com.semisky.autoservice.manager.ACManager.WIND_EXIT_MODE_FOOT;

/**
 * Created by chenhongrui on 2018/3/9
 * <p>
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class AirVoiceManager {

    private static final String TAG = "AirVoiceManager";

    public void setActionJson(AirControlEntity airEntity) {
        String airflow_direction = airEntity.getAirflow_direction();
        String device = airEntity.getDevice();
        String fan_speed = airEntity.getFan_speed();
        String mode = airEntity.getMode();
        String operation = airEntity.getOperation();
        String temperature = airEntity.getTemperature();

        if (operation != null) {
            if (operation.equals("SET")) {
                setAirMode(mode, temperature, fan_speed);
            } else if (operation.equals("OPEN")) {
                //{"device":"空调","operation":"OPEN","focus":"airControl","rawText":"打开空调"}
                ACManager.getInstance().setAirConditionerWorking(AIR_WORKING_ON);
                Log.d(TAG, "OPEN: exit");
            } else if (operation.equals("CLOSE")) {
                //{"device":"空调","operation":"CLOSE","focus":"airControl","rawText":"关闭空调"}
                ACManager.getInstance().setAirConditionerWorking(AIR_WORKING_OFF);
                Log.d(TAG, "CLOSE: launch");
            }
        }
    }

    private void setAirMode(String mode, String temperature, String fanSpeed) {
        if (mode != null) {
            Log.d(TAG, "setAirMode:mode " + mode);
            switch (mode) {
                case "制冷": //{"mode":"制冷","operation":"SET","focus":"airControl","rawText":"打开制冷模式"}
                    ACManager.getInstance().setAirConditionerAC(true);//开启空调
                    Log.d(TAG, "setAirMode: 制冷");
                    break;

                case "内循环"://{"mode":"内循环","operation":"SET","focus":"airControl","rawText":"开启内循环模式"}
                    ACManager.getInstance().setAirConditionerCirMode(CIR_MODE_INNER);
                    Log.d(TAG, "setAirMode: 内循环");
                    break;

                case "外循环"://{"mode":"外循环","operation":"SET","focus":"airControl","rawText":"开启外循环模式"}
                    ACManager.getInstance().setAirConditionerCirMode(CIR_MODE_OUTSIDE);
                    Log.d(TAG, "setAirMode: 外循环");
                    break;

                case "吹面":
                    ACManager.getInstance().setAirConditionerWindExitMode(WIND_EXIT_MODE_FACE);
                    Log.d(TAG, "setAirMode: 吹面");
                    break;

                case "吹面吹脚":
                    ACManager.getInstance().setAirConditionerWindExitMode(WIND_EXIT_MODE_FACE_FOOT);
                    Log.d(TAG, "setAirMode: 吹面吹脚");
                    break;

                case "吹脚":
                    ACManager.getInstance().setAirConditionerWindExitMode(WIND_EXIT_MODE_FOOT);
                    Log.d(TAG, "setAirMode: 吹脚");
                    break;

                case "前除霜"://{"mode":"除霜","operation":"SET","focus":"airControl","rawText":"开启除霜模式"}
                    ACManager.getInstance().enableAirConditionerDefrost(DEFROST_MODE_FRONT, true);
                    Log.d(TAG, "setAirMode: 前除霜");
                    break;

                case "后除霜"://{"mode":"除霜","operation":"SET","focus":"airControl","rawText":"开启除霜模式"}
                    ACManager.getInstance().enableAirConditionerDefrost(DEFROST_MODE_REAR, true);
                    Log.d(TAG, "setAirMode: 后除霜");
                    break;

                default:

                    break;
            }
        }

        //{"device":"空调","operation":"SET","temperature":"7","focus":"airControl","rawText":"空调温度调到七档"}
        if (temperature != null) {
            Log.d(TAG, "setAirMode:temperature " + temperature);
            switch (temperature) {
                case "+"://{"operation":"SET","temperature":"+","focus":"airControl","rawText":"有点冷"}
                    setAirTemperatureUp();
                    break;
                case "-"://{"operation":"SET","temperature":"-","focus":"airControl","rawText":"有点热"}
                    setAirTemperatureDown();
                    break;
                case "+1"://{"operation":"SET","temperature":"+1","focus":"airControl","rawText":"温度调高一度"}
                    setAirTemperatureUp();
                    break;
                case "-1"://{"operation":"SET","temperature":"+1","focus":"airControl","rawText":"温度调高一度"}
                    setAirTemperatureDown();
                    break;
                case "30": //{"device":"空调","operation":"SET","temperature":"30","focus":"airControl","rawText":"空调温度调到最高"}
                    ACManager.getInstance().setAirConditionerTemp(SIDE_FL, 17);
                    Log.d(TAG, "setAirMode: 30");
                    break;
                case "16"://{"device":"空调","operation":"SET","temperature":"16","focus":"airControl","rawText":"空调温度调到最低"}
                    ACManager.getInstance().setAirConditionerTemp(SIDE_FL, 1);
                    Log.d(TAG, "setAirMode: 16");
                    break;
                default:
                    Log.d(TAG, "setAirMode:temperatureInt " + temperature);
                    int temperatureInt = Integer.valueOf(temperature);
                    if (temperatureInt < 17 && temperatureInt > 0) {
                        //调节到具体档位
                        ACManager.getInstance().setAirConditionerTemp(SIDE_FL, temperatureInt);
                    }
                    break;
            }
        }

        //{"fan_speed":"最大","operation":"SET","focus":"airControl","rawText":"风速调到最大"}
        //{"fan_speed":"最小","operation":"SET","focus":"airControl","rawText":"风速调到最小"}
        if (fanSpeed != null) {
            switch (fanSpeed) {
                case "+"://{"fan_speed":"+","operation":"SET","focus":"airControl","rawText":"风速大一点"}
                    setFanSpeedUp();
                    break;
                case "-"://{"fan_speed":"-","operation":"SET","focus":"airControl","rawText":"风速小一点"}
                    setFanSpeedDown();
                    break;
                case "最大":
                    ACManager.getInstance().setAirConditionerWindValue(7);
                    Log.d(TAG, "setAirMode: 最大");
                    break;
                case "最小":
                    ACManager.getInstance().setAirConditionerWindValue(1);
                    Log.d(TAG, "setAirMode: 最小");
                    break;
                default:
                    Log.d(TAG, "setAirMode:fanSpeed " + fanSpeed);
                    int fanSpeedInt = Integer.valueOf(fanSpeed);
                    if (fanSpeedInt < 7 && fanSpeedInt > 0) {
                        //调节到具体档位
                        ACManager.getInstance().setAirConditionerWindValue(fanSpeedInt);
                    }
                    break;
            }
        }
    }

    private void setAirTemperatureUp() {
        int CurrentTemperature = ACManager.getInstance().getAirConditionerTemp(SIDE_FL);//当前温度
        Log.d(TAG, "setAirTemperatureDown: " + CurrentTemperature);
        if (CurrentTemperature < 17 && CurrentTemperature > 0) {
            ACManager.getInstance().setAirConditionerTemp(SIDE_FL, CurrentTemperature + 1);
        }
    }

    private void setAirTemperatureDown() {
        int CurrentTemperature = ACManager.getInstance().getAirConditionerTemp(SIDE_FL);//当前温度
        Log.d(TAG, "setAirTemperatureDown: " + CurrentTemperature);
        if (CurrentTemperature < 17 && CurrentTemperature > 0) {
            ACManager.getInstance().setAirConditionerTemp(SIDE_FL, CurrentTemperature - 1);
        }
    }


    private void setFanSpeedUp() {
        int fanSpeed = ACManager.getInstance().getAirConditionerFanLevel();
        Log.d(TAG, "setFanSpeedUp: " + fanSpeed);
        if (fanSpeed < 7 && fanSpeed > 0) {
            ACManager.getInstance().setAirConditionerWindValue(fanSpeed + 1);
        }
    }

    private void setFanSpeedDown() {
        int fanSpeed = ACManager.getInstance().getAirConditionerFanLevel();
        Log.d(TAG, "setFanSpeedDown: " + fanSpeed);
        if (fanSpeed < 7 && fanSpeed > 0) {
            ACManager.getInstance().setAirConditionerWindValue(fanSpeed - 1);
        }
    }
}
