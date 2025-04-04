package com.dl4m.backend3.config.cloud;

import com.dl4m.backend3.utils.CloudUtils;
import com.google.cloud.secretmanager.v1.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class SecretManagerUtil {

    // TODO: Consider supporting local secret fetching via a GCP Secret Manager emulator
    //       See: https://cloud.google.com/secret-manager/docs/emulator

    private final String projectId = "devlife4me-generic-apps";
    private final boolean runningInGcp;

    public SecretManagerUtil(CloudUtils cloudUtils) {
        this.runningInGcp = cloudUtils.isRunningInGCP();
    }

    public String getSecret(String secretId) {
        if (!runningInGcp) {
            throw new UnsupportedOperationException("Secret fetching is only supported in GCP runtime.");
        }

        SecretVersionName secretVersion = SecretVersionName.of(projectId, secretId, "latest");

        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersion);
            String secret = response.getPayload().getData().toStringUtf8();
            log.debug("[{}] Successfully fetched secret [{}] from Secret Manager", this.getClass().getSimpleName(), secretId);
            return secret;
        } catch (Exception e) {
            log.error("[{}] Failed to fetch secret [{}] from Secret Manager", this.getClass().getSimpleName(), secretId, e);
            throw new RuntimeException("Unable to retrieve secret: " + secretId, e);
        }
    }
}
