package com.clianz.cloudant.spring;

import com.cloudant.client.api.CloudantClient;
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration properties for Cloudant.
 *
 * @author Ian Chan (ian.chan@clianz.com)
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cloudant")
public class CloudantProperties {
	private String account;
	private String username;
	private String password;
	private String url;
	private String proxyURL;
	private String proxyUser;
	private String proxyPassword;
	private long connectTimeout;
	private long readTimeout;
	private int maxConnections;
	private boolean disableSSLAuthentication;
}
