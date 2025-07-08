package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.LoginDto;
import com.papelariafrasato.api.dtos.RegisterAddressDto;
import com.papelariafrasato.api.dtos.RegisterDto;
import com.papelariafrasato.api.dtos.ResponseUserDto;
import com.papelariafrasato.api.exceptions.InternalServerException;
import com.papelariafrasato.api.exceptions.UserNotFoundException;
import com.papelariafrasato.api.models.Cart;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.AddressRepository;
import com.papelariafrasato.api.repositories.CartRepository;
import com.papelariafrasato.api.repositories.UserRepository;
import com.papelariafrasato.api.services.TokenService;
import com.papelariafrasato.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/auth")
@Tag(
        name = "Authenticate",
        description = "EndPoints to login and register users"
)
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(
            summary = "Register",
            description = "Create a new user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created a new user success"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> userRegister(@RequestBody RegisterDto registerDto){
        User user = new User();
        user.setName(registerDto.name());
        user.setEmail(registerDto.email());
        user.setCpf(registerDto.cpf());
        user.setPhone(registerDto.phone());
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        user.setRole("ROLE_USER");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(0);

        user.setCart(cart);

        userRepository.save(user);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Enter with a existent user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User find success",
                    content = @Content(schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> userLogin(@RequestBody LoginDto loginDto){
        Optional<User> user = userRepository.findByEmail(loginDto.email());

        if(user.isEmpty()){
            throw new RuntimeException("ERROR: Email or Password are wrong!");
        }

        User findedUser = user.orElseThrow(() -> new UserNotFoundException(user.get().getId()));

        if(passwordEncoder.matches(loginDto.password(), findedUser.getPassword())){
            String token = this.tokenService.generateToken(findedUser);
            return ResponseEntity.ok().body(new ResponseUserDto(findedUser.getName(), findedUser.getId(), token));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/address/{userId}")
    @Operation(
            summary = "Register Address",
            description = "Endpoint to register a address for a user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User find success",
                    content = @Content(schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = UserNotFoundException.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "A internal server error occurred",
                    content = @Content(schema = @Schema(implementation = InternalServerException.class))),
    })
    public ResponseEntity<?> registerAddress(@RequestBody RegisterAddressDto addressDto, @PathVariable("userId")String userId){
        return userService.registerAddress(addressDto, userId);
    }

}
