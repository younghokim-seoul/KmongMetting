package com.example.meeting.view.edit;

import android.content.Context;

import com.example.meeting.AppContainer;
import com.example.meeting.L;
import com.example.meeting.data.AppDataManger;
import com.example.meeting.data.model.Account;
import com.example.meeting.view.login.LoginContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UserEditPresenter implements UserEditContract.UserEditPresenter {
    private UserEditContract.View mView;
    private FirebaseAuth auth;
    private DatabaseReference userDatabaseReference;
    private CompositeDisposable compositeDisposable;
    private AppDataManger appDataManager;
    private Account mCurrentAccount;


    public UserEditPresenter(UserEditContract.View view,
                             CompositeDisposable compositeDisposable,
                             AppContainer appContainer) {
        this.mView = view;
        this.compositeDisposable = compositeDisposable;
        this.appDataManager = appContainer.appDataManger;
    }

    @Override
    public void init() {
        auth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
        getUser(auth.getCurrentUser().getUid());
    }

    @Override
    public void getUser(String uid) {
        compositeDisposable.add(appDataManager.getUser(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    L.i("[getUser] Success");
                    if (result != null) {
                        L.i(":::call getUser " + result.toString());
                        mCurrentAccount = result;
                        mView.setUserInformation(result);
                    }
                }, error -> {
                    L.e("[getUser] Fail " + error.getMessage());
                    mView.setUserInformation(null);
                }));
    }

    @Override
    public void onUserEditClick(com.example.meeting.model.Account account) {
        mView.removeKeyboard();

        account.setEmail(mCurrentAccount.email);
        account.setPassword(mCurrentAccount.password);

        L.e(":::::account : " + account.toString());
        userDatabaseReference
                .setValue(account)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mView.showLoading(false);
                        cacheTransactions(auth.getCurrentUser().getUid(), account);
                    }
                });

    }

    private void cacheTransactions(String uid, com.example.meeting.model.Account account) {
        //내장 db에 유저정보 insert
        com.example.meeting.data.model.Account cacheUser = new com.example.meeting.data.model.Account();
        cacheUser.uid = uid;
        cacheUser.age = account.getAge();
        cacheUser.email = account.getEmail();
        cacheUser.name = account.getName();
        cacheUser.rank = account.getPosition();
        cacheUser.password = account.getPassword();
        cacheUser.token = "";


        compositeDisposable.add(appDataManager.insertUser(cacheUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mView.showMessage("수정 되었습니다 !");
                    mView.onSuccess();
                }, error -> {
                    L.e("::[Insert Error] " + error.getMessage());
                    mView.showMessage(error.getMessage());
                }));
    }

}
