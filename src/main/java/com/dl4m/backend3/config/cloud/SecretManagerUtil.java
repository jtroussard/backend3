package com.dl4m.backend3.config.cloud;

import com.dl4m.backend3.utils.CloudUtils;
import com.google.cloud.secretmanager.v1.*;
import org.springframework.stereotype.Component;

@Component
public class SecretManagerUtil {

    private final String projectId;

    public SecretManagerUtil(CloudUtils cloudUtils) {
        if (cloudUtils.isRunningInGCP()) {
            this.projectId = System.getenv("GCP_PROJECT_ID");
            if (this.projectId == null || this.projectId.isBlank()) {
                throw new IllegalStateException("GCP_PROJECT_ID environment variable is not set.");
            }
        } else {
            this.projectId = "local-run-no-project-id";
        }
    }

    public String getProjectId() {
        return projectId;
    }

    public String getSecret(String secretId) {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, "latest");
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            return response.getPayload().getData().toStringUtf8();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch secret: " + secretId, e);
        }
    }
}
