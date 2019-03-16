package com.semisky.voicereceiveclient.ManagerHandler;

import org.json.JSONObject;

/**
 * Created by chenhongrui on 2019/3/15
 *
 * 内容摘要: 优化语义下发方式，采取责任链设计模式(Chain of Responsibility)
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 *
 * 责任链模式是一种对象的行为模式。在责任链模式里，很多对象由每一个对象对其下家的引用而连接起来形成一条链。
 * 请求在这个链上传递，直到链上的某一个对象决定处理此请求。发出这个请求的客户端并不知道链上的哪一个对象最终处理这个请求，
 * 这使得系统可以在不影响客户端的情况下动态地重新组织和分配责任。
 *
 */
public abstract class MessageHandler<D> {

    protected MessageHandler<D> nextHandler;

    public MessageHandler() {
    }

    public abstract JSONObject action(int cmd, D data);

    void setNextHandler(MessageHandler<D> handler) {
        this.nextHandler = handler;
    }
}
