package com.sqli.iscm.c19track.domain.service;

import com.sqli.iscm.c19track.domain.core.DataPoint;
import com.sqli.iscm.c19track.domain.core.Region;
import com.sqli.iscm.c19track.domain.repository.DataPointRepository;
import org.springframework.stereotype.Service;

@Service
class C19DataService implements DataService {

    private DataPointRepository dataPointRepository;

    public C19DataService(DataPointRepository dataRepository) {
        this.dataPointRepository = dataRepository;
    }

    @Override
    public Iterable<DataPoint> getAllDataPointsByRegion(Region region) {
        return dataPointRepository.allDataPointsByRegion(region.getName());
    }
    @Override
    public Iterable<DataPoint> findAllDeathsByRegion(Region region) {
        return dataPointRepository.findAllDeathsByRegion(region.getName());
    }
}
