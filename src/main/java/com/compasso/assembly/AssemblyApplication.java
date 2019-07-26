package com.compasso.assembly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication(scanBasePackages = { "com.compasso.assembly" })
@EnableAutoConfiguration
@EnableDiscoveryClient(autoRegister = true)
public class AssemblyApplication {

	 public static void main(String[] args) {
	        SpringApplication.run(AssemblyApplication.class, args);
	 }

}
