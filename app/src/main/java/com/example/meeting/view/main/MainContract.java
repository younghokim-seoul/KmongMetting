package com.example.meeting.view.main;

import android.app.Activity;

import com.example.meeting.model.Account;
import com.example.meeting.view.base.BasePresenter;
import com.example.meeting.view.base.BaseView;

public interface MainContract {
    interface View extends BaseView {
        Activity getActivity();
        void setUserName(String name);
    }

    interface MainPresenter extends BasePresenter {
        void init();
        void getUser(String uid);
    }
}
