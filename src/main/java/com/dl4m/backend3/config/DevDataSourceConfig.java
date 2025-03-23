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

        // Base JDBC URL placeholder - Cloud SQL Socket Factory will override
        SpringBootApplicationProperties.DataSourceProperties dsProps = appProperties.getDatasource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/backend3");
        dataSource.setDriverClassName(dsProps.getDriverClassName());
        dataSource.setUsername(secretManagerUtil.getSecret("backend3-dev-db-user"));
        dataSource.setPassword(secretManagerUtil.getSecret("backend3-dev-db-password"));

        // Cloud SQL Socket Factory properties from bindings
        SpringBootApplicationProperties.CloudSqlProperties cloudSql = dsProps.getHikari().getCloudSql();
        dataSource.addDataSourceProperty("socketFactory", cloudSql.getSocketFactory());
        dataSource.addDataSourceProperty("cloudSqlInstance", cloudSql.getCloudSqlInstance());
        dataSource.addDataSourceProperty("sslmode", cloudSql.getSslMode());

        // Hikari pool tuning from properties
        SpringBootApplicationProperties.HikariProperties hikariProps = dsProps.getHikari();
        dataSource.setMaximumPoolSize(hikariProps.getMaximumPoolSize());
        dataSource.setConnectionTimeout(hikariProps.getConnectionTimeout());
        dataSource.setMinimumIdle(hikariProps.getMinimumIdle());
        dataSource.setIdleTimeout(hikariProps.getIdleTimeout());
        dataSource.setMaxLifetime(hikariProps.getMaxLifetime());
        dataSource.setValidationTimeout(hikariProps.getValidationTimeout());
        dataSource.setConnectionTestQuery(hikariProps.getConnectionTestQuery());

        return dataSource;
    }
}
