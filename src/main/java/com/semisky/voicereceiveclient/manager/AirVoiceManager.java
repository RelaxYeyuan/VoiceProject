package com.semisky.voicereceiveclient.manager;

import android.util.Log;

import com.semisky.autoservice.manager.ACManager;
import com.semisky.voicereceiveclient.jsonEntity.AirControlEntity;

import static com.semisky.autoservice.manager.ACManager.CIR_MODE_INNER;
import static com.semisky.autoservice.manager.ACManager.CIR_MODE_OUTSIDE;
import static com.semisky.autoservice.manager.ACManager.DEFROST_MODE_FRONT;
import static com.semisky.autoservice.manager.ACManager.SIDE_FL;

/**
 * Created by chenhongrui on 2018/3/9
 * <p>
 * 内容摘要：${TODO}
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
            }
        }
    }

    private void setAirMode(String mode, String temperature, String fanSpeed) {
        if (mode != null) {
            switch (mode) {
                case "制冷": //{"mode":"制冷","operation":"SET","focus":"airControl","rawText":"打开制冷模式"}
                    ACManager.getInstance().setAirConditionerAC(true);//开启空调
                    break;

                case "内循环"://{"mode":"内循环","operation":"SET","focus":"airControl","rawText":"开启内循环模式"}
                    ACManager.getInstance().setAirConditionerCirMode(CIR_MODE_INNER);
                    break;

                case "外循环"://{"mode":"外循环","operation":"SET","focus":"airControl","rawText":"开启内循环模式"}
                    ACManager.getInstance().setAirConditionerCirMode(CIR_MODE_OUTSIDE);
                    break;

                case "自动"://{"mode":"自动","operation":"SET","focus":"airControl","rawText":"打开自动模式"}
                    ACManager.getInstance().enableAirConditionerAutoMode(true);
                    break;

                case "手动"://{"mode":"手动","operation":"SET","focus":"airControl","rawText":"开启手动模式"}
                    ACManager.getInstance().enableAirConditionerAutoMode(false);
                    break;

                case "除霜"://{"mode":"除霜","operation":"SET","focus":"airControl","rawText":"开启除霜模式"}
                    ACManager.getInstance().enableAirConditionerDefrost(DEFROST_MODE_FRONT, true);
                    break;

                case "除雾":
//                    ACManager.getInstance().setFrontWindshieldAutoDeforst(AUTO_DEFORST_SENSITIVITY_NORMAL);
                    break;

                default:

                    break;
            }
        }

        if (temperature != null) {
            switch (temperature) {
                case "+"://{"operation":"SET","temperature":"+","focus":"airControl","rawText":"有点冷"}
                    setAirTemperatureUp();
                    break;
                case "-"://{"operation":"SET","temperature":"-","focus":"airControl","rawText":"有点热"}
                    setAirTemperatureDown();
                    break;
                case "30": //{"device":"空调","operation":"SET","temperature":"30","focus":"airControl","rawText":"空调温度调到最高"}

                    break;
                case "16"://{"device":"空调","operation":"SET","temperature":"16","focus":"airControl","rawText":"空调温度调到最低"}

                    break;
                default:

                    break;
            }
        }

        if (fanSpeed != null) {
            switch (fanSpeed) {
                case "+"://{"fan_speed":"+","operation":"SET","focus":"airControl","rawText":"风速大一点"}
                    ACManager.getInstance().setAirConditionerFanIncOrDec(true);
                    break;
                case "-"://{"fan_speed":"-","operation":"SET","focus":"airControl","rawText":"风速小一点"}
                    ACManager.getInstance().setAirConditionerFanIncOrDec(false);
                    break;
                default:

                    break;
            }
        }
    }

    private void setAirTemperatureUp() {
        int airConditionerTemp = ACManager.getInstance().getAirConditionerTemp(SIDE_FL);
        Log.d(TAG, "setAirTemperatureUp: " + airConditionerTemp);

        ACManager.getInstance().setAirConditionerTemp(SIDE_FL, airConditionerTemp + 50);
    }

    private void setAirTemperatureDown() {
        int airConditionerTemp = ACManager.getInstance().getAirConditionerTemp(SIDE_FL);
        Log.d(TAG, "setAirTemperatureDown: " + airConditionerTemp);

        ACManager.getInstance().setAirConditionerTemp(SIDE_FL, airConditionerTemp - 50);
    }
}
