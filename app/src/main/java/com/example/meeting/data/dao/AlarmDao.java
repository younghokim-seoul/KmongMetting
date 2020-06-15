package com.example.meeting.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.meeting.data.model.Alarm;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Alarm alarm);

    @Query("SELECT * FROM alarms")
    List<Alarm> loadAll();

    @Query("DELETE FROM alarms WHERE id = :id")
    Completable delete(Long id);


}
