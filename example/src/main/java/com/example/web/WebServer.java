package com.example.web;

import com.cloudant.client.api.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebServer {

	@Autowired
	Database mydb;

	@RequestMapping("/")
	public String index() {
		return "pong!";
	}

	@RequestMapping("/save")
	public String saver() {
		mydb.save(new Pet("myPet"));
		return "saved";
	}
}
