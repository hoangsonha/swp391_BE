package com.group6.swp391.responses;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUserRespone {
    private int userId;
    private List<ListOrderUserResphone> orders;
}
