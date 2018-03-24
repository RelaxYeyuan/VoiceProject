package com.semisky.voicereceiveclient.jsonEntity;

/**
 * Created by chenhongrui on 2018/3/9
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class AirControlEntity extends BaseEntity {


    /**
     * focus : airControl
     * rawText : 升高空调温度
     * operation : SET
     * temperature : 26
     * device : 空调
     */

    private String operation;
    private String temperature;
    private String device;
    private String mode;
    private String fan_speed;
    private String airflow_direction;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFan_speed() {
        return fan_speed;
    }

    public void setFan_speed(String fan_speed) {
        this.fan_speed = fan_speed;
    }

    public String getAirflow_direction() {
        return airflow_direction;
    }

    public void setAirflow_direction(String airflow_direction) {
        this.airflow_direction = airflow_direction;
    }
}
