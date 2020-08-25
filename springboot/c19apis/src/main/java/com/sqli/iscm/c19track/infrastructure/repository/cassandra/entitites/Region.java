package com.sqli.iscm.c19track.infrastructure.repository.cassandra.entitites;

import com.sqli.iscm.c19track.infrastructure.configuration.cassandra.TableConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@Table(value = TableConfig.DATA_TABLE_REGION_DAILY)
public class Region {
    @PrimaryKeyColumn(name = "region", type = PrimaryKeyType.PARTITIONED)
    private @NonNull String region;
}