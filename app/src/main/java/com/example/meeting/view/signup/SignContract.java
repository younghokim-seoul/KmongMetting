package com.example.meeting.view.signup;

import android.app.Activity;
import android.content.Context;

import com.example.meeting.model.Account;
import com.example.meeting.view.base.BasePresenter;
import com.example.meeting.view.base.BaseView;

public interface SignContract {
    interface View extends BaseView {
        Activity getActivity();
        void onSuccess();
    }

    interface SingupPresenter extends BasePresenter {
        void onSingUpClick(Account account);

    }
}
