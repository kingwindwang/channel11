package com.example.channel.https;

/**
 * Created by Admin on 2019/8/19.
 * 编写人：li
 * 功能描述：
 */
public interface HttpCallBack<T> {
    public void onSuccess(T success);
    public void onFail(String fail);
}
