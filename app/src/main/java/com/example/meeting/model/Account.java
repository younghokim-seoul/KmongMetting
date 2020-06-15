package com.example.meeting.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class Account {
    private String email;
    private String password;
    private String token;
    private String name;
    private String age;
    private String position;
}
