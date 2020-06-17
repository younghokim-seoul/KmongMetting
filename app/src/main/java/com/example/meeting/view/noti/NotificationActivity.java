package com.example.meeting.view.noti;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meeting.R;
import com.example.meeting.model.PushMessage;
import com.example.meeting.model.Weather;
import com.example.meeting.utils.MessageUtils;
import com.example.meeting.view.base.BaseActivity;
import com.example.meeting.view.weather.WeatherActivity;
import com.github.ybq.android.spinkit.SpinKitView;
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends BaseActivity<NotificationPresenter> implements NotificationContract.View {
    @BindView(R.id.loading)
    SpinKitView loading;

    @BindView(R.id.recycler_list)
    RecyclerView recyclerView;

    private NotificationAdapter adapter;

    private boolean isStack;

    @OnClick(R.id.btn_calendar)
    void onCalendarClick() {
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.btn_weather)
    void onWeatherClick() {
        if (isStack) {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("EXTRA_MAIN_STACK", true);
            getCompositeDisposable().add(TedRxOnActivityResult.with(this)
                    .startActivityForResult(intent)
                    .subscribe((activityResult) -> {
                                if (activityResult.getResultCode() == RESULT_OK) {
                                    finish();
                                }

                            }, error -> {

                            }
                    ));
        } else {
            finish();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setUnBinder(ButterKnife.bind(this));

        Intent intent = getIntent();
        isStack = intent.getBooleanExtra("EXTRA_MAIN_STACK", false);


        initView();

    }

    private void initView() {
        adapter = new NotificationAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);


        presenter.init();
        presenter.loadNotification();
    }

    @Override
    protected NotificationPresenter getPresenter() {
        return new NotificationPresenter(this, getCompositeDisposable(), appContainer);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setUpContent(List<PushMessage> items) {
        adapter.updateItems(items);
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
