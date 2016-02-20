package com.clianz.cloudant.spring;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.clianz.cloudant.cloudfoundry.CfConfig;
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.java.Log;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration properties for Cloudant.
 *
 * @author Ian Chan (ian.chan@clianz.com)
 */
@Data
@Log
@Configuration
@ConfigurationProperties(prefix = "cloudant")
public class CloudantProperties {
	private CloudantClient cloudantClient;
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

	@PostConstruct
	public void createCloudantClient() throws MalformedURLException {
		try {
			String vcapServices = System.getenv("VCAP_SERVICES");
			if (vcapServices != null) {
				CfConfig cfConfig = new Gson().fromJson(vcapServices, CfConfig.class);
				CfConfig.Credentials credentials = cfConfig.getCloudantNoSQLDB().getCredentials();
				if (credentials.getUsername() != null && credentials.getPassword() != null) {
					username = credentials.getUsername();
					password = credentials.getPassword();
					cloudantClient = ClientBuilder.account(username).username(username).password(password).build();
					log.info("Using VCAP_SERVICES configuration for Cloudant.");
					return;
				}
				else {
					log.info("VCAP_SERVICES invalid, switching to Spring properties");
				}
			}
		}
		catch (IllegalArgumentException e) {
			log.warning("Can not initiate Cloudant client from VCAP_SERVICES, switching to use Spring properties.");
		}

		if (account == null) {
			account = username;
		}

		ClientBuilder clientBuilder;
		// To use Spring config file, either url or account/username must be configured.
		// Fallback to use CF's VCAP_SERVICES env variable if property doesn't exist.
		if (url != null) {
			log.finest("Using Cloudant URL properties config");
			clientBuilder = ClientBuilder.url(new URL(url));
		}
		else if (account != null) {
			log.finest("Using Cloudant account name: " + account);
			clientBuilder = ClientBuilder.account(account);
		}
		else {
			log.severe("Can not initiate Cloudant client from Spring config.");
			throw new RuntimeException("No valid configuration for Cloudant client found.");
		}

		if (proxyURL != null) {
			clientBuilder.proxyURL(new URL(proxyURL));
		}

		clientBuilder.username(username)
				.password(password)
				.proxyUser(proxyUser)
				.proxyPassword(proxyPassword);

		if (maxConnections > 0) {
			clientBuilder.maxConnections(maxConnections);
		}
		if (connectTimeout > 0) {
			clientBuilder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
		}
		if (readTimeout > 0) {
			clientBuilder.readTimeout(readTimeout, TimeUnit.SECONDS);
		}
		if (disableSSLAuthentication) {
			clientBuilder.disableSSLAuthentication();
		}
		cloudantClient = clientBuilder.build();
	}
}
