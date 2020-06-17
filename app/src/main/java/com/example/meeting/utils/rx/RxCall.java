package com.example.meeting.utils.rx;

public interface RxCall<T> {
    void onCall(T data);
    void onError(Throwable throwable);
}
