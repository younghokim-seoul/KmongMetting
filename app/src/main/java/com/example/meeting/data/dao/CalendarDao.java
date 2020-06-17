package com.example.meeting.data.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.meeting.data.model.Calendar;


import java.util.List;

import io.reactivex.Completable;

@Dao
public interface CalendarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Calendar calendar);

    @Query("SELECT * FROM schedulers WHERE rootKey = :rootKey AND addedByUser = :uid")
    List<Calendar> loadMonthSchedulers(String uid, String rootKey);

    @Query("DELETE FROM schedulers")
    Completable delete();
}
