package com.clianz.cloudant.integration;

import com.clianz.cloudant.integration.sample.DbDemoApplication;
import com.clianz.cloudant.integration.sample.web.WebServer;
import com.clianz.cloudant.spring.CloudantAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@ImportAutoConfiguration(CloudantAutoConfiguration.class)
@ContextConfiguration(classes = {DbDemoApplication.class, WebServer.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DbDemoApplicationIT {

    @LocalServerPort
    private int port;

    @Test
    public void testSendReceiveItems() {
        given().baseUri("http://localhost:" + port)
                .when()
                .post("/save")
                .then()
                .statusCode(200);
        given().baseUri("http://localhost:" + port)
                .when()
                .post("/save")
                .then()
                .statusCode(200);
        given().baseUri("http://localhost:" + port)
                .when()
                .post("/list")
                .then()
                .statusCode(200)
                .body("size()", is(2));
    }
}