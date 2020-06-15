package com.example.meeting.data.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.ToString;

@Entity(tableName = "accounts")
@ToString
public class Account {

    @PrimaryKey
    @NonNull
    public String uid;

    public String email;

    public String token;

    public String name;

    public String age;

    public String rank;
}
