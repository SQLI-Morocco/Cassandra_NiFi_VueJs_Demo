CREATE KEYSPACE coronavirus WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '1'
};

CREATE TABLE coronavirus.region_data_global_latest (
    country_region text,
    combined_key text,
    active bigint,
    admin2 text,
    confirmed bigint,
    deaths bigint,
    fips text,
    last_update timestamp,
    lat float,
    long_ float,
    province_state text,
    recovered bigint,
    PRIMARY KEY (country_region, combined_key)
) WITH CLUSTERING ORDER BY (combined_key ASC);

CREATE TABLE coronavirus.region_daily_data_points (
    region text,
    year int,
    month int,
    day int,
    last_update timestamp,
    province_state text,
    admin2 text,
    active bigint,
    combined_key text,
    confirmed bigint,
    deaths bigint,
    fips text,
    lat float,
    long_ float,
    recovered bigint,
    PRIMARY KEY (region, year, month, day, last_update, province_state, admin2))
    WITH CLUSTERING ORDER BY (year DESC, month DESC, day DESC, last_update DESC, province_state ASC, admin2 ASC);