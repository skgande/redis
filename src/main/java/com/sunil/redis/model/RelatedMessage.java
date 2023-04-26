package com.sunil.redis.model;

public class RelatedMessage {
    private String messageId;
    private String relatedObjectId;

    public RelatedMessage() {
    }

    public RelatedMessage(String messageId, String relatedObjectId) {
        this.messageId = messageId;
        this.relatedObjectId = relatedObjectId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRelatedObjectId() {
        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {
        this.relatedObjectId = relatedObjectId;
    }

    @Override
    public String toString() {
        return "RelatedMessage{" +
                "messageId='" + messageId + '\'' +
                ", relatedObjectId='" + relatedObjectId + '\'' +
                '}';
    }
}
