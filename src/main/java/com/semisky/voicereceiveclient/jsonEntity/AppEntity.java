package com.semisky.voicereceiveclient.jsonEntity;

/**
 * Created by chenhongrui on 2018/3/9
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class AppEntity extends BaseEntity{

    /**
     * focus : app
     * rawText : 打开导航
     * operation : LAUNCH
     * name : 导航
     */

    private String operation;
    private String name;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
