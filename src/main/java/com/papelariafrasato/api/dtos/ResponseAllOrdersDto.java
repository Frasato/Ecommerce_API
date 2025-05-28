package com.papelariafrasato.api.dtos;

import com.papelariafrasato.api.models.Order;

import java.util.List;

public record ResponseAllOrdersDto(
        List<Order> orders
){}
