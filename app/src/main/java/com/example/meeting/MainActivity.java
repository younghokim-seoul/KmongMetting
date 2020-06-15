package com.example.meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.model.Scheduler;
import com.example.meeting.utils.MessageUtils;
import com.example.meeting.view.base.BaseActivity;
import com.example.meeting.view.dialog.TodoDialog;
import com.example.meeting.view.main.MainContract;
import com.example.meeting.view.main.MainPresenter;
import com.example.meeting.view.signup.SignPresenter;
import com.github.ybq.android.spinkit.SpinKitView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.tv_user)
    TextView tvUser;

    @BindView(R.id.loading)
    SpinKitView loading;


    @BindView(R.id.caneldarView)
    CalendarView mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));

        presenter.init();


        mCalendar.setOnDayClickListener(eventDay -> {
            presenter.setTimeSetting(eventDay.getCalendar());
        });
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
    public void showTimeDialog() {
        new TodoDialog(MainActivity.this).setDialogButtonClickListener(new TodoDialog.OnClickListener() {
            @Override
            public void onDeny(View view, AlertDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onSubmit(View view, AlertDialog dialog, Alarm alarm, Scheduler scheduler) {

            }
        }).show();
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
