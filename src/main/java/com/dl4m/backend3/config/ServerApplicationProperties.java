package com.dl4m.backend3.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "server")
public class ServerApplicationProperties {

    private ErrorProperties errorProperties;

    @Getter
    @Setter
    public static class ServerProperties {
        private int port;
        private String contextPath;
        private ErrorProperties error = new ErrorProperties();
    }

    @Getter
    @Setter
    public static class ErrorProperties {
        private String includeMessage;
        private String includeBindingErrors;
    }
}
