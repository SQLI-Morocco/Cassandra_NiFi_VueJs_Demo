package com.sqli.iscm.c19track.domain.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DataPoint {
    private Deaths deaths;
    private Active active;
    private Confirmed confirmed;
    private Recovered recovered;
    private String dateRecorded;
}