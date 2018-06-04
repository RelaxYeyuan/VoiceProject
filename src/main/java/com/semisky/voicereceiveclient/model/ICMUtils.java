package com.semisky.voicereceiveclient.model;

import android.util.Log;

import com.semisky.autoservice.manager.ICMManager;

/**
 * Created by chenhongrui on 2018/6/4
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class ICMUtils {

    private static final String TAG = "ICMUtils";

    public static void setCurrentPlayStatusTrue() {
        Log.d(TAG, "setCurrentPlayStatusTrue: ");
        ICMManager.getInstance().setCurrentPlayStatus(true);
    }

    public static void setCurrentPlayStatusFalse() {
        Log.d(TAG, "setCurrentPlayStatusFalse: ");
        ICMManager.getInstance().setCurrentPlayStatus(false);
    }
}
