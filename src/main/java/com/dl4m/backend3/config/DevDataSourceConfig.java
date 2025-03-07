package com.dl4m.backend3.config;

import com.dl4m.backend3.config.cloud.SecretManagerUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
public class DevDataSourceConfig {

    private final SecretManagerUtil secretManagerUtil;
    private final SpringBootApplicationProperties appProperties;

    public DevDataSourceConfig(SecretManagerUtil secretManagerUtil, SpringBootApplicationProperties appProperties) {
        this.secretManagerUtil = secretManagerUtil;
        this.appProperties = appProperties;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(secretManagerUtil.getSecret("backend3-dev-db-url"));
        dataSource.setUsername(secretManagerUtil.getSecret("backend3-dev-db-username"));
        dataSource.setPassword(secretManagerUtil.getSecret("backend3-dev-db-password"));

        dataSource.setMaximumPoolSize(appProperties.getDatasource().getHikari().getMaximumPoolSize());
        dataSource.setMinimumIdle(appProperties.getDatasource().getHikari().getMinimumIdle());
        dataSource.setIdleTimeout(appProperties.getDatasource().getHikari().getIdleTimeout());
        dataSource.setMaxLifetime(appProperties.getDatasource().getHikari().getMaxLifetime());
        dataSource.setConnectionTimeout(appProperties.getDatasource().getHikari().getConnectionTimeout());

        return dataSource;
    }
}
