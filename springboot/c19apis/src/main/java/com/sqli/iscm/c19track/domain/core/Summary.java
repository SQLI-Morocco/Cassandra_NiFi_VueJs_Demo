package com.sqli.iscm.c19track.domain.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Summary {
    private @NonNull Confirmed confirmed;
    private @NonNull Active active;
    private @NonNull Deaths deaths;
    private @NonNull Recovered recovered;
    private @NonNull Date lastUpdate;

}