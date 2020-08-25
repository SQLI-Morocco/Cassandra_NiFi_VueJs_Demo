package com.sqli.iscm.c19track.infrastructure.repository.cassandra;

import com.sqli.iscm.c19track.infrastructure.configuration.cassandra.TableConfig;
import com.sqli.iscm.c19track.infrastructure.repository.cassandra.entitites.GlobalSummary;
import com.sqli.iscm.c19track.infrastructure.repository.cassandra.entitites.RegionSummary;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CassandraSummaryRepository extends CrudRepository<RegionSummary, String> {
    @Query("SELECT country_region, " +
            "SUM(confirmed) as confirmed, " +
            "SUM(active) as active, " +
            "SUM(deaths) as deaths, " +
            "SUM(recovered) as recovered," +
            "MAX(last_update) as lastUpdate" +
            "FROM " + TableConfig.DATA_TABLE_REGION_DAILY + " WHERE last_update < ?0 " +
            "GROUP BY country_region, last_update")
    List<RegionSummary> getAllSummaryByRegion(long date);

    @Query("SELECT country_region, " +
            "SUM(confirmed) as confirmed, " +
            "SUM(active) as active, " +
            "SUM(deaths) as deaths, " +
            "SUM(recovered) as recovered," +
            "MAX(last_update) as lastUpdate " +
            "FROM " + TableConfig.DATA_TABLE_REGION_LATEST +
            " GROUP BY country_region")
    List<RegionSummary> getLatestSummaryByRegion();

    @Query("SELECT country_region, " +
            "SUM(confirmed) as confirmed, " +
            "SUM(active) as active, " +
            "SUM(deaths) as deaths, " +
            "SUM(recovered) as recovered," +
            "MAX(last_update) as lastUpdate " +
            "FROM " + TableConfig.DATA_TABLE_REGION_LATEST +
            " WHERE country_region = ?0")
    RegionSummary getLatestSummaryByRegion(String region);

    @Query("SELECT SUM(confirmed) as confirmed, " +
            "SUM(active) as active, " +
            "SUM(deaths) as deaths, " +
            "SUM(recovered) as recovered," +
            "MAX(last_update) as lastUpdate " +
            "FROM " + TableConfig.DATA_TABLE_REGION_LATEST)
    GlobalSummary getLatestGlobalSummary();
}
