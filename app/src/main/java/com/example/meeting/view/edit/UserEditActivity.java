package com.example.meeting.view.edit;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.meeting.R;
import com.example.meeting.model.Account;
import com.example.meeting.utils.MessageUtils;
import com.example.meeting.view.base.BaseActivity;
import com.example.meeting.view.signup.SignPresenter;
import com.github.ybq.android.spinkit.SpinKitView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserEditActivity extends BaseActivity<UserEditPresenter> implements UserEditContract.View {

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        //뷰와 xml을 연동시킨후 presenter로 db초기화를 시킨다.
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
    protected UserEditPresenter getPresenter() {
        return new UserEditPresenter(this, getCompositeDisposable(), appContainer);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setUserInformation(com.example.meeting.data.model.Account account) {
        if (account != null) {
            etName.setText(account.name);
            etAge.setText(account.age);
            selectedBusinessRank = account.rank;
            positionSpinner.setSelection(getRank(account.rank));
        } else {
            etName.setText("");
            etAge.setText("");
            positionSpinner.setSelection(0);
        }
    }

    @Override
    public void onSuccess() {
        setResult(RESULT_OK);
        finish();
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


    @OnClick(R.id.user_edit_confirm)
    void clickLogin() {
        String name = etName.getText().toString();
        String age = etAge.getText().toString();

        //id와 비밀번호를 가져온다.
        Account account = Account.builder().name(name).age(age).position(selectedBusinessRank).token("").build();
        //로그인시도
        presenter.onUserEditClick(account);
    }

    @OnClick(R.id.user_edit_cancel)
    void clickCancel() {
        onBackPressed();
    }

    private int getRank(String rank) {
        int result = 0;
        switch (rank) {
            case "직책":
                result = 0;
                break;
            case "부장":
                result = 1;
                break;
            case "차장":
                result = 2;
                break;
            case "사원":
                result = 3;
                break;
        }

        return result;

    }
}
