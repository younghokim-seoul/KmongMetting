package com.example.meeting.view.signup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.meeting.view.base.BaseActivity;

public class SignupActivity extends BaseActivity<SignPresenter> implements SignContract.View {
    @Override
    protected SignPresenter getPresenter() {
        return new SignPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void showLoading(boolean is) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onError(String resId) {

    }

    @Override
    public void removeKeyboard() {
        hideKeyboard();
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
