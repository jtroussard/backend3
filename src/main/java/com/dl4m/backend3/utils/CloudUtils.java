package com.dl4m.backend3.utils;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CloudUtils {

    private final Environment environment;

    public CloudUtils(Environment environment) {
        this.environment = environment;
    }

    public boolean isRunningInGCP() {
        return environment.getActiveProfiles().length > 0 && !environment.acceptsProfiles("local");
    }
}