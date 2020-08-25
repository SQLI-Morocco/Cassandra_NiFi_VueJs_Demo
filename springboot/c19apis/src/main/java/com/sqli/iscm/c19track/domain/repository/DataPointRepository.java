package com.sqli.iscm.c19track.domain.repository;

import com.sqli.iscm.c19track.domain.core.DataPoint;

public interface DataPointRepository {
    Iterable<DataPoint> allDataPointsByRegion(String region);

    Iterable<DataPoint> findAllDeathsByRegion(String region);
}
