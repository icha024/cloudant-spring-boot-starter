package com.clianz.cloudant.spring.internal;

import com.clianz.bluemix.configurator.BluemixConfigStore;

/**
 * Util for getting Cloudant configs on Bluemix (CloudFoundry).
 *
 * @author Ian Chan
 */
public class CredentialUtils {

	public static final String getCloudantUsername() {
		String username = System.getProperty("cloudant.username", "");
		if (username.length() > 0) {
			return username;
		}
		return BluemixConfigStore.getConfig().getCloudantNoSQLDB().getCredentials().getUsername();
	}

	public static final String getCloudantPassword() {
		String password = System.getProperty("cloudant.password", "");
		if (password.length() > 0) {
			return password;
		}
		return BluemixConfigStore.getConfig().getCloudantNoSQLDB().getCredentials().getPassword();
	}
}
