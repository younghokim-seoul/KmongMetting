package com.example.meeting.view.signup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.meeting.L;
import com.example.meeting.R;
import com.example.meeting.model.Account;
import com.example.meeting.utils.MessageUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignPresenter implements SignContract.SingupPresenter {

    private SignContract.View mView;
    private Context mContext;
    private FirebaseAuth auth;
    private DatabaseReference userDatabaseReference;


    public SignPresenter(SignContract.View view) {
        L.i("[LoginPresenter init]");
        this.mView = view;
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onError();

            }
        });
    }

    private void registerSuccess(FirebaseUser firebaseUser, Account account) {
        userDatabaseReference
                .child(firebaseUser.getUid())
                .setValue(account)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }


    private void onError() {
        mView.showLoading(false);
        MessageUtils.showLongToastMsg(mContext, "다시 시도해주세요.");
    }


}
