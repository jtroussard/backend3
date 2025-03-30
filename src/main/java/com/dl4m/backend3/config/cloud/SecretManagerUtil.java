package com.dl4m.backend3.config.cloud;

import com.dl4m.backend3.utils.CloudUtils;
import com.google.cloud.secretmanager.v1.*;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SecretManagerUtil {

    private final String projectId = "devlife4me-generic-apps";
    private final boolean runningInGcp;

    public SecretManagerUtil(CloudUtils cloudUtils) {
        this.runningInGcp = cloudUtils.isRunningInGCP();
    }

    public String getSecret(String secretId) {
        if (!runningInGcp) {
            throw new UnsupportedOperationException("Secret fetching is only supported in GCP runtime.");
        }

        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, "latest");
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            return response.getPayload().getData().toStringUtf8();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch secret: " + secretId, e);
        }
    }
}
