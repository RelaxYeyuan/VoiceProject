package com.semisky.voicereceiveclient.manager;

import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.semisky.autoservice.manager.AutoConstants;
import com.semisky.autoservice.manager.AutoManager;
import com.semisky.voicereceiveclient.BaseApplication;
import com.semisky.voicereceiveclient.R;
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
    private static KWMusicAPI kwMusicAPI;
    private static String song;
    private static String artist;
    private String album;
    private MusicType musicType;

    private MusicVoiceManager() {
        kwMusicAPI = new KWMusicAPI();
    }

    private static class SingletonHolder {
        private static final MusicVoiceManager INSTANCE = new MusicVoiceManager();
    }

    public static MusicVoiceManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 多媒体播放都是 operation = play
     */
    public int setActionJson(MusicEntity musicEntity) {
        try {
            String operation = musicEntity.getOperation();
            song = musicEntity.getSong();
            artist = musicEntity.getArtist();
            String category = musicEntity.getCategory();
            String source = musicEntity.getSource();
            album = musicEntity.getAlbum();

            if (category != null) {
                switch (category) {
                    case "音乐列表":
                    case "播放列表":
                        int statue = checkMediaPlay();
                        if (statue == AppConstant.MUSIC_TYPE_SUCCESS) {
                            //打开音乐列表
                            Log.d(TAG, "setActionJson: 打开音乐列表");
                            AidlManager.getInstance().getUsbMusicListener().openMusicList();
                            return statue;
                        } else {
                            return statue;
                        }
                    case "我的收藏":
                        //{"category":"我的收藏","operation":"PLAY","focus":"music","rawText":"播放收藏的歌曲"}
                        //{"category":"我的收藏","operation":"PLAY","focus":"music","rawText":"打开我的收藏"}
                        if (ToolUtils.isNetworkAvailable()) {
                            Log.d(TAG, "setActionJson: 我的收藏");
                            return AppConstant.MUSIC_TYPE_FAIL;
                        } else {
                            return AppConstant.MUSIC_TYPE_NOT_CONNECTED;
                        }
                    case "category":
                        //onNLPResult{"artist":"周杰伦`陈奕迅","category":"合唱","operation":"SEARCH",
                        // "focus":"music","rawText":"查询周杰伦和陈奕迅的合唱。
                        // onNLPResult{"artist":"周杰伦`陈奕迅","category":"合唱","operation":"SEARCH"
                        // ,"song":"简单爱","focus":"music","rawText":"查询周杰伦和陈奕迅的简单爱。"}
                        checkPlaySong();
                }

            }

            switch (operation) {
                case "PLAY":
                    //{"operation":"PLAY","source":"蓝牙音乐","focus":"music","rawText":"播放蓝牙音乐"}
                    //{"operation":"PLAY","source":"本地","focus":"music","rawText":"打开本地歌曲"}
                    //{"operation":"PLAY","source":"本地","focus":"music","rawText":"播放本地音乐"}
                    //{"operation":"PLAY","source":"网络","focus":"music","rawText":"我想听网络音乐"}

                    //{"artist":"收藏","operation":"PLAY","focus":"music","rawText":"听收藏的频道"}
                    //{"operation":"PLAY","source":"usb","focus":"music","rawText":"打开USB音乐"}

                    //{"operation":"PLAY","focus":"music","rawText":"播放音乐。"}
                    if (source != null) {
                        return openSource(source);
                    }

                    //{"artist":"刘德华","operation":"PLAY","focus":"music","rawText":"来一首刘德华的歌曲"}
                    //{"artist":"刘德华","operation":"","focus":"music","rawText":"刘德华的专辑。"} TODO 应该返回album
                    //{"album":"你到底有没有爱过我","operation":"PLAY","focus":"music","rawText":"我想听专辑,你到底有没有爱过我?"}
                    //{"artist":"刘德华","operation":"PLAY","focus":"music","rawText":"打开刘德华的专辑"}
                    //专辑播放

                    /*
                     * 业务流程：我要听XX歌
                     * 1.先判断本地U盘是否连接
                     * 2.再判断网络是否正常
                     * {"operation":"PLAY","song":"你到底有没有爱过我","focus":"music","rawText":"我想听歌曲，你到底有没有爱过我?"}
                     */
                    checkPlaySong();
                case "":
                    if (source != null) {
                        return openSource(source);
                    }

                    //onNLPResult{"operation":"","song":"忘情水","focus":"music","rawText":"忘情水。"}
                    //onNLPResult{"artist":"周杰伦","operation":"","focus":"music","rawText":"周杰伦的歌。"}
                    return checkPlaySong();
                case "SEARCH":
                    //onNLPResult{"operation":"SEARCH","song":"忘情水","focus":"music","rawText":"查询忘情水。"}
                    //onNLPResult{"artist":"周杰伦","operation":"SEARCH","focus":"music","rawText":"查找周杰伦的歌。"}
                    //onNLPResult{"artist":"周杰伦","operation":"SEARCH","song":"简单爱","focus":"music","rawText":"查找周杰伦的简单爱"}
                    //onNLPResult{"album":"简单爱","operation":"SEARCH","focus":"music","rawText":"查找专辑简单爱。"}
                    //onNLPResult{"album":"简单爱","artist":"周杰伦","operation":"SEARCH","focus":"music","rawText":"查找周杰伦的专辑简单爱。"}
                    return checkPlaySong();
                default:
                    return AppConstant.MUSIC_TYPE_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppConstant.MUSIC_TYPE_FAIL;
    }

    private int openSource(String source) {
        try {
            switch (source) {
                case "蓝牙音乐":
                case "bt":
                    if (VoiceBTModel.getInstance().isConnectionState()) {
                        AidlManager.getInstance().getBTMusicListener().play();
                        startActivity(PKG_BTMUSIC, CLS_BTMUSIC);
                    } else {
                        return AppConstant.BT_TYPE_NOT_CONNECTED;
                    }
                    return AppConstant.MUSIC_TYPE_SUCCESS;
                case "本地":
                    if (checkDisk()) {
                        if (checkLoadData()) {
                            AidlManager.getInstance().getUsbMusicListener().play();
                            startActivity(PKG_MEDIA, CLS_MEDIA_MUSIC);
                            return AppConstant.MUSIC_TYPE_SUCCESS;
                        } else {
                            return AppConstant.MUSIC_TYPE_DISK_LOAD_DATA;
                        }
                    } else {
                        return AppConstant.MUSIC_TYPE_DISK_MISSING;
                    }
                case "网络":
                    kwMusicAPI.startApp();
                    AutoManager.getInstance().setAppStatus(AutoConstants.PackageName.CLASS_KUWO,
                            BaseApplication.getContext().getString(R.string.kw_music_name),
                            AutoConstants.AppStatus.RUN_FOREGROUND);
                    return AppConstant.MUSIC_TYPE_SUCCESS;
                case "usb":
                    if (checkDisk()) {
                        if (checkLoadData()) {
                            startActivity(PKG_MEDIA, CLS_MEDIA_MUSIC);
                            return AppConstant.MUSIC_TYPE_SUCCESS;
                        } else {
                            return AppConstant.MUSIC_TYPE_DISK_LOAD_DATA;
                        }
                    } else {
                        return AppConstant.MUSIC_TYPE_DISK_MISSING;
                    }

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppConstant.MUSIC_TYPE_FAIL;
    }

    private int checkPlaySong() {
        if (checkDisk()) {
            if (checkLoadData()) {
                if (checkMediaService()) {
                    if (song != null && artist != null) {//根据歌名加歌手播放
                        playByArtAndSong();
                    } else if (artist != null && album != null) {//根据专辑名加歌手 TODO 需要加接口
                        playByAlbum();
                    } else if (song != null) {//根据歌名播放
                        playBySong();
                    } else if (artist != null) {//根据歌手播放
                        playByArtist();
                    } else if (album != null) {//根据专辑播放
                        playByAlbum();
                    } else {//没特定要求 打开USB音乐 随便听首歌
                        playResume();
                    }
                    return AppConstant.MUSIC_TYPE_SUCCESS;
                } else {
                    return playNetworkMusic();
                }
            } else {
                return playNetworkMusic();
            }
        } else {
            return playNetworkMusic();
        }
    }

    private void playByAlbum() {
        try {
            AidlManager.getInstance().getUsbMusicListener().playByAlbum(album);
            musicType = MusicType.ALBUM;
            Log.d(TAG, "本地音乐专辑播放: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void playResume() {
        try {
            AidlManager.getInstance().getUsbMusicListener().playResume();
            Log.d(TAG, "本地没特定要求音乐: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void playByArtist() {
        try {
            AidlManager.getInstance().getUsbMusicListener().playByArtist(artist);
            musicType = MusicType.ARTIST;
            Log.d(TAG, "本地根据歌手播放: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void playBySong() {
        try {
            AidlManager.getInstance().getUsbMusicListener().playBySong(song);
            musicType = MusicType.SONG;
            Log.d(TAG, "本地根据歌名播放: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void playByArtAndSong() {
        try {
            AidlManager.getInstance().getUsbMusicListener().playByArtistAndSong(artist, song);
            musicType = MusicType.ARTISTANDSONG;
            Log.d(TAG, "本地根据歌名加歌手播放: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private int playNetworkMusic() {
        //判断是否网络连接
        if (ToolUtils.isNetworkAvailable()) {
            if (song != null && artist != null) {//根据歌名加歌手播放
                kwMusicAPI.playByArtistAndSong(artist, song);
                Log.d(TAG, "网络根据歌名加歌手播放: ");
            } else if (artist != null && album != null) {//根据专辑名加歌手
                kwMusicAPI.playByArtistAndAlbum(artist, album);
                Log.d(TAG, "网络根据专辑名加歌手播放: ");
            } else if (song != null) {//根据歌名播放
                kwMusicAPI.playBySong(song);
                Log.d(TAG, "网络根据歌名播放: ");
            } else if (artist != null) {//根据歌手播放
                kwMusicAPI.playByArtist(artist);
                Log.d(TAG, "网络根据歌手播放: ");
            } else if (album != null) {
                kwMusicAPI.playByAlbum(album);
                Log.d(TAG, "网络根据专辑播放: ");
            } else {//没特定要求 打开USB音乐 随便听首歌
                kwMusicAPI.play();
                Log.d(TAG, "网络没特定要求音乐: ");
            }
            return AppConstant.MUSIC_TYPE_SUCCESS;
        } else {
            return AppConstant.MUSIC_TYPE_NOT_CONNECTED;
        }
    }

    private boolean checkDisk() {
        boolean isdOrUsbMounted = ToolUtils.isSdOrUsbMounted(BaseApplication.getContext(), "/storage/udisk");
        Log.d(TAG, "checkDisk: " + isdOrUsbMounted);
        return isdOrUsbMounted;
    }

    private boolean checkLoadData() {
        return VoiceBTModel.getInstance().isLoadData();
    }

    private boolean checkMediaService() {
        return VoiceBTModel.getInstance().isMediaService();
    }

    private int checkMediaPlay() {
        if (!checkDisk()) {
            return AppConstant.MUSIC_TYPE_DISK_MISSING;
        }

        if (!checkLoadData()) {
            return AppConstant.MUSIC_TYPE_DISK_LOAD_DATA;
        }

        if (!checkMediaService()) {
            return AppConstant.MUSIC_TYPE_SERVICE_STATUS;
        }

        return AppConstant.MUSIC_TYPE_SUCCESS;
    }

    private void startActivity(String packageName, String className) {
        Log.d(TAG, "startActivity: " + packageName);
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.getContext().startActivity(intent);
    }

    public void setResultCode(int resultCode) {
        Log.d(TAG, "setResultCode: " + resultCode);
        Log.d(TAG, "type: ");
        //判断是否网络连接
        switch (resultCode) {
            case AppConstant.RESULT_FAIL:
                if (ToolUtils.isNetworkAvailable()) {
                    switch (musicType) {
                        case ARTISTANDSONG:
                            kwMusicAPI.playByArtistAndSong(artist, song);
                            break;
                        case SONG:
                            kwMusicAPI.playBySong(song);
                            break;
                        case ARTIST:
                            kwMusicAPI.playByArtist(artist);
                            break;
                        case ALBUM:
                            kwMusicAPI.playByAlbum(album);
                            break;
                    }
                } else {
                    Log.d(TAG, "setResultCode: 网络未连接");
                    kwMusicAPI.startApp();
                    AutoManager.getInstance().setAppStatus(AutoConstants.PackageName.CLASS_KUWO,
                            BaseApplication.getContext().getString(R.string.kw_music_name),
                            AutoConstants.AppStatus.RUN_FOREGROUND);
                }
                break;
            case AppConstant.RESULT_SUCCESS:
                Log.d(TAG, "setResultCode: 本地音乐已处理");
                break;
            case AppConstant.RESULT_ERROR:
                Log.d(TAG, "setResultCode: RESULT_ERROR");
                break;
        }
    }

    private enum MusicType {
        ARTISTANDSONG,
        SONG,
        ARTIST,
        ALBUM
    }


}
