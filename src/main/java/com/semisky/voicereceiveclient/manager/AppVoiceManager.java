package com.semisky.voicereceiveclient.manager;

import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.jsonEntity.AppEntity;

/**
 * Created by chenhongrui on 2018/3/8
 * <p>
 * 内容摘要：处理应用操作 focus = app
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class AppVoiceManager {

    public AppVoiceManager() {
    }

    public void setActionJson(AppEntity actionJson) {
        String name = actionJson.getName();
        String operation = actionJson.getOperation();
        if (name != null) {
            switch (name) {
                case "收音机":
                    radioOperation(operation);
                    break;
                case "蓝牙":
                    BTCallOperation(operation);
                    break;
                case "通话记录":
                    BTCallOperation(operation);
                    break;
                case "设置":
                    setOperation(operation);
                    break;
            }
        }
    }

    private void radioOperation(String operation) {
        try {
            if (operation != null) {
                switch (operation) {
                    case "EXIT"://退出app
                        AidlManager.getInstance().getRadioListener().closeActivity();
                        break;
                    case "LAUNCH"://进入app
                        AidlManager.getInstance().getRadioListener().openActivity();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BTCallOperation(String operation) {
        try {
            if (operation != null) {
                switch (operation) {
                    case "EXIT"://断开蓝牙连接
                        AidlManager.getInstance().getSystemListener().closeBTCallConnect();
                        break;
                    case "LAUNCH"://打开蓝牙连接
                        AidlManager.getInstance().getSystemListener().openBTCallConnect();
                        break;
                    case "QUERY"://查看通话记录
                        AidlManager.getInstance().getBTCallListener().queryCallRecords();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOperation(String operation) {
        try {
            if (operation != null) {
                switch (operation) {
                    case "EXIT"://退出app
                        AidlManager.getInstance().getSystemListener().closeActivity();
                        break;
                    case "LAUNCH"://进入app
                        AidlManager.getInstance().getSystemListener().openActivity();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
