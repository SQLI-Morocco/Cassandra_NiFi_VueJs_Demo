package com.sqli.iscm.c19track.domain.service;

import com.sqli.iscm.c19track.domain.core.Summary;
import com.sqli.iscm.c19track.domain.core.Region;
import com.sqli.iscm.c19track.domain.repository.SummaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class C19SummaryService implements SummaryService {

    private SummaryRepository summaryRepository;

    public C19SummaryService(SummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    @Override
    public List<Region> getAllSummaryRegion(long date) {
        return summaryRepository.getAllStatsByCountry(date);
    }

    @Override
    public List<Region> getLatestSummaryByRegion() {
        return summaryRepository.getLatestStatsByCountry();
    }

    @Override
    public Region getLatestRegionSummary(String country) {
        return summaryRepository.getLatestCountryStats(country);
    }

    @Override
    public Summary getLatestGlobalSummary() {
        return summaryRepository.getLatestGlobalSummary();
    }
}
