package com.sqli.iscm.c19track.infrastructure.repository.cassandra.entitites;

import com.sqli.iscm.c19track.infrastructure.configuration.cassandra.TableConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Table(value = TableConfig.DATA_TABLE_REGION_DAILY)
public class DataPoint {
    @PrimaryKeyColumn(name = "region", type = PrimaryKeyType.PARTITIONED)
    private @NonNull String region;
    private @NonNull Date last_update;
    private @NonNull Integer confirmed;
    private @NonNull Integer deaths;
    private @NonNull Integer recovered;
    private @NonNull Integer active;
}