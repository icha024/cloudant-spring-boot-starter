package com.clianz.cloudant.integration.sample.web;

import com.cloudant.client.api.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class WebServer {

	@Autowired
	Database myItemsDB;

	@RequestMapping("/")
	public String index() {
		return "pong!";
	}

	@RequestMapping("/save")
	public void saveItems() {
		String name = "My Item";
		myItemsDB.save(new com.clianz.cloudant.integration.sample.web.Item(name));
	}

	@RequestMapping("/list")
	public List<com.clianz.cloudant.integration.sample.web.Item> getItems() throws IOException {
		return myItemsDB.getAllDocsRequestBuilder()
				.includeDocs(true)
				.build()
				.getResponse().getDocsAs(com.clianz.cloudant.integration.sample.web.Item.class);
	}
}
