package com.example.mediacalremider.requests;

public class RequestModel {
    String requestId;
    RequestModel(String id){
        requestId=id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
