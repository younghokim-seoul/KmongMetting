package com.example.meeting.view.main;

import android.app.Activity;
import android.app.AlertDialog;

import com.applandeo.materialcalendarview.EventDay;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.model.Scheduler;
import com.example.meeting.view.base.BasePresenter;
import com.example.meeting.view.base.BaseView;

import java.util.Calendar;
import java.util.List;

public interface MainContract {
    interface View extends BaseView {
        Activity getActivity();

        void setUserName(String name);

        void showTimeDialog(Calendar calendar);

        void setCalendarSwipeDisable(boolean is);

        void setCalendarEventDay(List<EventDay> list);


    }

    interface MainPresenter extends BasePresenter {
        void init();

        void getUser(String uid);

        void logout();

        void setTimeSetting(Calendar cal);

        void setTimeInfo(Calendar calendar);

        void setDetailCalendar(Calendar calendar);

        void onSubmit(AlertDialog dialog, Alarm alarm, Scheduler scheduler, com.example.meeting.data.model.Calendar calendar);
    }
}
