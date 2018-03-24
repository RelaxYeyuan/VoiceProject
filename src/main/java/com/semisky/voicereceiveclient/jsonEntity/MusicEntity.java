package com.semisky.voicereceiveclient.jsonEntity;

/**
 * Created by chenhongrui on 2018/1/29
 * <p>
 * 内容摘要：${TODO}
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class MusicEntity extends BaseEntity {
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
    private String category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
