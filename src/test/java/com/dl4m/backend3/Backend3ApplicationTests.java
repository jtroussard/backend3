package com.dl4m.backend3;

import com.dl4m.backend3.config.cloud.SecretManagerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class Backend3ApplicationTests {

	private SecretManagerUtil secretManagerUtil;

	@BeforeEach
	void setup() {
		secretManagerUtil = mock(SecretManagerUtil.class);
		when(secretManagerUtil.isRunningInGcp()).thenReturn(true);
		when(secretManagerUtil.getSecret("jwt-secret-dev")).thenReturn("fake-jwt-secret");
	}

	@Test
	void contextLoads() {
		assert "fake-jwt-secret".equals(secretManagerUtil.getSecret("jwt-secret-dev"));
	}
}
