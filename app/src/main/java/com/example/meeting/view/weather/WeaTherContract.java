package com.example.meeting.view.weather;

import android.app.Activity;
import android.app.AlertDialog;

import com.applandeo.materialcalendarview.EventDay;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.model.Scheduler;
import com.example.meeting.model.Weather;
import com.example.meeting.view.base.BasePresenter;
import com.example.meeting.view.base.BaseView;

import java.util.Calendar;
import java.util.List;

public interface WeaTherContract {
    interface View extends BaseView {
        Activity getActivity();

        void setAddress(String address);

        void setWeatherInfo(Weather weather);

    }

    interface WeaTherPresenter extends BasePresenter {
        void init();

        void getUser(String uid);
    }
}
