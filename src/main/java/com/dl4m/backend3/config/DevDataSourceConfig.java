package com.dl4m.backend3.config;

import com.dl4m.backend3.config.cloud.SecretManagerUtil;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        System.out.println("TUNA️ DevDataSourceConfig initializing");
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();

        // Base JDBC URL placeholder - Cloud SQL Socket Factory will override
        SpringBootApplicationProperties.DataSourceProperties dsProps = appProperties.getDatasource();
        dataSource.setJdbcUrl(secretManagerUtil.getSecret("jdbc-url-dev"));
        dataSource.setDriverClassName(dsProps.getDriverClassName());
        dataSource.setUsername(secretManagerUtil.getSecret("db-user-dev"));
        dataSource.setPassword(secretManagerUtil.getSecret("db-password-dev"));

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
