package com.example.demo.entity;

public class MessageKafka {

    private String topic;
    private Object content;
    private Object extendInfo;
    private boolean commitFlag;

    public MessageKafka() {}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(Object extendInfo) {
        this.extendInfo = extendInfo;
    }

    public boolean isCommitFlag() {
        return commitFlag;
    }

    public void setCommitFlag(boolean commitFlag) {
        this.commitFlag = commitFlag;
    }

    @Override
    public String toString() {
        return "MessageKafka{" +
                "topic='" + topic + '\'' +
                ", content=" + content +
                ", extendInfo=" + extendInfo +
                ", commitFlag=" + commitFlag +
                '}';
    }
}
