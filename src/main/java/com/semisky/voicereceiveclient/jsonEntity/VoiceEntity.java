package com.semisky.voicereceiveclient.jsonEntity;

/**
 * Created by chenhongrui on 2018/1/29
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class VoiceEntity extends BaseEntity {

    /**
     * focus : radio
     * rawText : 我想听安徽省新闻广播
     * location : 安徽省
     * category : 新闻
     * name : 电台名称
     * waveband : 取值：【fm/am】
     * code : 电台频点
     */

    private String location;
    private String category;
    private String name;
    private String code;
    private String waveband;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWaveband() {
        return waveband;
    }

    public void setWaveband(String waveband) {
        this.waveband = waveband;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * focus : music
     * rawText : 我想听周杰伦的青花瓷
     * song : 青花瓷 歌曲名
     * artist : 周杰伦 歌手名
     * operation : PLAY（默认）|SEARCH
     * album : 专辑名
     * category : 歌曲类型【抒情/古典/流行/蓝调/乡村/校园/嘻哈/摇滚/爵士/轻音乐/经典】
     * source :  歌曲来源 【U盘|iPod|SD|CD|本地|网络】
     */
    private String song;
    private String artist;
    private String operation;
    private String album;
    private String source;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * 示例：打电话给10086
     * {"action":"call", "param1":"10086"}
     * 示例：发短信给10086，内容：话费还剩多少
     * {"action":"sendsms", "param1":"10086", "param2":"话费还剩多少"}
     * 语音助理开始进行识别录音
     * {"action":"startspeechrecord"}
     * startspeechrecord：语音助理告知系统，这个时候自身开始识别功能录音。（若使用讯飞降噪模块硬件，这个时候可以通知模块切换到降噪功能）
     * stopspeechrecord：语音助理告知系统，这个时候自身结束识别功能录音。
     * startwakerecord：语音助理告知系统，这个时候自身开始唤醒功能录音。（若使用讯飞降噪模块硬件，这个时候可以通知模块切换到唤醒功能）
     * stopwakerecord：语音助理告知系统，这个时候自身结束唤醒功能录音。
     */
    private String action;
    private String param1;
    private String param2;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
}
