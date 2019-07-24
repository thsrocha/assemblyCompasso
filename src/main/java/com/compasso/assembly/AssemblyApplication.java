package com.compasso.assembly;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.ConfigurableEnvironment;


@SpringBootApplication(scanBasePackages = { "com.compasso.assembly" })
@EnableAutoConfiguration
@EnableDiscoveryClient
public class AssemblyApplication {

	@Autowired
	private ConfigurableEnvironment env;

	@PostConstruct
	public void initApplication() throws Exception {
		// log.info("Running with Spring profile(s) : {}",
		// Arrays.toString(env.getActiveProfiles()));
		Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if (activeProfiles.contains(Arrays.asList("prod", "local"))) {
			// log.error("'local' and 'prod' profiles at the same time is not allowed.");
		}
	}

	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(AssemblyApplication.class);
		app.run(args);
	}

}
