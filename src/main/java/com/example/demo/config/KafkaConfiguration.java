package com.example.demo.config;

import com.example.demo.handler.IMessageHandler;
import com.example.demo.handler.KafkaBatchAcknowledgingMessageListener;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration extends KafkaConfig {

    @Autowired
    private IMessageHandler messageHandler;

    @Override
    protected ContainerProperties getContainerProps() {
        ContainerProperties containerProps = new ContainerProperties(consumerTopic);
        containerProps.setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);
        containerProps.setMessageListener(new KafkaBatchAcknowledgingMessageListener(threadNum, maxPullRecords, messageHandler));
        return containerProps;
    }

    @Override
    protected Map<String, Object> getConsumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddress);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMS);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMS);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        propsMap.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 50000);
        if (maxPullSwitch) {
            propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPullRecords);
        }
        return propsMap;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, Object> kafkaAckMessageListenerContainer() {
        ConsumerFactory<String, Object> factory = new DefaultKafkaConsumerFactory<>(getConsumerConfigs());
        ConcurrentMessageListenerContainer<String, Object> container = new ConcurrentMessageListenerContainer<>(factory, getContainerProps());
        container.setBeanName("kafkaAckMessageListenerContainer");
        container.setConcurrency(concurrency);
        container.setAutoStartup(true);
        return container;
    }
}
