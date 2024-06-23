package com.group6.swp391.response;

import com.group6.swp391.model.Order;
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
