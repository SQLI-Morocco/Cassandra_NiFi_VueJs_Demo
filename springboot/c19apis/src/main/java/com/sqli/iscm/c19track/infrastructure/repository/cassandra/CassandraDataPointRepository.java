package com.sqli.iscm.c19track.infrastructure.repository.cassandra;

import com.sqli.iscm.c19track.infrastructure.configuration.cassandra.TableConfig;
import com.sqli.iscm.c19track.infrastructure.repository.cassandra.entitites.DataPoint;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CassandraDataPointRepository extends CrudRepository<DataPoint, String> {
    @Query("SELECT region, " +
            "SUM(confirmed) as confirmed, " +
            "SUM(active) as active, " +
            "SUM(deaths) as deaths, " +
            "SUM(recovered) as recovered," +
            "last_update " +
            "FROM " + TableConfig.DATA_TABLE_REGION_DAILY + " WHERE region = ?0 " +
            " GROUP BY region, year, month, day")
    List<DataPoint> findAllByRegion(String region);

    @Query("SELECT region, " +
            "SUM(deaths) as deaths, " +
            "last_update " +
            "FROM " + TableConfig.DATA_TABLE_REGION_DAILY + " WHERE region = ?0 " +
            " GROUP BY region, year, month, day")
    List<DataPoint> findAllDeathsByRegion(String region);
}
