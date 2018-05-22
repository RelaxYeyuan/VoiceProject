package com.semisky.voicereceiveclient.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.semisky.voicereceiveclient.appAidl.AidlManager;
import com.semisky.voicereceiveclient.constant.AppConstant;
import com.semisky.voicereceiveclient.jsonEntity.MusicEntity;
import com.semisky.voicereceiveclient.model.KWMusicAPI;
import com.semisky.voicereceiveclient.model.VoiceBTModel;
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
    private static Context mContext;
    private static KWMusicAPI kwMusicAPI;
    private static String song;
    private static String artist;

    public MusicVoiceManager(Context context) {
        kwMusicAPI = new KWMusicAPI();
        mContext = context;
    }

    /**
     * 多媒体播放都是 operation = play
     * 当song | artist 不为空时说明有指定的播放需求
     */
    public int setActionJson(MusicEntity musicEntity) {
        try {
            String operation = musicEntity.getOperation();
            song = musicEntity.getSong();
            artist = musicEntity.getArtist();
            String category = musicEntity.getCategory();
            String source = musicEntity.getSource();
            String album = musicEntity.getAlbum();
            if (category != null && category.equals("音乐列表")) {
                if (checkDisk()) {
                    //打开音乐列表
                    AidlManager.getInstance().getUsbMusicListener().openMusicList();
                    return AppConstant.MUSIC_TYPE_SUCCESS;
                } else {
                    return AppConstant.MUSIC_TYPE_DISK_MISSING;
                }
            }

            //{"artist":"刘德华","operation":"","focus":"music","rawText":"刘德华的专辑。"}
            if (operation.equals("")) {
                if (album != null) {
                    if (checkDisk()) {
                        AidlManager.getInstance().getUsbMusicListener().playByAlbum(album);
                        Log.d(TAG, "本地音乐专辑播放: ");
                        return AppConstant.MUSIC_TYPE_SUCCESS;
                    } else {
                        //判断是否网络连接
                        if (ToolUtils.isNetworkAvailable(mContext)) {
                            kwMusicAPI.playByAlbum(album);
                            return AppConstant.MUSIC_TYPE_SUCCESS;
                        } else {
                            return AppConstant.MUSIC_TYPE_NOT_CONNECTED;
                        }
                    }
                }
            }

            switch (operation) {
                case "PLAY":
                    //{"operation":"PLAY","source":"蓝牙音乐","focus":"music","rawText":"播放蓝牙音乐"}
                    //{"operation":"PLAY","source":"本地","focus":"music","rawText":"打开本地歌曲"}
                    //{"operation":"PLAY","source":"网络","focus":"music","rawText":"我想听网络音乐"}

                    //{"artist":"收藏","operation":"PLAY","focus":"music","rawText":"听收藏的频道"}
                    //{"operation":"PLAY","source":"usb","focus":"music","rawText":"打开USB音乐"}
                    if (source != null) {
                        switch (source) {
                            case "蓝牙音乐":
                                if (VoiceBTModel.getInstance().isConnectionState()) {
                                    startActivity(PKG_BTMUSIC, CLS_BTMUSIC);
                                } else {
                                    return AppConstant.BT_TYPE_NOT_CONNECTED;
                                }
                                return AppConstant.MUSIC_TYPE_SUCCESS;
                            case "本地":
                                if (checkDisk()) {
                                    startActivity(PKG_MEDIA, CLS_MEDIA_MUSIC);
                                    return AppConstant.MUSIC_TYPE_SUCCESS;
                                } else {
                                    return AppConstant.MUSIC_TYPE_DISK_MISSING;
                                }
                            case "网络":
                                kwMusicAPI.startApp();
                                return AppConstant.MUSIC_TYPE_SUCCESS;
                            case "usb":
                                if (checkDisk()) {
                                    startActivity(PKG_MEDIA, CLS_MEDIA_MUSIC);
                                    return AppConstant.MUSIC_TYPE_SUCCESS;
                                } else {
                                    return AppConstant.MUSIC_TYPE_DISK_MISSING;
                                }
                            default:
                                break;
                        }
                    }

                    //{"artist":"刘德华","operation":"PLAY","focus":"music","rawText":"来一首刘德华的歌曲"}
                    //{"artist":"刘德华","operation":"","focus":"music","rawText":"刘德华的专辑。"}
                    //{"album":"你到底有没有爱过我","operation":"PLAY","focus":"music","rawText":"我想听专辑,你到底有没有爱过我?"}
                    //专辑播放
                    if (album != null) {
                        if (checkDisk()) {
                            AidlManager.getInstance().getUsbMusicListener().playByAlbum(album);
                            Log.d(TAG, "本地音乐专辑播放: ");
                            return AppConstant.MUSIC_TYPE_SUCCESS;
                        } else {
                            //判断是否网络连接
                            if (ToolUtils.isNetworkAvailable(mContext)) {
                                Log.d(TAG, "网络电台专辑播放: ");
                                kwMusicAPI.playByAlbum(album);
                                return AppConstant.MUSIC_TYPE_SUCCESS;
                            } else {
                                return AppConstant.MUSIC_TYPE_NOT_CONNECTED;
                            }
                        }
                    }

                    /*
                     * 业务流程：我要听XX歌
                     * 1.先判断本地U盘是否连接
                     * 2.再判断网络是否正常
                     * {"operation":"PLAY","song":"你到底有没有爱过我","focus":"music","rawText":"我想听歌曲，你到底有没有爱过我?"}
                     */
                    if (checkDisk()) {
                        if (song != null && artist != null) {//根据歌名加歌手播放
                            AidlManager.getInstance().getUsbMusicListener().playByArtistAndSong(artist, song);
                            type = "1";
                            Log.d(TAG, "本地根据歌名加歌手播放: ");
                        } else if (song != null) {//根据歌名播放
                            AidlManager.getInstance().getUsbMusicListener().playBySong(song);
                            type = "2";
                            Log.d(TAG, "本地根据歌名播放: ");
                        } else if (artist != null) {//根据歌手播放
                            AidlManager.getInstance().getUsbMusicListener().playByArtist(artist);
                            type = "3";
                            Log.d(TAG, "本地根据歌手播放: ");
                        } else {//没特定要求 打开USB音乐 随便听首歌
                            AidlManager.getInstance().getUsbMusicListener().playResume();
                            Log.d(TAG, "本地没特定要求音乐: ");
                        }
                        return AppConstant.MUSIC_TYPE_SUCCESS;
                    } else {
                        //判断是否网络连接
                        if (ToolUtils.isNetworkAvailable(mContext)) {
                            if (song != null && artist != null) {//根据歌名加歌手播放
                                kwMusicAPI.playByArtistAndSong(artist, song);
                                Log.d(TAG, "网络根据歌名加歌手播放: ");
                            } else if (song != null) {//根据歌名播放
                                kwMusicAPI.playBySong(song);
                                Log.d(TAG, "网络根据歌名播放: ");
                            } else if (artist != null) {//根据歌手播放
                                kwMusicAPI.playByArtist(artist);
                                Log.d(TAG, "网络根据歌手播放: ");
                            } else {//没特定要求 打开USB音乐 随便听首歌
                                kwMusicAPI.play();
                                Log.d(TAG, "网络没特定要求音乐: ");
                            }
                            return AppConstant.MUSIC_TYPE_SUCCESS;
                        } else {
                            return AppConstant.MUSIC_TYPE_NOT_CONNECTED;
                        }
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

    private static String type;

    public static void setResultCode(int resultCode) {
        if (resultCode == AppConstant.RESULT_FAIL) {
            Log.d(TAG, "setResultCode: " + type);
            switch (type) {
                case "1":
                    kwMusicAPI.playByArtistAndSong(artist, song);
                    break;
                case "2":
                    kwMusicAPI.playBySong(song);
                    break;
                case "3":
                    kwMusicAPI.playByArtist(artist);
                    break;
            }
        } else {
            Log.d(TAG, "setResultCode: 本地音乐已处理");
        }
    }
}
