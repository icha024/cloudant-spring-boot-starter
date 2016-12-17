package com.clianz.cloudant.spring;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.clianz.cloudant.cloudfoundry.ConfigReader;
import com.cloudant.client.api.ClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CloudantAutoConfigurationTest {

	public static final String TEST_ACCOUNT_NAME = "someAccountName";
	public static final String TEST_URL = "someUrl";
	public static final String TEST_PASSWORD = "testPassword";
	public static final String TEST_USERNAME = "testUserName";
	public static final long TEST_CONNECT_TIMEOUT = 1;
	public static final long TEST_READ_TIMEOUT = 2;
	public static final int TEST_MAX_CONNECTIONS = 3;
	public static final boolean TEST_DISABLE_SSL_AUTH = true;
	public static final String TEST_PROXY_PASSWORD = "testProxyPassword";
	public static final String TEST_PROXY_USER = "testProxyUser";
	public static final String TEST_PROXY_URL = "http://localhost";
	public static final String TEST_PROXY_HOST = "localhost";
	public static final String TEST_VCAP_USERNAME = "abcccctests-9144-4c65-8f10-abcccctests-bluemix";
	public static final String TEST_VCAP_PASSWORD = "abcccctests";

	@InjectMocks
	CloudantAutoConfiguration cloudantAutoConfiguration;

	@Mock
	CloudantClientBuilderProvider clientBuilderProvider;

	@Mock
	ClientBuilder clientBuilder;

	@Before
	public void setup() throws MalformedURLException {
		when(clientBuilderProvider.account(anyString())).thenReturn(clientBuilder);
		when(clientBuilderProvider.url(anyString())).thenReturn(clientBuilder);
		when(clientBuilder.username(anyString())).thenReturn(clientBuilder);
		when(clientBuilder.password(anyString())).thenReturn(clientBuilder);
		when(clientBuilder.proxyURL(any(URL.class))).thenReturn(clientBuilder);
		when(clientBuilder.proxyUser(anyString())).thenReturn(clientBuilder);
		when(clientBuilder.proxyPassword(anyString())).thenReturn(clientBuilder);
		when(clientBuilder.connectTimeout(anyLong(), any(TimeUnit.class)))
				.thenReturn(clientBuilder);
		when(clientBuilder.readTimeout(anyLong(), any(TimeUnit.class)))
				.thenReturn(clientBuilder);
		when(clientBuilder.maxConnections(anyInt())).thenReturn(clientBuilder);
		when(clientBuilder.disableSSLAuthentication()).thenReturn(clientBuilder);
	}

	@Test
	public void testUrlOnly() throws Exception {
		CloudantProperties prop = new CloudantProperties();
		prop.setUrl(TEST_URL);
		prop.setAccount(TEST_ACCOUNT_NAME);

		cloudantAutoConfiguration.createCloudantClient(prop);

		verify(clientBuilderProvider, times(1)).url(TEST_URL);
		verify(clientBuilderProvider, times(0)).account(TEST_ACCOUNT_NAME);
	}

	@Test
	public void testUrlAccountName() throws Exception {
		CloudantProperties prop = new CloudantProperties();
		prop.setAccount(TEST_ACCOUNT_NAME);
		prop.setPassword(TEST_PASSWORD);

		cloudantAutoConfiguration.createCloudantClient(prop);

		verify(clientBuilderProvider, times(0)).url(TEST_URL);
		verify(clientBuilderProvider, times(1)).account(TEST_ACCOUNT_NAME);
		verify(clientBuilder, times(1)).password(TEST_PASSWORD);
	}

	@Test
	public void testLoadCfConfig() throws Exception {
		String cfConfig = new ConfigReader().readFile("cf-config.json");
		cloudantAutoConfiguration.setVcapServices(cfConfig);
		cloudantAutoConfiguration.createCloudantClient(null);

		verify(clientBuilder, times(1)).username(TEST_VCAP_USERNAME);
		verify(clientBuilder, times(1)).password(TEST_VCAP_PASSWORD);
	}

	@Test
	public void testCreateCloudantClient() throws MalformedURLException {
		CloudantProperties prop = new CloudantProperties();
		prop.setAccount(TEST_USERNAME);
		prop.setPassword(TEST_PASSWORD);
		prop.setProxyURL(TEST_PROXY_URL);
		prop.setProxyUser(TEST_PROXY_USER);
		prop.setProxyPassword(TEST_PROXY_PASSWORD);
		prop.setConnectTimeout(TEST_CONNECT_TIMEOUT);
		prop.setReadTimeout(TEST_READ_TIMEOUT);
		prop.setMaxConnections(TEST_MAX_CONNECTIONS);
		prop.setDisableSSLAuthentication(TEST_DISABLE_SSL_AUTH);

		cloudantAutoConfiguration.createCloudantClient(prop);

		verify(clientBuilderProvider, times(0)).url(TEST_URL);
		verify(clientBuilderProvider, times(1)).account(TEST_USERNAME);
		verify(clientBuilder, times(1)).password(TEST_PASSWORD);
		ArgumentCaptor<URL> argument = ArgumentCaptor.forClass(URL.class);
		verify(clientBuilder, times(1)).proxyURL(argument.capture());
		assertEquals(TEST_PROXY_HOST, argument.getValue().getHost());
		verify(clientBuilder, times(1)).proxyUser(TEST_PROXY_USER);
		verify(clientBuilder, times(1)).proxyPassword(TEST_PROXY_PASSWORD);
		verify(clientBuilder, times(1)).connectTimeout(TEST_CONNECT_TIMEOUT, TimeUnit.SECONDS);
		verify(clientBuilder, times(1)).readTimeout(TEST_READ_TIMEOUT, TimeUnit.SECONDS);
		verify(clientBuilder, times(1)).maxConnections(TEST_MAX_CONNECTIONS);
		verify(clientBuilder, times(1)).disableSSLAuthentication();
	}
}
