package com.example.demo.handler;

import com.example.demo.entity.MessageKafka;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Kafka batch acknowledging message listener
 *
 * @author TaoNbo
 * @create 2017-06-23 17:15
 **/
public class KafkaBatchAcknowledgingMessageListener implements BatchAcknowledgingMessageListener<String, Object> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IMessageHandler messageHandler;
    private ThreadPoolExecutor threadPoolExecutor;
    private int maxPullRecords;

    public KafkaBatchAcknowledgingMessageListener(int threadNum, int maxPullRecords, IMessageHandler messageHandler) {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
        this.maxPullRecords = maxPullRecords;
        this.messageHandler = messageHandler;
    }

    @Override
    public void onMessage(List<ConsumerRecord<String, Object>> records, Acknowledgment acknowledgment) {
        logger.error("ReceivingSize:{}", records.size());
        CountDownLatch latch = new CountDownLatch(records.size() < maxPullRecords ? records.size() : maxPullRecords);
        for (ConsumerRecord<String, Object> record : records) {
            MessageKafka messageKafka = new MessageKafka();
            messageKafka.setTopic(record.topic());
            messageKafka.setContent(record.value());
            messageKafka.setExtendInfo(new HashMap<String, Object>() {{
                put("partition", String.valueOf(record.partition()));
                put("offset", String.valueOf(record.offset()));
            }});
            messageKafka.setCommitFlag(records.indexOf(record) == (records.size() - 1));
            try {
                if (threadPoolExecutor.getQueue().size() > 10 * 1000) {
                    Thread.sleep(3);
                }
                threadPoolExecutor.submit(() -> messageHandler.doHandler(messageKafka));
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Execute condition process error");
            } finally {
                latch.countDown();
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("latch await error");
            e.printStackTrace();
        }
        try {
            acknowledgment.acknowledge();//提交offset
        } catch (Exception e) {
            logger.error("Commit offset to kafka error");
            e.printStackTrace();
        }
    }
}
