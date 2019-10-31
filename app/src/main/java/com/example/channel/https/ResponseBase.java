package com.example.channel.https;

/**
 * Created by Admin on 2019/8/19.
 * 编写人：li
 * 功能描述：
 */
public class ResponseBase<T> {

    private int state;
    private T content;
    private String reason;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
