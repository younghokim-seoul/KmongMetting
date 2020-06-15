package com.example.meeting.view.login;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginPresenter implements LoginContract.LoginPresenter {

    private LoginContract.View mView;
    private Context mContext;
    private FirebaseAuth auth;
    private DatabaseReference userDatabaseReference;


    public LoginPresenter(LoginContract.View view) {
        this.mView = view;
        this.mContext = view.getContext();
    }

    @Override
    public void onLoginClick(String id, String password, boolean isAuto) {
        mView.showLoading(true);
        mView.removeKeyboard();

        auth.signInWithEmailAndPassword(id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mView.showLoading(false);
                    mView.showMessage("로그인에 성공 하였습니다");
                    mView.openMainActivity();
                }
            }
        }).addOnFailureListener(e -> {
            mView.showLoading(false);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                mView.onError("비밀번호가 일치하지 않습니다.");
            } else if (e instanceof FirebaseAuthInvalidUserException) {
                mView.onError("존재하지 않는 이메일 입니다.");
            } else {
                mView.showMessage("다시 시도해주세요.");
            }
        });
    }


    @Override
    public void init() {
        auth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        if (auth.getCurrentUser() != null) {
            mView.openMainActivity();
        }
    }

}
