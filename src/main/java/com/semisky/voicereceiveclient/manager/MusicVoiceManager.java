package com.semisky.voicereceiveclient.manager;

import android.content.Intent;
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
                int statue = checkMediaPlay();
                if (statue == AppConstant.MUSIC_TYPE_SUCCESS) {
                    //打开音乐列表
                    AidlManager.getInstance().getUsbMusicListener().openMusicList();
                    return statue;
                } else {
                    return statue;
                }
            }

            //{"category":"我的收藏","operation":"PLAY","focus":"music","rawText":"播放收藏的歌曲"}
            //{"category":"我的收藏","operation":"PLAY","focus":"music","rawText":"打开我的收藏"}
            if (category != null && category.equals("我的收藏")) {
                if (ToolUtils.isNetworkAvailable()) {
                    Log.d(TAG, "setActionJson: 我的收藏");
                    return AppConstant.MUSIC_TYPE_FAIL;
                } else {
                    return AppConstant.MUSIC_TYPE_NOT_CONNECTED;
                }
            }

            //{"artist":"刘德华","operation":"","focus":"music","rawText":"刘德华的专辑。"}
            if (operation.equals("")) {
                if (album != null) {
                    if (checkDisk()) {
                        if (checkLoadData()) {
                            if (checkMediaService()) {
                                AidlManager.getInstance().getUsbMusicListener().playByAlbum(album);
                                Log.d(TAG, "本地音乐专辑播放: ");
                                return AppConstant.MUSIC_TYPE_SUCCESS;
                            } else {
                                return AppConstant.MUSIC_TYPE_SERVICE_STATUS;
                            }
                        } else {
                            return AppConstant.MUSIC_TYPE_DISK_LOAD_DATA;
                        }
                    } else {
                        //判断是否网络连接
                        if (ToolUtils.isNetworkAvailable()) {
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
                                    if (checkLoadData()) {
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
                    }

                    //{"artist":"刘德华","operation":"PLAY","focus":"music","rawText":"来一首刘德华的歌曲"}
                    //{"artist":"刘德华","operation":"","focus":"music","rawText":"刘德华的专辑。"}
                    //{"album":"你到底有没有爱过我","operation":"PLAY","focus":"music","rawText":"我想听专辑,你到底有没有爱过我?"}
                    //{"artist":"刘德华","operation":"PLAY","focus":"music","rawText":"打开刘德华的专辑"}
                    //专辑播放
                    if (album != null) {
                        if (checkDisk()) {
                            if (checkLoadData()) {
                                if (checkMediaService()) {
                                    AidlManager.getInstance().getUsbMusicListener().playByAlbum(album);
                                    Log.d(TAG, "本地音乐专辑播放: ");
                                    return AppConstant.MUSIC_TYPE_SUCCESS;
                                } else {
                                    return AppConstant.MUSIC_TYPE_SERVICE_STATUS;
                                }
                            } else {
                                return AppConstant.MUSIC_TYPE_DISK_LOAD_DATA;
                            }
                        } else {
                            //判断是否网络连接
                            if (ToolUtils.isNetworkAvailable()) {
                                Log.d(TAG, "网络音乐专辑播放: ");
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
                        if (checkLoadData()) {
                            if (checkMediaService()) {
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
                                return AppConstant.MUSIC_TYPE_SERVICE_STATUS;
                            }
                        } else {
                            return AppConstant.MUSIC_TYPE_DISK_LOAD_DATA;
                        }
                    } else {
                        //判断是否网络连接
                        if (ToolUtils.isNetworkAvailable()) {
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

    private static String type;

    public void setResultCode(int resultCode) {
        Log.d(TAG, "setResultCode: " + resultCode);
        Log.d(TAG, "type: " + type);
        //判断是否网络连接
        switch (resultCode) {
            case AppConstant.RESULT_FAIL:
                if (ToolUtils.isNetworkAvailable()) {
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
}
