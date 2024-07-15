package com.group6.swp391.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "price")
@Getter
@Setter
public class CrawledDataProperties {

    private String dola;

}
