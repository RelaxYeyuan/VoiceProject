package com.semisky.voicereceiveclient.model;

import android.content.Context;
import android.util.Log;

import com.semisky.autoservice.manager.ICMManager;
import com.ximalaya.speechcontrol.SpeechControler;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

/**
 * Created by chenhongrui on 2018/5/29
 * <p>
 * 内容摘要: 喜马拉雅
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class XMLYApi {

    private static final String TAG = "XMLYApi";

    private SpeechControler controler;
    private Context context;

    public XMLYApi(Context context) {
        this.context = context;
        controler = SpeechControler.getInstance(context);
    }

    public void openApp() {
        controler.openAppAndContinuePlay(context);
    }

    public void exitApp(){
        controler.stopAndExitApp();
    }

    public void addPlayerStatusListener(){
        Log.d(TAG, "addPlayerStatusListener: ");
        controler.addPlayerStatusListener(new IXmPlayerStatusListener() {
            @Override
            public void onPlayStart() {
                Log.d(TAG, "onPlayStart: ");
                ICMUtils.setCurrentPlayStatusTrue();
            }

            @Override
            public void onPlayPause() {
                Log.d(TAG, "onPlayPause: ");
                ICMUtils.setCurrentPlayStatusFalse();
            }

            @Override
            public void onPlayStop() {
                Log.d(TAG, "onPlayStop: ");
                ICMUtils.setCurrentPlayStatusFalse();
            }

            @Override
            public void onSoundPlayComplete() {
                Log.d(TAG, "onSoundPlayComplete: ");
            }

            @Override
            public void onSoundPrepared() {
                Log.d(TAG, "onSoundPrepared: ");
            }

            @Override
            public void onSoundSwitch(PlayableModel playableModel, PlayableModel curModel) {
                Log.d(TAG, "onSoundSwitch: ");
                if (curModel instanceof Track) {
                    String trackTitle = ((Track) curModel).getTrackTitle();
                    Log.d(TAG, "onSoundSwitch: " + trackTitle);
                    ICMManager.getInstance().setCurrentSourceName(trackTitle);
                } else if (curModel instanceof Radio) {
                    String trackTitle = ((Radio) curModel).getProgramName();
                    Log.d(TAG, "onSoundSwitch: " + trackTitle);
                    ICMManager.getInstance().setCurrentSourceName(trackTitle);
                }
            }

            @Override
            public void onBufferingStart() {
            }

            @Override
            public void onBufferingStop() {
            }

            @Override
            public void onBufferProgress(int i) {
            }

            @Override
            public void onPlayProgress(int i, int i1) {
            }

            @Override
            public boolean onError(XmPlayerException e) {
                return false;
            }
        });
    }
}
