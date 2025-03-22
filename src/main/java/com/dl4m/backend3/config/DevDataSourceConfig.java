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

        // Pull URL and creds from Secret Manager
        dataSource.setJdbcUrl(secretManagerUtil.getSecret("backend3-dev-db-url"));
        dataSource.setUsername(secretManagerUtil.getSecret("backend3-dev-db-user"));
        dataSource.setPassword(secretManagerUtil.getSecret("backend3-dev-db-password"));

        // Pool settings from properties
        SpringBootApplicationProperties.HikariProperties hikariProps = appProperties.getDatasource().getHikari();
        dataSource.setMaximumPoolSize(hikariProps.getMaximumPoolSize());
        dataSource.setConnectionTimeout(hikariProps.getConnectionTimeout());
        dataSource.setMinimumIdle(hikariProps.getMinimumIdle());
        dataSource.setIdleTimeout(hikariProps.getIdleTimeout());
        dataSource.setMaxLifetime(hikariProps.getMaxLifetime());
        dataSource.setValidationTimeout(hikariProps.getValidationTimeout());
        dataSource.setConnectionTestQuery(hikariProps.getConnectionTestQuery());

        // Add Cloud SQL Socket Factory properties
        var dsProps = new java.util.Properties();
        SpringBootApplicationProperties.CloudSqlProperties cloudSql = hikariProps.getCloudSql();
        dsProps.setProperty("socketFactory", cloudSql.getSocketFactory());
        dsProps.setProperty("cloudSqlInstance", cloudSql.getCloudSqlInstance());
        dsProps.setProperty("sslmode", cloudSql.getSslMode());

        dataSource.setDataSourceProperties(dsProps);

        return dataSource;
    }
}
