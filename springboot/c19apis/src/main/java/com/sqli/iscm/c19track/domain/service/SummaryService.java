package com.sqli.iscm.c19track.domain.service;

import com.sqli.iscm.c19track.domain.core.Region;
import com.sqli.iscm.c19track.domain.core.Summary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SummaryService {
    List<Region> getAllSummaryRegion(long date);

    List<Region> getLatestSummaryByRegion();

    Region getLatestRegionSummary(String ccountry);

    Summary getLatestGlobalSummary();
}
