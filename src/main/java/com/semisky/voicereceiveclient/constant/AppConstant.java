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

    /*车辆设置*/
    public static final String PKG_CAR_SETTINGS = "com.semisky.autovehicalsetting";
    public static final String CLS_CAR_SETTINGS = "com.semisky.autovehicalsetting.VehicalSettingActivity";

    /*帮助手册*/
    public static final String PKG_HELP = "com.semisky.autoguide";
    public static final String CLS_HELP = "com.semisky.autoguide.MainActivity";

    //进入讯飞语音广播
    public static final String ACTION_START_VOICE = "com.semisky.broadcast.VOICE_START_ACTIVITY";
    public static final String START_VOICE_FLAG = "start_voice_flag";//true 进入 ; false 退出

    //通知拨打蓝牙电话
    public static final String ACTION_CALL_NUMBER = "com.semisky.cx62.ACTION_CALL_BY_NUMBER";
    public static final String CALL_NUMBER = "CALL_NUMBER";//拨打的电话号码

    //通知蓝牙电话重拨
    public static final String ACTION_CALL_REDIAL_NUMBER = "com.semisky.cx62.ACTION_CALL_REDIAL_NUMBER";

    public static final String XMLYSecret = "ff26b9d980267571270045294a702bd3";
    public static final String XMLYAppKey = "0f726e39f019cb8a6b290eda22c599e7";

    public static final int RADIO_BINDER = 1;
    public static final int USB_MUSIC_BINDER = 2;
    public static final int BT_CALL_BINDER = 3;
    public static final int BT_MUSIC_BINDER = 4;
    public static final int BT_CAR_CONTROL_BINDER = 5;
    public static final int IPOD_BINDER = 6;
    public static final int SYSTEM_CONTROL_BINDER = 7;
    public static final int CARLIFE_BINDER = 8;

    //循环播放
    public static final int LOOP_PLAY = 1;
    //单曲播放
    public static final int SINGLE_PLAY = 2;
    //随机播放
    public static final int RANDOM_PLAY = 3;

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
    public static final int MUSIC_TYPE_DISK_LOAD_DATA = 5;
    public static final int MUSIC_TYPE_SERVICE_STATUS = 6;

    //radio相关状态
    public static final int RADIO_TYPE_SUCCESS = 1;
    public static final int RADIO_TYPE_FAIL = 2;
    public static final int RADIO_TYPE_SCOPE = 3;

    //cmd相关状态
    public static final int CMD_TYPE_SUCCESS = 1;
    public static final int CMD_TYPE_FAIL = 2;

    //空调相关状态
    public static final int AIR_TYPE_SUCCESS = 1;
    public static final int AIR_TYPE_FAIL = 2;

    //车控相关状态
    public static final int CAR_TYPE_SUCCESS = 1;
    public static final int CAR_TYPE_FAIL = 2;

    //BTMusic
    public static final int BT_TYPE_NOT_CONNECTED = 7;

    //CARLIFE关闭语音
    public static final int CLOSE_VOICE_CARLIFE = 4;
    //CARLIFE唤醒语音
    public static final int WAKUP_VOICE_CARLIFE = 4;

    public class Numerical {
        public static final int FM_MAX_FREQ = 10800;
        public static final int FM_MIN_FREQ = 8750;
        public static final int FM_MAX_FREQ_INT = 108;
        public static final int FM_MIN_FREQ_INT = 87;
        public static final int AM_MAX_FREQ = 1629;
        public static final int AM_MIN_FREQ = 531;
        public static final int RADIO_BAND_DEFAULT = 1;
        public static final String FM_FREQ = "FM_FREQ";
        public static final String AM_FREQ = "AM_FREQ";
        public static final String RADIO_BAND = "RADIO_BAND";
        public static final String RADIO_BASE_FREQ = "RADIO_BASE_FREQ";
    }
}
