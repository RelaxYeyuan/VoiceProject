package com.semisky.voicereceiveclient.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ximalaya.speechcontrol.SpeechControler;

/**
 * Created by chenhongrui on 2018/4/17
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class RadioOnlineAPI {

    private SpeechControler controler;
    private Context mContext;

    public RadioOnlineAPI(Context context) {
        controler = SpeechControler.getInstance(context);
        mContext = context;
    }

    public void startApp() {
        Intent intent = new Intent();
        intent.setData(Uri.parse("tingcar://open"));
        mContext.startActivity(intent);
    }

    public void quitApp() {
        controler.stopAndExitApp();
    }

    public void play() {
        controler.play();
    }

    public void pause() {
        controler.pause();
    }
}
