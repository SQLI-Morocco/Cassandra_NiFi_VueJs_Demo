package com.sqli.iscm.c19track.infrastructure.repository;

import com.sqli.iscm.c19track.domain.core.Active;
import com.sqli.iscm.c19track.domain.core.Confirmed;
import com.sqli.iscm.c19track.domain.core.DataPoint;
import com.sqli.iscm.c19track.domain.core.Deaths;
import com.sqli.iscm.c19track.domain.core.Recovered;
import com.sqli.iscm.c19track.infrastructure.repository.cassandra.CassandraDataPointRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.sqli.iscm.c19track.app.utils.DateUtil.DATE_FORMAT_GMT;

@Component
public class DataPointRepository implements com.sqli.iscm.c19track.domain.repository.DataPointRepository {
    CassandraDataPointRepository cassandraDataPointRepository;

    public DataPointRepository(CassandraDataPointRepository cassandraDataPointRepository) {
        this.cassandraDataPointRepository = cassandraDataPointRepository;
    }

    @Override
    public List<DataPoint> allDataPointsByRegion(String region) {
        return cassandraDataPointRepository.findAllByRegion(region).stream().map(d -> DataPoint.builder()
                .confirmed(Confirmed.builder().value(d.getConfirmed()).build())
                .active(Active.builder().value(d.getActive()).build())
                .deaths(Deaths.builder().value(d.getDeaths()).build())
                .recovered(Recovered.builder().value(d.getRecovered()).build())
                .dateRecorded(DATE_FORMAT_GMT.format(d.getLast_update()))
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<DataPoint> findAllDeathsByRegion(String region) {
        return cassandraDataPointRepository.findAllByRegion(region).stream().map(d -> DataPoint.builder()
                .deaths(Deaths.builder().value(d.getDeaths()).build())
                .dateRecorded(DATE_FORMAT_GMT.format(d.getLast_update()))
                .build()).collect(Collectors.toList());
    }
}
