package com.clianz.cloudant.cloudfoundry;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.google.gson.Gson;
import org.junit.Test;

public class CfConfigTest {

	@Test
	public void testGetCloudantNoSQLDB() throws Exception {
		String configJson = new ConfigReader().readFile("cf-config.json");
		CfConfig cfConfig = new Gson().fromJson(configJson, CfConfig.class);
		String username = cfConfig.getCloudantNoSQLDB().getCredentials().getUsername();
		String password = cfConfig.getCloudantNoSQLDB().getCredentials().getPassword();
		assertThat(username, is("abcccctests-9144-4c65-8f10-abcccctests-bluemix"));
		assertThat(password, is("abcccctests"));
	}
}