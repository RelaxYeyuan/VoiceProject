package com.semisky.voicereceiveclient.ManagerHandler;

import org.json.JSONObject;

/**
 * Created by chenhongrui on 2019/3/15
 *
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class MessageHandlerManager<D> {

    private MessageHandler<D> handler;

    private MessageHandlerManager(MessageHandler<D> h) {
        this.handler = h;
    }

    /**
     * 分发指令
     */
    public JSONObject dispatchActionHandler(int cmd, D data) {
        return handler.action(cmd, data);
    }

    public static class HandlersBuilder<D> {
        private MessageHandler<D> header;
        private MessageHandler<D> tail;

        public HandlersBuilder() {
            header = null;
            tail = null;
        }

        public HandlersBuilder<D> addManager(MessageHandler<D> handler) {
            if (header == null) {
                this.header = handler;
                this.tail = handler;
            } else {
                this.tail.setNextHandler(handler);
                this.tail = handler;
            }
            return this;
        }

        public MessageHandlerManager<D> build() {
            return new MessageHandlerManager<>(header);
        }
    }
}