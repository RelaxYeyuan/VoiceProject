package com.semisky.voicereceiveclient.utils;

import com.google.gson.Gson;
import com.semisky.voicereceiveclient.jsonEntity.VoiceEntity;

/**
 * Created by chenhongrui on 2018/1/29
 * <p>
 * 内容摘要：GSON解析
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class ParseJsonUtils {

    /**
     * 解析字符串
     *
     * @param responseData 字符串
     */
    public static VoiceEntity parseJson(String responseData) {
        Gson gson = new Gson();
        return gson.fromJson(responseData, VoiceEntity.class);
    }
}
