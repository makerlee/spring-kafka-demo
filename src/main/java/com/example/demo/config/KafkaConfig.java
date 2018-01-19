package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.util.Map;

/**
 * kafka配置文件类
 */
public abstract class KafkaConfig {

    /* Kafka consumer client config */
    @Value("${test.kafka.sender.serverAddress}")
    protected String brokerAddress;
    @Value("${test.kafka.topic.consumer}")
    protected String consumerTopic;
    @Value("${test.kafka.client.enableAutoCommit}")
    protected String enableAutoCommit;
    @Value("${test.kafka.client.autoCommitIntervalMS}")
    protected String autoCommitIntervalMS;
    @Value("${test.kafka.client.sessionTimeoutMS}")
    protected String sessionTimeoutMS;
    @Value("${test.kafka.client.groupID}")
    protected String groupID;
    @Value("${test.kafka.client.autoOffsetReset}")
    protected String autoOffsetReset;
    @Value("${test.kafka.client.maxPullSwitch}")
    protected boolean maxPullSwitch;
    @Value("${test.kafka.client.maxPullRecords}")
    protected int maxPullRecords;
    @Value("${test.kafka.client.concurrency}")
    protected Integer concurrency;
    @Value("${test.kafka.client.pollTimeOut}")
    protected long pollTimeOut;
    @Value("${test.kafka.client.requestTimeout}")
    protected long requestTimeout;
    @Value("${test.kafka.client.group}")
    protected String group;
    @Value("${test.kafka.client.threadNum}")
    protected int threadNum;

    protected abstract ContainerProperties getContainerProps();

    protected abstract Map<String, Object> getConsumerConfigs();
}
