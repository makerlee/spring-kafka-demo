package com.example.demo.handler.impl;

import com.example.demo.entity.MessageKafka;
import com.example.demo.handler.IMessageHandler;
import org.springframework.stereotype.Component;

/**
 * Created by lijiyang on 2018/1/19.
 */
@Component
public class MessageHandlerImpl implements IMessageHandler{
    @Override
    public void doHandler(MessageKafka messageKafka) {
        System.out.println("consume---->"+messageKafka);
    }
}
