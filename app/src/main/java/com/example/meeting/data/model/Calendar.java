package com.example.meeting.data.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.ToString;


@Entity(tableName = "schedulers")
@ToString
public class Calendar {

    @PrimaryKey
    @NonNull
    public String seq;

    public String title;

    public String rootKey;

    public String subKey;

    public long expireTimeMils;

    public String addedByUser;

}
