package top.dj.business.bootstrap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.dj.business.bootstrap.properties.BusinessLogProperties;

@Configuration
public class BusinessLogConfig {

	@Bean
	@ConfigurationProperties(prefix = "business.log")
	public BusinessLogProperties getBusinessLogProperties() {
		return new BusinessLogProperties();
	}

}
