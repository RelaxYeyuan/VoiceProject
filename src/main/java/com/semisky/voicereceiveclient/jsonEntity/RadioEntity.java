package com.semisky.voicereceiveclient.jsonEntity;

/**
 * Created by chenhongrui on 2018/3/28
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class RadioEntity extends BaseEntity {


    /**
     * waveband : fm
     * code : 90.8
     */

    private String waveband;
    private String code;

    public String getWaveband() {
        return waveband;
    }

    public void setWaveband(String waveband) {
        this.waveband = waveband;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
