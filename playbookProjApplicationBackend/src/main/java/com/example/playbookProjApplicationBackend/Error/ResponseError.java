package com.example.playbookProjApplicationBackend.Error;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;

public class ResponseError{

    private Object message;
    private int errorCode;

    public ResponseError() {
    }

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

        return preetyJson(mainObj.toJSONString());
    }

    private String preetyJson(String uglyJSONString){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(JsonParser.parseString(uglyJSONString));
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
