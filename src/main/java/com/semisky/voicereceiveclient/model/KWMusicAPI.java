package com.semisky.voicereceiveclient.model;

import cn.kuwo.autosdk.api.KWAPI;
import cn.kuwo.autosdk.api.PlayState;

/**
 * Created by chenhongrui on 2018/4/3
 * <p>
 * 内容摘要：${TODO}
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

    //随便播放
    public void randomPlayMusic() {
        mKwapi.randomPlayMusic();
    }

    //播放
    public void play() {
        mKwapi.setPlayState(PlayState.STATE_PLAY);
    }

    public void pause() {
        mKwapi.setPlayState(PlayState.STATE_PAUSE);
    }

    public void lastMusic() {
        mKwapi.setPlayState(PlayState.STATE_PRE);
    }

    public void nextMusic() {
        mKwapi.setPlayState(PlayState.STATE_NEXT);
    }
}
