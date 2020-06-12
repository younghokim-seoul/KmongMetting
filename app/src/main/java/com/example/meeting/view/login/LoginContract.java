package com.example.meeting.view.login;

import android.content.Context;
import com.example.meeting.view.base.BasePresenter;
import com.example.meeting.view.base.BaseView;

public interface LoginContract {
    interface View extends BaseView {
        void openMainActivity();
        Context getContext();
    }

    interface LoginPresenter extends BasePresenter {
        //로그인버튼클릭시
        void onLoginClick(String id, String password, boolean isAuto);

    }
}
