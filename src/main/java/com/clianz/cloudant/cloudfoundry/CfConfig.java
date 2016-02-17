package com.clianz.cloudant.cloudfoundry;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Config parser for Bluemix/CloudFoundry Cloudant configs.
 *
 * @author ian.chan@clianz.com (Ian Chan)
 */
@Setter
public class CfConfig {

	@Getter
	@SerializedName("cloudantNoSQLDB")
	private CloudantNoSQLDB[] cloudantNoSQLDB = new CloudantNoSQLDB[]{new CloudantNoSQLDB()};

	public CloudantNoSQLDB getCloudantNoSQLDB() {
		return cloudantNoSQLDB[0];
	}

	@Data
	public class CloudantNoSQLDB {
		private Credentials credentials = new Credentials();
	}

	@Data
	public class Credentials {
		private String username;
		private String password;
	}
}