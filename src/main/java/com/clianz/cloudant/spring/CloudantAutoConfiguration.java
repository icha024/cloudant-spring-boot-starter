package com.clianz.cloudant.spring;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.clianz.cloudant.cloudfoundry.CfConfig;
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.google.gson.Gson;
import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Cloudant.
 *
 * @author Ian Chan (ian.chan@clianz.com)
 */
@Configuration
@Log
public class CloudantAutoConfiguration {

	@Autowired
	private CloudantProperties prop;

	@Autowired
	CloudantClientBuilderProvider clientBuilderProvider;

	String vcapServices = System.getenv("VCAP_SERVICES");
	
	@Bean
	@ConditionalOnMissingBean
	public CloudantClient cloudantClient() throws MalformedURLException {
		return createCloudantClient();
	}

	protected CloudantClient createCloudantClient() throws MalformedURLException {
		try {
			if (vcapServices != null) {
				CfConfig cfConfig = new Gson().fromJson(vcapServices, CfConfig.class);
				String username = cfConfig.getCloudantNoSQLDB().getCredentials().getUsername();
				String password = cfConfig.getCloudantNoSQLDB().getCredentials().getPassword();
				if (username != null && password != null) {
					log.info("Using VCAP_SERVICES configuration for Cloudant.");
					return clientBuilderProvider.account(username)
							.username(username)
							.password(password)
							.build();
				}
				else {
					log.info("VCAP_SERVICES invalid, switching to Spring properties");
				}
			}
		}
		catch (IllegalArgumentException e) {
			log.warning("Can not initiate Cloudant client from VCAP_SERVICES, " +
					"switching to use Spring properties.");
		}

		// On Cloudant, account name is username
		ClientBuilder clientBuilder;
		if (prop.getAccount() == null) {
			prop.setAccount(prop.getUsername());
		}

		// To use Spring config file, either url or account/username must be configured.
		// Fallback to use CF's VCAP_SERVICES env variable if property doesn't exist.
		if (prop.getUrl() != null) {
			log.finest("Using Cloudant URL properties config");
			clientBuilder = clientBuilderProvider.url(prop.getUrl());
		}
		else if (prop.getAccount() != null) {
			log.finest("Using Cloudant account name: " + prop.getAccount());
			clientBuilder = clientBuilderProvider.account(prop.getAccount());
		}
		else {
			log.severe("Can not initiate Cloudant client from Spring config.");
			throw new RuntimeException("No valid configuration for Cloudant client found.");
		}

		if (prop.getProxyURL() != null) {
			clientBuilder.proxyURL(new URL(prop.getProxyURL()));
		}

		clientBuilder.username(prop.getUsername())
				.password(prop.getPassword())
				.proxyUser(prop.getProxyUser())
				.proxyPassword(prop.getProxyPassword());

		if (prop.getMaxConnections() > 0) {
			clientBuilder.maxConnections(prop.getMaxConnections());
		}
		if (prop.getConnectTimeout() > 0) {
			clientBuilder.connectTimeout(prop.getConnectTimeout(), TimeUnit.SECONDS);
		}
		if (prop.getReadTimeout() > 0) {
			clientBuilder.readTimeout(prop.getReadTimeout(), TimeUnit.SECONDS);
		}
		if (prop.isDisableSSLAuthentication()) {
			clientBuilder.disableSSLAuthentication();
		}
		return clientBuilder.build();
	}

	protected void setVcapServices(String vcapServices) {
		this.vcapServices = vcapServices;
	}

	protected void setProp(CloudantProperties prop) {
		this.prop = prop;
	}
}