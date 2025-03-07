package com.dl4m.backend3.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;

@Configuration
@Profile("local")
@RequiredArgsConstructor
public class LocalDataSourceConfig {

    private final SpringBootApplicationProperties appProperties;

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setDriverClassName(properties.getDriverClassName());

        dataSource.setMaximumPoolSize(appProperties.getDatasource().getHikari().getMaximumPoolSize());
        dataSource.setMinimumIdle(appProperties.getDatasource().getHikari().getMinimumIdle());
        dataSource.setIdleTimeout(appProperties.getDatasource().getHikari().getIdleTimeout());
        dataSource.setMaxLifetime(appProperties.getDatasource().getHikari().getMaxLifetime());
        dataSource.setConnectionTimeout(appProperties.getDatasource().getHikari().getConnectionTimeout());

        return dataSource;
    }
}
