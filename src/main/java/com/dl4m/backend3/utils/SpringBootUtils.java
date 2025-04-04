package com.dl4m.backend3.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringBootUtils {

    private final Environment environment;

    @Autowired
    public SpringBootUtils(Environment environment) {
        this.environment = environment;
    }

    public boolean isProdEnvironment() {
        String[] activeProfiles = this.environment.getActiveProfiles();
        return List.of(activeProfiles).contains("prod");
    }

    public boolean isDevEnvironment() {
        String[] activeProfiles = this.environment.getActiveProfiles();
        return List.of(activeProfiles).contains("dev");
    }

    public boolean isLocalEnvironment() {
        String[] activeProfiles = this.environment.getActiveProfiles();
        return List.of(activeProfiles).contains("local");
    }
}
