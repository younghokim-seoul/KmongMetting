package com.example.meeting.data.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.meeting.data.model.Account;

import io.reactivex.Completable;

@Dao
public interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Account account);

    @Query("SELECT * FROM accounts WHERE uid = :uid")
    Account loadUser(String uid);
}
