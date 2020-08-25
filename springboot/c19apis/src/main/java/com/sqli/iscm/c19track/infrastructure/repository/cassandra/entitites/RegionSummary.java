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
@Table(value = TableConfig.DATA_TABLE_REGION_LATEST)
public class RegionSummary {
    @PrimaryKeyColumn(name = "country_region", type = PrimaryKeyType.PARTITIONED)
    private String country;
    private @NonNull String confirmed;
    private @NonNull String active;
    private @NonNull String deaths;
    private @NonNull String recovered;
    private @NonNull Date lastUpdate;
}