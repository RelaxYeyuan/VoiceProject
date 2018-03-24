package com.semisky.voicereceiveclient.jsonEntity;

/**
 * Created by chenhongrui on 2018/3/9
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class CMDEntity extends BaseEntity{


    /**
     * category : 音量控制
     * name : 音量调节
     * nameValue : 30
     * focus : cmd
     * rawText : 声音设置为30
     */

    private String category;
    private String name;
    private String nameValue;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameValue() {
        return nameValue;
    }

    public void setNameValue(String nameValue) {
        this.nameValue = nameValue;
    }
}
