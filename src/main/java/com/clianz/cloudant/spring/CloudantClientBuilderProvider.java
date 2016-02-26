package com.clianz.cloudant.spring;

import java.net.MalformedURLException;
import java.net.URL;

import com.cloudant.client.api.ClientBuilder;

import org.springframework.stereotype.Component;

/**
 * Wraps ClientBuilder so we can unit test around static methods.
 */
@Component
public class CloudantClientBuilderProvider {

	ClientBuilder url(String url) throws MalformedURLException {
		return ClientBuilder.url(new URL(url));
	}

	ClientBuilder account(String account) throws MalformedURLException {
		return ClientBuilder.account(account);
	}
}
