package com.clianz.cloudant.basedoc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.google.gson.Gson;
import org.junit.Test;

public class CloudantDocTest {

	public static class SomeTestDoc extends CloudantDoc {}

	@Test
	public void testCloudantDoc() {
		String testRev = "sghaslkgh3kl5235k235";
		String testId = "3453535hsgh";
		String testDoc = "{\"_id\":\"" + testId + "\", \"_rev\":\"" + testRev + "\"}";
		SomeTestDoc someTestDoc = new Gson().fromJson(testDoc, SomeTestDoc.class);
		assertThat(someTestDoc.getId(), is(testId));
		assertThat(someTestDoc.getRev(), is(testRev));
	}
}
