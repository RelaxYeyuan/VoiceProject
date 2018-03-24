package com.semisky.voicereceiveclient.jsonEntity;

/**
 * Created by chenhongrui on 2018/1/29
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BaseEntity {

    /**
     * 业务标签：RADIO MUSIC
     * 语音转写的文字 :
     *
     * focus
     * 1.app 打开应用
     */

    private String focus;
    private String rawText;

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public FocusType checkoutFocus() {
        switch (focus) {
            case "radio":
                return FocusType.RADIO;
            case "music":
                return FocusType.RADIO;
        }
        return FocusType.NULL;
    }
}
