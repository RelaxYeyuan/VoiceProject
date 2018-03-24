package com.semisky.voicereceiveclient.manager;

import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.jsonEntity.CMDEntity;

/**
 * Created by chenhongrui on 2018/3/8
 * <p>
 * 内容摘要：用于CMD相关操作 focus = cmd
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class CMDVoiceManager {

    private static final String TAG = "CMDVoiceManager";

    public CMDVoiceManager() {
    }

    public void setActionJson(CMDEntity cmdEntity) {
        String category = cmdEntity.getCategory();
        String name = cmdEntity.getName();
        String nameValue = cmdEntity.getNameValue();

        try {
            if (name.equals("切换模式")) {
//            AidlManager.getInstance().getSystemListener().changeLightMode();
            } else if (name.equals("返回主菜单")) {
                AidlManager.getInstance().getRadioListener().backLauncher();
            }

            switch (category) {
                case "收音机控制":
                    radioControl(name);
                    break;
                case "收藏":
                    AidlManager.getInstance().getRadioListener().radioPlayCollect();
                    break;
                case "屏幕控制":
                    setControl(name);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void radioControl(String name) {
        try {
            switch (name) {
                case "上一频道":
                    AidlManager.getInstance().getRadioListener().seekUp();
                    break;
                case "下一频道":
                    AidlManager.getInstance().getRadioListener().seekDown();
                    break;
                case "收藏本台":
                    AidlManager.getInstance().getRadioListener().collectFreq();
                    break;
                case "扫描电台":
                    AidlManager.getInstance().getRadioListener().seekFreq();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setControl(String name) {
        try {
            switch (name) {
                case "亮度+":
                    AidlManager.getInstance().getSystemListener().raiseLight();
                    break;
                case "亮度-":
                    AidlManager.getInstance().getSystemListener().lowerLight();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
