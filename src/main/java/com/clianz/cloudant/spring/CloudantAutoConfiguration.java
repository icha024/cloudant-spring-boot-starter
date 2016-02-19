package com.clianz.cloudant.spring;

import com.cloudant.client.api.CloudantClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Cloudant.
 *
 * @author Ian Chan (ian.chan@clianz.com)
 */
@Configuration
@EnableConfigurationProperties(CloudantProperties.class)
public class CloudantAutoConfiguration {

	@Autowired
	private CloudantProperties cloudantProperties;

	@Bean
	@ConditionalOnMissingBean
	public CloudantClient cloudantClient() {
		return cloudantProperties.getCloudantClient();
	}
}
