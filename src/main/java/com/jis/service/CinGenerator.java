package com.jis.service;

import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.UUID;

@Component
public class CinGenerator {

    public String generateCin() {
        String year = String.valueOf(Year.now().getValue());
        String unique = UUID.randomUUID().toString().substring(0, 6);
        return "CIN-" + year + "-" + unique;
    }
}