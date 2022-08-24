package ir.baam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableFeignClients
@Configuration
@RefreshScope
@EnableDiscoveryClient
public class SpringBootQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootQuartzApplication.class, args);
	}

}
