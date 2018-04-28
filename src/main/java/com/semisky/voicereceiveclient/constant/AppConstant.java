package com.semisky.voicereceiveclient.constant;

/**
 * Created by chenhongrui on 2018/3/1
 * <p>
 * 内容摘要：常量
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class AppConstant {

    public static final String PKG_SETTINGS = "com.semisky.autosetting";
    public static final String CLS_SETTINGS = "com.semisky.autosetting.SettingActivity";

    public static final String PKG_RADIO = "com.semisky.autoradio";
    public static final String CLS_RADIO = "com.semisky.autoradio.activity.main.RadioActivity";

    public static final String PKG_MEDIA = "com.semisky.automultimedia";
    public static final String CLS_MEDIA_MUSIC = "com.semisky.automultimedia.activity.MusicPlayActivity";
    public static final String CLS_MEDIA_VIDEO = "com.semisky.automultimedia.activity.VideoPlayActivity";
    public static final String CLS_MEDIA_PICTURE = "com.semisky.automultimedia.activity.PicturePlayActivity";


    public static final String PKG_BTCALL = "com.semisky.cx62.bluetooth";
    public static final String CLS_BTCALL = "com.semisky.cx62.bluetooth.activity.Cx62BtActivityPhone";

    public static final String PKG_BTMUSIC = "com.semisky.cx62.bluetooth";
    public static final String CLS_BTMUSIC = "com.semisky.cx62.bluetooth.activity.Cx62BtActivityMusic";

    public static final String PKG_NAVI = "com.winmu.autoNavi";
    public static final String CLS_NAVI = "ritu.navi.main.Navigation";

    public static final String PKG_VOICE = "com.iflytek.cutefly.speechclient";
    public static final String CLS_VOICE = "com.iflytek.autofly.activity.SpeechActivity";

    public static final String ACTION_START_VOICE = "com.semisky.broadcast.VOICE_START_ACTIVITY";
    public static final String START_VOICE_FLAG = "start_voice_flag";

    public static final int RADIO_BINDER = 1;
    public static final int USB_MUSIC_BINDER = 2;
    public static final int BT_CALL_BINDER = 3;
    public static final int BT_MUSIC_BINDER = 4;
    public static final int BT_CAR_CONTROL_BINDER = 5;
    public static final int IPOD_BINDER = 6;
    public static final int SYSTEM_CONTROL_BINDER = 7;

    //单曲播放
    public static final int SINGLE_PLAY = 1;
    //顺序播放
    public static final int ORDER_PLAY = 2;
    //随机播放
    public static final int RANDOM_PLAY = 3;
    //循环播放
    public static final int LOOP_PLAY = 4;

    //搜索到歌曲
    public final static int RESULT_SUCCESS = 0x01;
    //搜索不到歌曲
    public final static int RESULT_FAIL = 0x02;

    public final static int RESULT_ERROR = 0x03;


    //FM
    public static final int FM_TYPE = 1;
    //AM
    public static final int AM_TYPE = 2;

    //music相关状态
    public static final int MUSIC_TYPE_SUCCESS = 1;
    public static final int MUSIC_TYPE_FAIL = 2;
    public static final int MUSIC_TYPE_DISK_MISSING = 3;
    public static final int MUSIC_TYPE_NOT_CONNECTED = 4;

    //BTMusic
    public static final int BT_TYPE_NOT_CONNECTED = 5;
}
