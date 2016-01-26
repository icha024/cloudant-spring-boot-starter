package com.clianz.cloudant.spring;

import com.clianz.bluemix.configurator.BluemixConfigStore;
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * Configuration properties for Cloudant.
 *
 * @author Ian Chan
 */
@Configuration
@ConfigurationProperties(prefix = "cloudant")
public class CloudantProperties {

	public static final Logger log = Logger.getLogger(CloudantProperties.class.getName());

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
	public void init() throws MalformedURLException {

		try {
			String username = BluemixConfigStore.getConfig().getCloudantNoSQLDB().getCredentials().getUsername();
			String password = BluemixConfigStore.getConfig().getCloudantNoSQLDB().getCredentials().getPassword();
			if (username != null && password != null && username.length() > 0 && password.length() > 0) {
				cloudantClient = ClientBuilder.account(username).username(username).password(password).build();
				log.info("Using VCAP_SERVICES configuration for Cloudant.");
				return;
			} else {
				log.info("VCAP_SERVICES invalid, switching to Spring properties");
			}
		} catch (IllegalArgumentException e) {
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
		} else if (account != null) {
			log.finest("Using Cloudant account name: " + account);
			clientBuilder = ClientBuilder.account(account);
		} else {
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

	public CloudantClient getCloudantClient() {
		return cloudantClient;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProxyURL() {
		return proxyURL;
	}

	public void setProxyURL(String proxyURL) {
		this.proxyURL = proxyURL;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public long getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(long connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public long getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(long readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public boolean isDisableSSLAuthentication() {
		return disableSSLAuthentication;
	}

	public void setDisableSSLAuthentication(boolean disableSSLAuthentication) {
		this.disableSSLAuthentication = disableSSLAuthentication;
	}
}
