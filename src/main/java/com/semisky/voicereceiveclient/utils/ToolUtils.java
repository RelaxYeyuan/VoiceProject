package com.semisky.voicereceiveclient.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    /**
     * 判断网络情况
     *
     * @param context 上下文
     * @return false 表示没有网络 true 表示有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();
            if (net_info != null) {
                for (NetworkInfo aNet_info : net_info) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (aNet_info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
