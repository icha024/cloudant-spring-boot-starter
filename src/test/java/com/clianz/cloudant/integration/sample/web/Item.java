package com.clianz.cloudant.integration.sample.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@RequiredArgsConstructor
public class Item {

    private final String name;
    private String dateAdded = LocalDateTime.now()
            .format(DateTimeFormatter.ISO_DATE_TIME);

}
