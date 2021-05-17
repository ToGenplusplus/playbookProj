package com.example.playbookProjApplicationBackend.Error;

import org.json.simple.JSONObject;

public class ResponseError{

    private Object message;
    private int errorCode;

    public ResponseError(Object message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ResponseError{" +
                "message='" + message + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }

    public String toJson(){
        JSONObject mainObj = new JSONObject();
        JSONObject headers = new JSONObject();
        headers.put("Access-Control-Allow-Origin","*");
        mainObj.put("headers",headers);
        mainObj.put("status",errorCode);
        mainObj.put("body",message);

        return mainObj.toJSONString();
    }


    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
