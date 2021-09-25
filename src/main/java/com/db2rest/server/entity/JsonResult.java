package com.db2rest.server.entity;

public class JsonResult<T> {
    private T data;
    private boolean success;
    private String msg;

    public JsonResult() {
        this.success = true;
    }

    public JsonResult(boolean code, String msg) {
        this.success = code;
        this.msg = msg;
    }

    public JsonResult(T data) {
        this.data = data;
        this.success = true;
        this.msg = "";
    }

    public JsonResult(T data, String msg) {
        this.data = data;
        this.success = true;
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean code) {
        this.success = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
