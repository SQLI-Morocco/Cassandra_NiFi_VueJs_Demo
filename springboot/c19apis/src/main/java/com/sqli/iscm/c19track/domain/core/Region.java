package com.sqli.iscm.c19track.domain.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Region {
    private @NonNull String name;
    private Summary summary;
    private List<Province> provinces;
    private List<DataPoint> dataPoints;
}