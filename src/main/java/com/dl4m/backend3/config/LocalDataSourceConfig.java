package com.dl4m.backend3.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;

@Slf4j
@Configuration
@Profile("local")
@RequiredArgsConstructor
public class LocalDataSourceConfig {

    private final SpringBootApplicationProperties appProperties;

    @PostConstruct
    public void logInit() {
        if (log.isDebugEnabled()) {
            log.debug("[{}] Initialized local Hikari DataSource", this.getClass().getSimpleName());
        }
    }

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setDriverClassName(properties.getDriverClassName());

        SpringBootApplicationProperties.HikariProperties hikariProps = appProperties.getDatasource().getHikari();
        dataSource.setMaximumPoolSize(hikariProps.getMaximumPoolSize());
        dataSource.setMinimumIdle(hikariProps.getMinimumIdle());
        dataSource.setIdleTimeout(hikariProps.getIdleTimeout());
        dataSource.setMaxLifetime(hikariProps.getMaxLifetime());
        dataSource.setConnectionTimeout(hikariProps.getConnectionTimeout());

        return dataSource;
    }
}
