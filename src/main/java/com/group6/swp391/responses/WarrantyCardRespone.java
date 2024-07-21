package com.group6.swp391.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.group6.swp391.view.Views;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarrantyCardRespone {
    @JsonView(Views.Public.class)
    private int warrantyCardID;

    @JsonView(Views.Public.class)
    private String objectId;

    @JsonView(Views.Public.class)
    private Date purchaseDate;

    @JsonView(Views.Public.class)
    private Date expirationDate;

    @JsonView(Views.Internal.class)
    private int userId;
}
