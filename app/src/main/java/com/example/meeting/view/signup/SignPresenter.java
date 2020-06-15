package com.example.meeting.view.signup;

import com.example.meeting.AppContainer;
import com.example.meeting.L;
import com.example.meeting.data.AppDataManger;
import com.example.meeting.model.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SignPresenter implements SignContract.SingupPresenter {

    private SignContract.View mView;
    private FirebaseAuth auth;
    private DatabaseReference userDatabaseReference;
    private CompositeDisposable compositeDisposable;
    private AppDataManger appDataManager;


    public SignPresenter(SignContract.View view,
                         CompositeDisposable compositeDisposable,
                         AppContainer appContainer) {
        this.mView = view;
        this.compositeDisposable = compositeDisposable;
        this.appDataManager = appContainer.appDataManger;
    }


    @Override
    public void init() {
        auth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void onSingUpClick(Account account) {
        mView.showLoading(true);
        mView.removeKeyboard();

        auth.fetchSignInMethodsForEmail(account.getEmail()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean result = task.getResult().getSignInMethods().isEmpty();
                if (!result) {
                    mView.showLoading(false);
                    mView.showMessage("이미 존재하는 계정 입니다.");
                } else {
                    signUpFirebaseUser(account);
                }
            }
        }).addOnFailureListener(e -> {
            onError();
        });
    }

    private void signUpFirebaseUser(Account account) {
        auth.createUserWithEmailAndPassword(account.getEmail(), account.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                registerSuccess(task.getResult().getUser(), account);
            } else {
                onError();
            }
        }).addOnFailureListener(e -> onError());
    }

    private void registerSuccess(FirebaseUser firebaseUser, Account account) {
        userDatabaseReference
                .child(firebaseUser.getUid())
                .setValue(account)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mView.showLoading(false);
                        cacheTransactions(firebaseUser.getUid(), account);
                    }
                });
    }

    private void cacheTransactions(String uid, Account account) {


        //내장 db에 유저정보 insert
        com.example.meeting.data.model.Account cacheUser = new com.example.meeting.data.model.Account();
        cacheUser.uid = uid;
        cacheUser.age = account.getAge();
        cacheUser.email = account.getEmail();
        cacheUser.name = account.getName();
        cacheUser.rank = account.getPosition();
        cacheUser.token = "";


        compositeDisposable.add(appDataManager.insertUser(cacheUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mView.showMessage("회원가입이 완료 되었습니다.");
                    mView.onSuccess();
                }, error -> {
                    L.e("::[Insert Error] " + error.getMessage());
                    mView.showMessage(error.getMessage());
                }));
    }


    private void onError() {
        mView.showLoading(false);
        mView.showMessage("다시 시도해주세요.");
    }


}
