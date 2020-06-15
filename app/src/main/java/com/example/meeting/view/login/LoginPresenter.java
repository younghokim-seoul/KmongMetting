package com.example.meeting.view.login;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.meeting.AppContainer;
import com.example.meeting.L;
import com.example.meeting.data.AppDataManger;
import com.example.meeting.model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.LoginPresenter {

    private LoginContract.View mView;
    private Context mContext;
    private FirebaseAuth auth;
    private DatabaseReference userDatabaseReference;
    private CompositeDisposable compositeDisposable;
    private AppDataManger appDataManager;


    public LoginPresenter(LoginContract.View view,
                          CompositeDisposable compositeDisposable,
                          AppContainer appContainer) {
        this.mView = view;
        this.mContext = view.getContext();
        this.compositeDisposable = compositeDisposable;
        this.appDataManager = appContainer.appDataManger;
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
                    userDatabaseReference
                            .child(task.getResult().getUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Account user = dataSnapshot.getValue(Account.class);
                                        if (user != null) {
                                            cahceTransation(task.getResult().getUser(),user);
                                        }

                                    } else {
                                        mView.showMessage("다시 시도해주세요.");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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

    private void cahceTransation(FirebaseUser firebaseUser, Account account) {
        com.example.meeting.data.model.Account cacheUser = new com.example.meeting.data.model.Account();
        cacheUser.uid = firebaseUser.getUid();
        cacheUser.age = account.getAge();
        cacheUser.email = account.getEmail();
        cacheUser.name = account.getName();
        cacheUser.rank = account.getPosition();
        cacheUser.token = "";


        compositeDisposable.add(appDataManager.insertUser(cacheUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mView.showMessage("로그인에 성공 하였습니다");
                    mView.openMainActivity();
                }, error -> {
                    L.e("::[Insert Error] " + error.getMessage());
                    mView.showMessage(error.getMessage());
                }));
    }

}
