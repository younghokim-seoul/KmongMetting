package com.example.meeting;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.data.model.Calendar;
import com.example.meeting.model.Scheduler;
import com.example.meeting.utils.MessageUtils;
import com.example.meeting.utils.rx.RXHelper;
import com.example.meeting.utils.rx.RxCall;
import com.example.meeting.view.base.BaseActivity;
import com.example.meeting.view.dialog.TodoDialog;
import com.example.meeting.view.edit.UserEditActivity;
import com.example.meeting.view.login.LoginActivity;
import com.example.meeting.view.main.MainContract;
import com.example.meeting.view.main.MainPresenter;
import com.example.meeting.view.noti.NotificationActivity;
import com.example.meeting.view.signup.SignupActivity;
import com.example.meeting.view.weather.WeatherActivity;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.tv_user)
    TextView tvUser;

    @BindView(R.id.loading)
    SpinKitView loading;


    @BindView(R.id.caneldarView)
    CalendarView mCalendar;

    @OnClick(R.id.btn_message)
    void onNotificationClick() {

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("EXTRA_MAIN_STACK",true);
        getCompositeDisposable().add(TedRxOnActivityResult.with(this)
                .startActivityForResult(intent)
                .subscribe((activityResult) -> {

                        }, error -> {

                        }
                ));
    }


    @OnClick(R.id.btn_edit_userinfo)
    void onUserEditClick() {
        getCompositeDisposable().add(TedRxOnActivityResult.with(this)
                .startActivityForResult(new Intent(this, UserEditActivity.class))
                .subscribe(activityResult -> {
                            if (activityResult.getResultCode() == Activity.RESULT_OK) {
                                presenter.getUser(FirebaseAuth.getInstance().getUid());
                            }
                        }, error -> {

                        }
                ));
    }

    @OnClick(R.id.btn_logout)
    void onUserLogout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.btn_weather)
    void onWeatherClick() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    startActivity(new Intent(MainActivity.this, WeatherActivity.class));
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();

    }

    @OnClick(R.id.btn_calendar)
    void onCalendarClick() {
        LocalDate date = new LocalDate();
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, (view, year, month, dayOfMonth) -> {
            LocalDate resultDate = new LocalDate(year, month + 1, dayOfMonth);
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(java.util.Calendar.YEAR, resultDate.getYear());
            calendar.set(java.util.Calendar.MONTH, resultDate.getMonthOfYear() - 1);
            calendar.set(java.util.Calendar.DAY_OF_MONTH, resultDate.getDayOfMonth());
            presenter.setTimeSetting(calendar);
        }, date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());


        LocalDate today = new LocalDate();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTimeInMillis(today.toDate().getTime());
        cal.add(java.util.Calendar.YEAR, -12);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        cal.setTimeInMillis(today.toDate().getTime());
        cal.add(java.util.Calendar.YEAR, 12);
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePickerDialog.show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));

        presenter.init();


        mCalendar.setOnDayClickListener(eventDay -> {
//            presenter.setTimeSetting(eventDay.getCalendar());
            presenter.setDetailCalendar(eventDay.getCalendar());
        });

        mCalendar.setOnCalendarPageSelected(calendar -> RXHelper.delay(50, new RxCall<Long>() {
            @Override
            public void onCall(Long data) {
                presenter.setTimeInfo(calendar);
            }

            @Override
            public void onError(Throwable throwable) {
                L.i("[onError] " + throwable.getMessage());
            }
        }));
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter(this, getCompositeDisposable(), appContainer);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setUserName(String name) {
        tvUser.setText(name + "님");
    }

    @Override
    public void showTimeDialog(java.util.Calendar calendar) {
        new TodoDialog(MainActivity.this, calendar).setDialogButtonClickListener(new TodoDialog.OnClickListener() {
            @Override
            public void onDeny(View view, AlertDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onSubmit(View view, AlertDialog dialog, Alarm alarm, Scheduler scheduler, Calendar calendar) {
                presenter.onSubmit(dialog, alarm, scheduler, calendar);
            }
        }).show();
    }

    @Override
    public void setCalendarSwipeDisable(boolean is) {
        mCalendar.setSwipeEnabled(is);
    }

    @Override
    public void setCalendarEventDay(List<EventDay> list) {
        mCalendar.setEvents(list);
    }

    @Override
    public void showLoading(boolean is) {
        loading.setVisibility(is ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMessage(String message) {
        MessageUtils.showLongToastMsg(this, message);
    }

    @Override
    public void onError(String resId) {

    }

    @Override
    public void removeKeyboard() {

    }
}
