package com.example.meeting.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@NoArgsConstructor
public class Scheduler implements Serializable {
    private String todo;
    private String subkey;
    private String rootKey;
    private String title;
    private long expireTimeMils;
    private boolean isCommon;

}
