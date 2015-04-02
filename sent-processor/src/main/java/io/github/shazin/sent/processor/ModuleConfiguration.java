package io.github.shazin.sent.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

/**
 * Module Configuration
 * 
 * @author shazin
 *
 */

@Configuration
@EnableIntegration
@ComponentScan(basePackages="io.github.shazin.sent.processor")
public class ModuleConfiguration {
	@Autowired
	SentimentProcessor transformer;

	@Bean
	public MessageChannel input() {
		return new DirectChannel();
	}

	@Bean
	MessageChannel output() {
		return new DirectChannel();
	}
	
	@Bean
	PropertiesFactoryBean ignoreWordsProperties() {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocations(new ClassPathResource("ignorewords.properties"));
		return propertiesFactoryBean;
	}
	
	@Bean
	PropertiesFactoryBean negativeWordsProperties() {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocations(new ClassPathResource("negativewords.properties"));
		return propertiesFactoryBean;
	}

}
