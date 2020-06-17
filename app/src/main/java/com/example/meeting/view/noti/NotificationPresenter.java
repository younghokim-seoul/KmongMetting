package com.example.meeting.view.noti;

import androidx.annotation.NonNull;

import com.example.meeting.AppContainer;
import com.example.meeting.data.AppDataManger;
import com.example.meeting.model.PushMessage;
import com.example.meeting.model.Scheduler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class NotificationPresenter implements NotificationContract.NotificationPresenter {

    private NotificationContract.View mView;
    private FirebaseAuth auth;
    private DatabaseReference pushDatabaseReference;
    private CompositeDisposable compositeDisposable;
    private AppDataManger appDataManager;

    public NotificationPresenter(NotificationContract.View view,
                                 CompositeDisposable compositeDisposable,
                                 AppContainer appContainer) {
        this.mView = view;
        this.compositeDisposable = compositeDisposable;
        this.appDataManager = appContainer.appDataManger;
    }


    @Override
    public void loadNotification() {
        mView.showLoading(true);
        pushDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    mView.showLoading(false);
                    return;
                }

                List<PushMessage> list = new ArrayList<>();

                for (DataSnapshot node_child : dataSnapshot.getChildren()) {
                    PushMessage message = node_child.getValue(PushMessage.class);
                    list.add(message);
                }
                mView.showLoading(false);
                mView.setUpContent(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.showLoading(false);
            }
        });

    }

    @Override
    public void init() {
        auth = FirebaseAuth.getInstance();
        pushDatabaseReference = FirebaseDatabase.getInstance().getReference().child("notification").child(auth.getCurrentUser().getUid());
    }
}
