package com.sqli.iscm.c19track.domain.service;

import com.sqli.iscm.c19track.domain.core.DataPoint;
import com.sqli.iscm.c19track.domain.core.Region;
import org.springframework.stereotype.Service;

@Service
public interface DataService {
    Iterable<DataPoint> getAllDataPointsByRegion(Region region);

    Iterable<DataPoint> findAllDeathsByRegion(Region region);
}
