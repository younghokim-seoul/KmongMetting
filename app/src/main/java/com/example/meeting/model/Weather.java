package com.example.meeting.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class Weather {
    private String PTY;
    private String REH;
    private String RN1;
    private String T1H;
    private String UUU;
    private String VEC;
    private String VVV;
    private String WSD;
}
