package com.papelariafrasato.api.dtos;

import com.papelariafrasato.api.models.Address;

public record ResponseUserDto(String name, Address address, String token) {
}
