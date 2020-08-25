package com.sqli.iscm.c19track.infrastructure.repository;

import com.sqli.iscm.c19track.domain.core.Active;
import com.sqli.iscm.c19track.domain.core.Confirmed;
import com.sqli.iscm.c19track.domain.core.Deaths;
import com.sqli.iscm.c19track.domain.core.Recovered;
import com.sqli.iscm.c19track.domain.core.Region;
import com.sqli.iscm.c19track.domain.core.Summary;
import com.sqli.iscm.c19track.infrastructure.repository.cassandra.CassandraSummaryRepository;
import com.sqli.iscm.c19track.infrastructure.repository.cassandra.entitites.GlobalSummary;
import com.sqli.iscm.c19track.infrastructure.repository.cassandra.entitites.RegionSummary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SummaryRepository implements com.sqli.iscm.c19track.domain.repository.SummaryRepository {

    CassandraSummaryRepository cassandraSummaryRepository;

    public SummaryRepository(
            CassandraSummaryRepository cassandraSummaryRepository) {
        this.cassandraSummaryRepository = cassandraSummaryRepository;
    }

    @Override
    public List<Region> getAllStatsByCountry(long date) {
        return cassandraSummaryRepository.getAllSummaryByRegion(date)
                .stream()
                .map(c -> Region.builder()
                        .name(c.getCountry())
                        .summary(Summary.builder()
                                .confirmed(Confirmed.builder().value(Integer.valueOf(c.getConfirmed())).build())
                                .active(Active.builder().value(Integer.valueOf(c.getActive())).build())
                                .deaths(Deaths.builder().value(Integer.valueOf(c.getDeaths())).build())
                                .recovered(Recovered.builder().value(Integer.valueOf(c.getRecovered())).build())
                                .lastUpdate(c.getLastUpdate()).build()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Region> getLatestStatsByCountry() {
        return cassandraSummaryRepository.getLatestSummaryByRegion()
                .stream()
                .map(c -> Region.builder()
                        .name(c.getCountry())
                        .summary(Summary.builder()
                                .confirmed(Confirmed.builder().value(Integer.valueOf(c.getConfirmed())).build())
                                .active(Active.builder().value(Integer.valueOf(c.getActive())).build())
                                .deaths(Deaths.builder().value(Integer.valueOf(c.getDeaths())).build())
                                .recovered(Recovered.builder().value(Integer.valueOf(c.getRecovered())).build())
                                .lastUpdate(c.getLastUpdate()).build()).build())
                .collect(Collectors.toList());
    }

    @Override
    public Region getLatestCountryStats(String country) {
        RegionSummary regionSummary = cassandraSummaryRepository.getLatestSummaryByRegion(country);
        return Region.builder()
                .name(country)
                .summary(Summary.builder()
                        .confirmed(Confirmed.builder().value(Integer.valueOf(regionSummary.getConfirmed())).build())
                        .active(Active.builder().value(Integer.valueOf(regionSummary.getActive())).build())
                        .deaths(Deaths.builder().value(Integer.valueOf(regionSummary.getDeaths())).build())
                        .recovered(Recovered.builder().value(Integer.valueOf(regionSummary.getRecovered())).build())
                        .lastUpdate(regionSummary.getLastUpdate()).build()).build();
    }

    @Override
    public Summary getLatestGlobalSummary() {
        GlobalSummary globalSummary = cassandraSummaryRepository.getLatestGlobalSummary();
        return Summary.builder()
                .confirmed(Confirmed.builder().value(Integer.valueOf(globalSummary.getConfirmed())).build())
                .active(Active.builder().value(Integer.valueOf(globalSummary.getActive())).build())
                .deaths(Deaths.builder().value(Integer.valueOf(globalSummary.getDeaths())).build())
                .recovered(Recovered.builder().value(Integer.valueOf(globalSummary.getRecovered())).build())
                .lastUpdate(globalSummary.getLastUpdate()).build();
    }
}
