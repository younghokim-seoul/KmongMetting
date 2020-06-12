package com.example.meeting.view.base;

import androidx.annotation.StringRes;


//로딩및 에러창을 처리할 baseview;
public interface BaseView {
    void showLoading(boolean is);

    void showMessage(String message);

    void onError(String resId);

    void removeKeyboard();
}
