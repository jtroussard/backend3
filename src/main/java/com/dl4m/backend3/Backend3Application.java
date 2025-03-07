package com.dl4m.backend3;

import com.dl4m.backend3.config.JwtApplicationProperties;
import com.dl4m.backend3.config.ServerApplicationProperties;
import com.dl4m.backend3.config.SpringBootApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		SpringBootApplicationProperties.class,
		JwtApplicationProperties.class,
		ServerApplicationProperties.class
})
public class Backend3Application {

	public static void main(String[] args) {
		SpringApplication.run(Backend3Application.class, args);
	}

}
