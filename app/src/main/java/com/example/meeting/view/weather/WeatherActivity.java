package com.example.meeting.view.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.meeting.R;
import com.example.meeting.model.Weather;
import com.example.meeting.utils.MessageUtils;
import com.example.meeting.view.base.BaseActivity;
import com.example.meeting.view.noti.NotificationActivity;
import com.github.ybq.android.spinkit.SpinKitView;
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherActivity extends BaseActivity<WeaTherPresenter> implements WeaTherContract.View {

    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.loading)
    SpinKitView loading;

    @BindView(R.id.tv_t1h)
    TextView tvT1h;

    @BindView(R.id.tv_rn1)
    TextView tvRn1;

    @BindView(R.id.tv_uuu)
    TextView tvUUU;

    @BindView(R.id.tv_vvv)
    TextView tvVVV;

    @BindView(R.id.tv_reh)
    TextView tvREH;

    @BindView(R.id.tv_pty)
    TextView tvPTY;

    @BindView(R.id.tv_vec)
    TextView tvVEC;

    @BindView(R.id.tv_wsd)
    TextView tvWSD;

    private boolean isStack;

    @OnClick(R.id.btn_weather_back)
    void onClickBack() {
        if(isStack){
            setResult(RESULT_OK);
            finish();
        }else{
            onBackPressed();
        }
    }

    @OnClick(R.id.btn_notification)
    void onClickNotification() {
        getCompositeDisposable().add(TedRxOnActivityResult.with(this)
                .startActivityForResult(new Intent(this, NotificationActivity.class))
                .subscribe((activityResult) -> {
                            if (activityResult.getResultCode() == RESULT_OK) {
                                finish();
                            }
                        }, error -> {

                        }
                ));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        setUnBinder(ButterKnife.bind(this));
        presenter.init();

        Intent intent = getIntent();
        isStack = intent.getBooleanExtra("EXTRA_MAIN_STACK", false);


    }

    @Override
    protected WeaTherPresenter getPresenter() {
        return new WeaTherPresenter(this, getCompositeDisposable(), appContainer);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setAddress(String address) {
        tvAddress.setText(address == null ? "알수없음" : address);
    }

    @Override
    public void setWeatherInfo(Weather weather) {
        tvT1h.setText(weather.getT1H());
        tvRn1.setText(getRN1Code(Integer.parseInt(weather.getRN1())));
        tvUUU.setText(weather.getUUU());
        tvVVV.setText(weather.getVVV());
        tvREH.setText(weather.getREH());
        tvPTY.setText(getPTYCode(Integer.parseInt(weather.getPTY())));
        tvVEC.setText(weather.getVEC());
        tvWSD.setText(weather.getWSD());

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

    private String getPTYCode(int code) {
        String result = "";
        switch (code) {
            case 0:
                result = "없음";
                break;
            case 1:
                result = "비";
                break;
            case 2:
                result = "비/눈";
                break;
            case 3:
                result = "소나기";
                break;

        }

        return result;

    }

    private String getRN1Code(int code) {
        String result = "";
        switch (code) {
            case 0:
                result = "0mm 또는 없음";
                break;
            case 1:
                result = "1mm 미만";
                break;
            case 5:
                result = "1~4mm";
                break;
            case 10:
                result = "5~9mm";
                break;
            case 20:
                result = "10~19mm";
                break;
            case 40:
                result = "20~39mm";
                break;
            case 70:
                result = "40~69mm";
                break;
            case 100:
                result = "70mm 이상";
                break;

        }

        return result;

    }
}
