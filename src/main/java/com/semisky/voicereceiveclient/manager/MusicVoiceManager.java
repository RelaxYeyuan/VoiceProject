package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.constant.AppConstant;
import com.semisky.voicereceiveclient.jsonEntity.MusicEntity;
import com.semisky.voicereceiveclient.utils.ToolUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_BTMUSIC;
import static com.semisky.voicereceiveclient.constant.AppConstant.CLS_MEDIA_MUSIC;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_BTMUSIC;
import static com.semisky.voicereceiveclient.constant.AppConstant.PKG_MEDIA;

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
    private Context mContext;

    public MusicVoiceManager() {
    }

    /**
     * 多媒体播放都是 operation = play
     * 当song | artist 不为空时说明有指定的播放需求
     *
     * @param musicEntity
     */
    public int setActionJson(Context context, MusicEntity musicEntity) {
        mContext = context;
        try {
            String operation = musicEntity.getOperation();
            String song = musicEntity.getSong();
            String artist = musicEntity.getArtist();
            String category = musicEntity.getCategory();
            String source = musicEntity.getSource();
            if (category != null && category.equals("音乐列表")) {
                if (checkDisk()) {
                    //打开音乐列表
                    AidlManager.getInstance().getUsbMusicListener().openMusicList();
                    return AppConstant.MUSIC_TYPE_SUCCESS;
                } else {
                    return AppConstant.MUSIC_TYPE_DISK_MISSING;
                }
            }
            switch (operation) {
                case "PLAY":
                    //{"operation":"PLAY","source":"蓝牙音乐","focus":"music","rawText":"打开蓝牙音乐"}
                    //{"operation":"PLAY","source":"本地","focus":"music","rawText":"打开本地歌曲"}
                    if (source.equals("蓝牙音乐")) {
                        startActivity(PKG_BTMUSIC, CLS_BTMUSIC);
                        return AppConstant.MUSIC_TYPE_SUCCESS;
                    } else if (source.equals("本地")) {
                        if (checkDisk()) {
                            startActivity(PKG_MEDIA, CLS_MEDIA_MUSIC);
                            return AppConstant.MUSIC_TYPE_SUCCESS;
                        } else {
                            return AppConstant.MUSIC_TYPE_DISK_MISSING;
                        }
                    }
                    if (checkDisk()) {
                        if (song != null && artist != null) {//根据歌名加歌手播放
                            AidlManager.getInstance().getUsbMusicListener().playByArtistAndSong(artist, song);
                            Log.d(TAG, "根据歌名加歌手播放: ");
                        } else if (song != null) {//根据歌名播放
                            AidlManager.getInstance().getUsbMusicListener().playBySong(song);
                            Log.d(TAG, "根据歌名播放: ");
                        } else if (artist != null) {//根据歌手播放
                            AidlManager.getInstance().getUsbMusicListener().playByArtist(artist);
                            Log.d(TAG, "根据歌手播放: ");
                        } else {//没特定要求 打开USB音乐 随便听首歌
                            AidlManager.getInstance().getUsbMusicListener().play();
                            Log.d(TAG, "没特定要求音乐: ");
                        }
                        return AppConstant.MUSIC_TYPE_SUCCESS;
                    } else {
                        return AppConstant.MUSIC_TYPE_FAIL;
                    }
                default:
                    return AppConstant.MUSIC_TYPE_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppConstant.MUSIC_TYPE_FAIL;
    }

    private boolean checkDisk() {
        return ToolUtils.isSdOrUsbMounted(mContext, "/storage/udisk");
    }

    private void startActivity(String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
