package com.example.meeting.view.edit;

import android.app.Activity;
import android.app.AlertDialog;

import com.applandeo.materialcalendarview.EventDay;
import com.example.meeting.data.model.Account;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.model.Scheduler;
import com.example.meeting.view.base.BasePresenter;
import com.example.meeting.view.base.BaseView;

import java.util.Calendar;

public interface UserEditContract {
    interface View extends BaseView {
        Activity getActivity();

        void setUserInformation(Account account);

        void onSuccess();
    }

    interface UserEditPresenter extends BasePresenter {
        void init();

        void getUser(String uid);

        void onUserEditClick(com.example.meeting.model.Account account);
    }
}
