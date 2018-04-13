package com.semisky.voicereceiveclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.semisky.voicereceiveclient.model.KWMusicAPI;
import com.semisky.voicereceiveclient.service.BinderPoolService;

import static com.semisky.voicereceiveclient.utils.ToolUtils.isNetworkAvailable;


/**
 * 此为测试类，用于测试和各应用的连接通讯，以及显示语义
 * 服务端操作
 * 1.创建模块所需要的aidl并实现
 * 2.创建binder池aidl并实现查询方法
 * 3.创建并启动服务
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private KWMusicAPI kwMusicAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kwMusicAPI = new KWMusicAPI();
    }

    public void playForSinger(View view) {
        //未来放到系统启动
        startService(new Intent(this, BinderPoolService.class));
        Log.d(TAG, "playForSinger: ");
    }

    public void playForName(View view) {
        kwMusicAPI.playBySong("下个路口见");
    }

    public void pause(View view) {
        kwMusicAPI.pause();
    }

    public void play(View view) {
        kwMusicAPI.play();
    }

    public void randomPlay(View view) {
        kwMusicAPI.randomPlayMusic();
    }

    public void playForSpecial(View view) {
        kwMusicAPI.playByAlbum("小幸运");
    }

    public void playForNameAndSinger(View view) {
        kwMusicAPI.playByArtistAndSong("周杰伦", "听妈妈的话");
    }

    public void finishActivity(View view) {
        finish();
    }

    public void exitApp(View view) {
        kwMusicAPI.exitApp();
    }

    public void next(View view) {
        kwMusicAPI.nextMusic();
    }

    public void startApp(View view) {
        kwMusicAPI.startApp();
    }

    public void last(View view) {
        kwMusicAPI.lastMusic();
    }

    public void isNetwork(View view) {
        Log.d(TAG, "isNetwork: " + isNetworkAvailable(this));
    }
}