package com.semisky.voicereceiveclient;

// Declare any non-default types here with import statements

interface IUSBMusicListener {

    //根据歌手播放音乐
    int playByArtist(String artist);

    //根据歌名播放音乐
    int playBySong(String song);

    //根据歌手和歌名播放音乐
    int playByArtistAndSong(String artist,String song);

    //改变播放顺序
    //1.顺序播放 2.单曲循环 3.随机播放
    int changePlayOrder(int type);

    //暂停
    int pause();

    //播放
    int play();

    //上一个节目
    int lastProgram();

    //下一个节目
    int NextProgram();

    //打开音乐列表
    int openMusicList();

}