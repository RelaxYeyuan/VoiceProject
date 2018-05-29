package com.semisky.voicereceiveclient.model;

import android.util.Log;

import com.semisky.autoservice.manager.ICMManager;

import cn.kuwo.autosdk.api.KWAPI;
import cn.kuwo.autosdk.api.OnPlayerStatusListener;
import cn.kuwo.autosdk.api.PlayState;
import cn.kuwo.autosdk.api.PlayerStatus;
import cn.kuwo.base.bean.Music;

/**
 * Created by chenhongrui on 2018/4/3
 * <p>
 * 内容摘要：
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 * 在线音乐：
 * 1.指定歌手播放歌曲
 * 2.指定歌名播放歌曲
 * 3.指定歌名和歌手播放歌曲
 * 4.随便听首歌
 * 5.暂停，播放，上/下一曲，进入/退出应用
 */
public class KWMusicAPI {

    private static final String TAG = "KWMusicAPI";

    private KWAPI mKwapi;

    public KWMusicAPI() {
        mKwapi = KWAPI.getKWAPI();
    }

    //根据歌手播放音乐
    public void playByArtist(String artist) {
        mKwapi.playClientMusics(null, artist, null);
    }

    //根据歌名播放音乐
    public void playBySong(String song) {
        mKwapi.playClientMusics(song, null, null);
    }

    //根据歌手和歌名播放音乐
    public void playByArtistAndSong(String artist, String song) {
        mKwapi.playClientMusics(song, artist, null);
    }

    //根据专辑播放音乐
    public void playByAlbum(String album) {
        mKwapi.playClientMusics(null, null, album);
    }

    //随便播放
    public void randomPlayMusic() {
        mKwapi.randomPlayMusic();
    }

    //播放
    public void play() {
        mKwapi.setPlayState(PlayState.STATE_PLAY);
    }

    //暂停
    public void pause() {
        mKwapi.setPlayState(PlayState.STATE_PAUSE);
    }

    //上一曲
    public void lastMusic() {
        mKwapi.setPlayState(PlayState.STATE_PRE);
    }

    //下一曲
    public void nextMusic() {
        mKwapi.setPlayState(PlayState.STATE_NEXT);
    }

    //进入酷我
    public void startApp() {
        //true 是否自动播放
        mKwapi.startAPP(true);
    }

    //退出酷我
    public void exitApp() {
        mKwapi.exitAPP();
    }

    /**
     * " 歌名：" + music.name;
     * " 歌手：" + music.artist;
     * " 专辑：" + music.album;
     */
    public void registerPlayerStatusListener() {
        mKwapi.registerPlayerStatusListener(new OnPlayerStatusListener() {

            @Override
            public void onPlayerStatus(PlayerStatus arg0, Music music) {
                if (music != null) {
                    ICMManager.getInstance().setCurrentSourceName(music.name);
                    Log.d(TAG, "onPlayerStatus: " + music.name);
                }
            }
        });
    }
}
