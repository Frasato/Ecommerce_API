package com.papelariafrasato.api.dtos;

import com.papelariafrasato.api.models.User;

import java.util.List;

public record ResponseListUsersDto(
        List<User> userList
){}
