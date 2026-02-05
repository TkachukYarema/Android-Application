package com.example.myapplication;

public interface ApiCallback {
    void onSuccess(String result);

    void onFailure(String errorMessage);
}
