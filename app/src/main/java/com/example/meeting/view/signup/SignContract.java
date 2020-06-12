package com.example.meeting.view.signup;

import android.content.Context;

import com.example.meeting.view.base.BasePresenter;
import com.example.meeting.view.base.BaseView;

public interface SignContract {
    interface View extends BaseView {
        Context getContext();
    }

    interface LoginPresenter extends BasePresenter {
        //로그인버튼클릭시
        void onSignUpClick(String id, String password);

    }
}
