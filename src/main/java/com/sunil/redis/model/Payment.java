package com.sunil.redis.model;

public class Payment {
    String txnId;
    String statusCode;

    public Payment() {
    }

    public Payment(String txnId, String statusCode) {
        this.txnId = txnId;
        this.statusCode = statusCode;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "txnId='" + txnId + '\'' +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }
}
