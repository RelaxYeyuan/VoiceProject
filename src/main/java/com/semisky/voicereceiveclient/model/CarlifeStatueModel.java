package com.semisky.voicereceiveclient.model;

/**
 * Created by chenhongrui on 2018/8/8
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class CarlifeStatueModel {

    private static CarlifeStatueModel INSTANCE;

    public static CarlifeStatueModel getInstance() {
        if (INSTANCE == null) {
            synchronized (CarlifeStatueModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CarlifeStatueModel();
                }
            }
        }
        return INSTANCE;
    }

    private boolean isStartCarlife;

    public boolean isStartCarlife() {
        return isStartCarlife;
    }

    public void setStartCarlife(boolean startCarlife) {
        isStartCarlife = startCarlife;
    }
}
