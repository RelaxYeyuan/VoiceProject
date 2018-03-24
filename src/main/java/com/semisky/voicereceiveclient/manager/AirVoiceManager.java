package com.semisky.voicereceiveclient.manager;

import com.semisky.voicereceiveclient.jsonEntity.AirControlEntity;

/**
 * Created by chenhongrui on 2018/3/9
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class AirVoiceManager {

    public void setActionJson(AirControlEntity airEntity) {
        String airflow_direction = airEntity.getAirflow_direction();
        String device = airEntity.getDevice();
        String fan_speed = airEntity.getFan_speed();
        String mode = airEntity.getMode();
        String operation = airEntity.getOperation();
        String temperature = airEntity.getTemperature();


    }
}
