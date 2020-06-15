package com.example.meeting.view.signup;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.meeting.L;
import com.example.meeting.R;
import com.example.meeting.model.Account;
import com.example.meeting.utils.MessageUtils;
import com.example.meeting.view.base.BaseActivity;
import com.github.ybq.android.spinkit.SpinKitView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends BaseActivity<SignPresenter> implements SignContract.View {

    @BindView(R.id.et_email)
    EditText etId;
    @BindView(R.id.et_password)
    EditText etPw;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.spinner_company_position)
    Spinner positionSpinner;
    @BindView(R.id.loading)
    SpinKitView loading;
    private String selectedBusinessRank;


    @Override
    protected SignPresenter getPresenter() {
        return new SignPresenter(this, getCompositeDisposable(), appContainer);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUnBinder(ButterKnife.bind(this));
        presenter.init();

        initView();


    }

    private void initView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.position));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(adapter);
        positionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) positionSpinner.getSelectedItem();
                selectedBusinessRank = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    @Override
    public void onError(String resId) {

    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onSuccess() {
        setResult(RESULT_OK);
        finish();
    }


    //로그인버튼클릭시
    @OnClick(R.id.user_signup_confirm)
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
        String name = etAge.getText().toString();
        String age = etAge.getText().toString();


        //id와 비밀번호를 가져온다.
        Account account = Account.builder().name(name).email(id).password(pw).age(age).position(selectedBusinessRank).token("").build();
        //로그인시도
        presenter.onSingUpClick(account);
    }

    @OnClick(R.id.user_signup_cancel)
    void clickCancel() {
        onBackPressed();
    }
}
