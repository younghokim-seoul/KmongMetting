package com.example.meeting.view.main;

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

public class MainPresenter implements MainContract.MainPresenter {

    private MainContract.View mView;
    private FirebaseAuth auth;
    private DatabaseReference calendarDatabaseReference;
    private CompositeDisposable compositeDisposable;
    private AppDataManger appDataManager;


    public MainPresenter(MainContract.View view,
                         CompositeDisposable compositeDisposable,
                         AppContainer appContainer) {
        this.mView = view;
        this.compositeDisposable = compositeDisposable;
        this.appDataManager = appContainer.appDataManger;
    }


    @Override
    public void init() {
        auth = FirebaseAuth.getInstance();
        calendarDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Calendars");

        if (auth.getCurrentUser() != null) {
            getUser(auth.getCurrentUser().getUid());
        }

    }

    @Override
    public void getUser(String uid) {
        compositeDisposable.add(appDataManager.getUser(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    L.i("[getUser] Success");
                    if (result != null) {
                        mView.setUserName(result.name);
                    }
                }, error -> {
                    L.e("[getUser] Fail");
                    mView.setUserName("");
                }));
    }

    private void onError() {
        mView.showLoading(false);
        mView.showMessage("다시 시도해주세요.");
    }


}
