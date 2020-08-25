package com.sqli.iscm.c19track.domain.repository;

import com.sqli.iscm.c19track.domain.core.Region;
import com.sqli.iscm.c19track.domain.core.Summary;

import java.util.List;

public interface SummaryRepository {
    List<Region> getAllStatsByCountry(long date);

    List<Region> getLatestStatsByCountry();

    Region getLatestCountryStats(String country);

    Summary getLatestGlobalSummary();
}
