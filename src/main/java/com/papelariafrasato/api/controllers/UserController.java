package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.ResponseListUsersDto;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(
        name = "User",
        description = "EndPoints to get user by ID or get a list of all active users on database"
)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    @Operation(
            summary = "All Users",
            description = "Get all registered users on database"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A list with all registered users",content = @Content(schema = @Schema(implementation = ResponseListUsersDto.class))),
            @ApiResponse(responseCode = "404", description = "Haven't users registered on database"),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> getAllUser(){
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "Get User",
            description = "Get a single user by your user ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A list with all registered users",content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Haven't user registered on database"),
            @ApiResponse(responseCode = "400", description = "User ID can't be empty!", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> getSingleUser(@PathVariable("userId")String userId){
        return userService.getSingleUser(userId);
    }

}
