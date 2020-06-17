package com.example.meeting.view.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.meeting.MainActivity;
import com.example.meeting.R;
import com.example.meeting.utils.MessageUtils;
import com.example.meeting.view.base.BaseActivity;
import com.example.meeting.view.signup.SignupActivity;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    //로그인 화면

    @BindView(R.id.email)
    EditText etId;
    @BindView(R.id.password)
    EditText etPw;

    @BindView(R.id.loading)
    SpinKitView loading;

    @Override
    protected LoginPresenter getPresenter() {
        //로그인을 db에서 체크하기위한 presenter 부분을 이관시킨다.
        return new LoginPresenter(this, getCompositeDisposable(), appContainer);
    }


    //로그인화면 진입시 타는부분
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //뷰와 xml을 연동시킨후 presenter로 db초기화를 시킨다.
        setUnBinder(ButterKnife.bind(this));
        presenter.init();
    }

    @Override
    public void openMainActivity() {
        //로그인성공시 mainActivity로
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void onError(String resId) {
        //로그인실패시 발생하는 popup창
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(resId);
        builder.setCancelable(false);
        builder.setNegativeButton("확인", (dialog, which) -> dialog.dismiss());
        builder.show();
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
    public void removeKeyboard() {
        hideKeyboard();
    }


    //로그인버튼클릭시
    @OnClick(R.id.login_button)
    void clickLogin() {

        //id 입력상태체크
        if (TextUtils.isEmpty(etId.getText().toString()) || etId.getText().toString().equalsIgnoreCase("")) {
            etId.setError("Required");
            return;
        }

        //비밀번호 입력상태체크
        if (TextUtils.isEmpty(etPw.getText().toString()) || etPw.getText().toString().equalsIgnoreCase("")) {
            etPw.setError("Required");
            return;
        }

        String id = etId.getText().toString();
        String pw = etPw.getText().toString();

        //id와 비밀번호를 가져온다.


        //로그인시도
        presenter.onLoginClick(id, pw, false);
    }

    //회원가입화면으로전화
    @OnClick(R.id.create_button)
    void clickCreateUser() {
        getCompositeDisposable().add(TedRxOnActivityResult.with(this)
                .startActivityForResult(new Intent(this, SignupActivity.class))
                .subscribe(activityResult -> {
                            if (activityResult.getResultCode() == Activity.RESULT_OK) {
                                FirebaseAuth.getInstance().signOut();

                                if (activityResult.getData() != null) {
                                    String email = activityResult.getData().getStringExtra("EXTRA_EMAIL");
                                    etId.setText(email);
                                }
                            }
                        }, error -> {

                        }
                ));
    }


}
