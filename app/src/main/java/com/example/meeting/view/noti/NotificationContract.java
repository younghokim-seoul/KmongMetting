package com.example.meeting.view.noti;

import android.app.Activity;

import com.example.meeting.model.PushMessage;
import com.example.meeting.model.Weather;
import com.example.meeting.view.base.BasePresenter;
import com.example.meeting.view.base.BaseView;

import java.util.List;

public interface NotificationContract {
    interface View extends BaseView {
        Activity getActivity();
        void setUpContent(List<PushMessage> items);
    }

    interface NotificationPresenter extends BasePresenter {
        void loadNotification();
    }
}
