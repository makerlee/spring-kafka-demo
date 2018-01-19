package com.example.demo.handler;

import com.example.demo.entity.MessageKafka;

/**
 * Created by lijiyang on 2018/1/19.
 */

public interface IMessageHandler {
    void doHandler(MessageKafka messageKafka);
}
