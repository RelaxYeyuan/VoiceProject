package com.semisky.voicereceiveclient.utils;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by chenhongrui on 2018/3/26
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class ToolUtils {

    private static final String TAG = "ToolUtils";

    /**
     * 利用反射判断是否挂载了外部存储
     *
     * @param context
     * @param storagePath 挂载路径
     * @return
     */
    public static boolean isSdOrUsbMounted(Context context, String storagePath) {
        Log.d("LauncherBusiness", "isSdOrUsbMounted() storagePath: " + storagePath);
        boolean isMounted = false;
        if (storagePath != null && new File(storagePath).exists()) {
            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            try {
                Method getState = sm.getClass().getMethod("getVolumeState", String.class);
                String state = (String) getState.invoke(sm, storagePath);
                Log.d("LauncherBusiness", "isSdOrUsbMounted() storagePath: " + storagePath + ",state: " + state);
                if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                    isMounted = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isMounted;
    }
}
