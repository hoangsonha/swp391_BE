package com.group6.swp391.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecaptchaResponse {
    private boolean success;
    private String hostName;
    private String challenge_ts;

    @JsonProperty("error-codes")
    private String[] errorCodes;
}
