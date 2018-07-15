package com.example;

import org.junit.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

public class DbDemoApplicationIT {

    @Test
    public void testSendReceiveItems() {
        when().post("/save")
                .then()
                .statusCode(200);
        when().post("/save")
                .then()
                .statusCode(200);
        when().post("/list")
                .then()
                .statusCode(200)
                .body("size()", is(2));
    }
}