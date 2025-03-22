package com.dl4m.backend3.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring")
public class SpringBootApplicationProperties {

    private DataSourceProperties datasource;
    private JpaProperties jpa;
    private JwtProperties jwt;
    private ServerProperties server;

    @Getter
    @Setter
    public static class DataSourceProperties {
        private String url;
        private String driverClassName;
        private String username;
        private String password;
        private HikariProperties hikari = new HikariProperties();
    }

    @Getter
    @Setter
    public static class HikariProperties {
        private int maximumPoolSize;
        private int minimumIdle;
        private long idleTimeout;
        private long maxLifetime;
        private long connectionTimeout;
        private long validationTimeout;
        private String connectionTestQuery;
        private CloudSqlProperties cloudSql = new CloudSqlProperties();
    }

    @Getter
    @Setter
    public static class CloudSqlProperties {
        private String socketFactory;
        private String cloudSqlInstance;
        private String sslMode;
    }

    @Getter
    @Setter
    public static class JpaProperties {
        private String databasePlatform;
        private String ddlAuto;
        private boolean showSql;
        private boolean formatSql;
        private boolean openInView;
    }

    @Getter
    @Setter
    public static class JwtProperties {
        private long expiration;
    }

    @Getter
    @Setter
    public static class ServerProperties {
        private int port;
        private String contextPath;
    }
}
