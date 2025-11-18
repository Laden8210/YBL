package com.example.ybl.interfaces;

public interface ApiCallback<T> {
    void onSuccess(T response);
    void onError(String errorMessage);
    void onFailure(Throwable t);
}