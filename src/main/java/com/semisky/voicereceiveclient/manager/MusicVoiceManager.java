package com.semisky.voicereceiveclient.manager;

import android.util.Log;

import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.jsonEntity.MusicEntity;

/**
 * Created by chenhongrui on 2018/3/8
 * <p>
 * 内容摘要：处理多媒体操作 focus = music
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class MusicVoiceManager {

    private static final String TAG = "MusicVoiceManager";

    public MusicVoiceManager() {
    }

    /**
     * 多媒体播放都是 operation = play
     * 当song | artist 不为空时说明有指定的播放需求
     *
     * @param musicEntity
     */
    public void setActionJson(MusicEntity musicEntity) {
        String operation;
        try {
            operation = musicEntity.getOperation();
            String song = musicEntity.getSong();
            String artist = musicEntity.getArtist();
            String category = musicEntity.getCategory();
            if (category != null && category.equals("音乐列表")) {
                //打开音乐列表
                AidlManager.getInstance().getUsbMusicListener().openMusicList();
            }
            if (operation != null) {
                switch (operation) {
                    case "PLAY":
                        if (song != null && artist != null) {//根据歌名加歌手播放
                            AidlManager.getInstance().getUsbMusicListener().playByArtistAndSong(artist, song);
                            Log.d(TAG, "根据歌名加歌手播放: ");
                        } else if (song != null) {//根据歌名播放
                            AidlManager.getInstance().getUsbMusicListener().playBySong(song);
                            Log.d(TAG, "根据歌名播放: ");
                        } else if (artist != null) {//根据歌手播放
                            AidlManager.getInstance().getUsbMusicListener().playByArtist(artist);
                            Log.d(TAG, "根据歌手播放: ");
                        } else {//没特定要求
                            AidlManager.getInstance().getUsbMusicListener().play();
                            Log.d(TAG, "没特定要求音乐: ");
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
