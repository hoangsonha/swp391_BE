package com.group6.swp391.responses;

import com.fasterxml.jackson.annotation.JsonView;
import com.group6.swp391.view.Views;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObjectResponse {
    @JsonView(Views.Public.class)
    private String status;

    @JsonView(Views.Public.class)
    private String message;

    @JsonView(Views.Public.class)
    private Object data;
}
