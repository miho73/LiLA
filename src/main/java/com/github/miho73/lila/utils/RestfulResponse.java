package com.github.miho73.lila.utils;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;

public class RestfulResponse {
    public static String responseResult(HttpStatus status, Object result) {
        JSONObject resp = new JSONObject();
        resp.put("code", status.value());
        resp.put("message", status.getReasonPhrase());
        resp.put("result", result);
        return resp.toString();
    }
    public static String responseMessage(HttpStatus status, String message) {
        JSONObject resp = new JSONObject();
        resp.put("code", status.value());
        resp.put("message", message);
        return resp.toString();
    }
    public static String response(HttpStatus status, String message, Object result) {
        JSONObject resp = new JSONObject();
        resp.put("code", status.value());
        resp.put("message", message);
        resp.put("result", result);
        return resp.toString();
    }
}
