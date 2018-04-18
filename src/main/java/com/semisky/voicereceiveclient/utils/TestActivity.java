package com.semisky.voicereceiveclient.utils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.semisky.autoservice.manager.ACManager;
import com.semisky.autoservice.manager.CarCtrlManager;
import com.semisky.voicereceiveclient.R;

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

public class TestActivity extends Activity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void 打开AC模式(View view) {
        ACManager.getInstance().setAirConditionerAC(true);//开启空调
    }

    public void 开启内循环模式(View view) {
        ACManager.getInstance().setAirConditionerCirMode(CIR_MODE_INNER);
    }

    public void 开启外循环模式(View view) {
        ACManager.getInstance().setAirConditionerCirMode(CIR_MODE_OUTSIDE);
    }

    public void 吹面(View view) {
        ACManager.getInstance().setAirConditionerWindExitMode(WIND_EXIT_MODE_FACE);
    }

    public void 吹面吹脚(View view) {
        ACManager.getInstance().setAirConditionerWindExitMode(WIND_EXIT_MODE_FACE_FOOT);
    }

    public void 吹脚(View view) {
        ACManager.getInstance().setAirConditionerWindExitMode(WIND_EXIT_MODE_FOOT);
    }

    public void 前除霜(View view) {
        ACManager.getInstance().enableAirConditionerDefrost(DEFROST_MODE_FRONT, true);
    }

    public void 后除霜(View view) {
        ACManager.getInstance().enableAirConditionerDefrost(DEFROST_MODE_REAR, true);
    }

    public void 打开左前车窗大一点(View view) {
        CarCtrlManager.getInstance().setSideWindow(POSITION_FL, STATE_OPEN_80);
    }

    public void 打开左前车窗小一点(View view) {
        CarCtrlManager.getInstance().setSideWindow(POSITION_FL, STATE_CLOSE_80);
    }

    public void finishActivity(View view) {
        finish();
    }

    public void 打开近光灯(View view) {
        CarCtrlManager.getInstance().setLampStatus(TYPE_WIDTH_LAMP, STATUS_OPEN);
    }

    public void 关闭近光灯(View view) {
        CarCtrlManager.getInstance().setLampStatus(TYPE_WIDTH_LAMP, STATUS_CLOSE);
    }

    public void 打开天窗(View view) {
        CarCtrlManager.getInstance().setSkyWindow(SKY_WINDOW_OPEN);
    }

    public void 关闭天窗(View view) {
        CarCtrlManager.getInstance().setSkyWindow(SKY_WINDOW_CLOSE);
    }

    public void 翘起天窗(View view) {
        CarCtrlManager.getInstance().setSkyWindow(SKY_WINDOW_RISE);
    }

    public void 打开左前车窗(View view) {
        CarCtrlManager.getInstance().setSideWindow(POSITION_FL, STATUS_OPEN);
    }

    public void 打开右前车窗(View view) {
        CarCtrlManager.getInstance().setSideWindow(POSITION_FR, STATUS_OPEN);
    }

    public void 打开左后车窗(View view) {
        CarCtrlManager.getInstance().setSideWindow(POSITION_RL, STATUS_OPEN);
    }

    public void 打开右后车窗(View view) {
        CarCtrlManager.getInstance().setSideWindow(POSITION_RR, STATUS_OPEN);
    }

    public void 关闭左前车窗(View view) {
        CarCtrlManager.getInstance().setSideWindow(POSITION_FL, STATUS_CLOSE);
    }

    public void 关闭右前车窗(View view) {
        CarCtrlManager.getInstance().setSideWindow(POSITION_FR, STATUS_CLOSE);
    }

    public void 关闭左后车窗(View view) {
        CarCtrlManager.getInstance().setSideWindow(POSITION_RL, STATUS_CLOSE);
    }

    public void 关闭右后车窗(View view) {
        CarCtrlManager.getInstance().setSideWindow(POSITION_RR, STATUS_CLOSE);
    }

    public void 打开空调(View view) {
        ACManager.getInstance().setAirConditionerWorking(AIR_WORKING_ON);
    }

    public void 关闭空调(View view) {
        ACManager.getInstance().setAirConditionerWorking(AIR_WORKING_OFF);
    }

    public void 温度加一(View view) {
        setAirTemperatureUp();
    }

    public void 温度减一(View view) {
        setAirTemperatureDown();
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

    public void 温度最高(View view) {
        ACManager.getInstance().setAirConditionerTemp(SIDE_FL, 17);
    }

    public void 温度最低(View view) {
        ACManager.getInstance().setAirConditionerTemp(SIDE_FL, 1);
    }

    public void 风速加一(View view) {
        setFanSpeedUp();
    }

    public void 风速减一(View view) {
        setFanSpeedDown();
    }

    public void 风速最高(View view) {
        ACManager.getInstance().setAirConditionerWindValue(7);
    }

    public void 风速最低(View view) {
        ACManager.getInstance().setAirConditionerWindValue(1);
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
