package com.semisky.voicereceiveclient;

interface IVoiceChannelControl {

        /**
         * 关闭讯飞录音通道
         * controlProject 控制的工程
         */
        boolean closeVoiceChannel(int controlProject);

        /**
         * 打开讯飞录音通道
         * controlProject 控制的工程
         */
        boolean openVoiceChannel(int controlProject);

        /**
         * 关闭讯飞activity
         *
         */
        void closeVoiceActivity();
}
