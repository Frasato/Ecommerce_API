package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.LoginDto;
import com.papelariafrasato.api.dtos.RegisterDto;
import com.papelariafrasato.api.dtos.ResponseUserDto;
import com.papelariafrasato.api.models.Address;
import com.papelariafrasato.api.models.Cart;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.AddressRepository;
import com.papelariafrasato.api.repositories.CartRepository;
import com.papelariafrasato.api.repositories.UserRepository;
import com.papelariafrasato.api.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

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
        Address address = new Address();
        address.setCEP(registerDto.CEP());
        address.setStreet(registerDto.street());
        address.setCity(registerDto.city());
        address.setNumber(registerDto.number());

        User user = new User();
        user.setName(registerDto.name());
        user.setEmail(registerDto.email());
        user.setCpf(registerDto.cpf());
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        user.setRole("ROLE_USER");

        address.setUser(user);
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(0);

        user.setCart(cart);
        user.setAddress(address);
        address.setUser(user);

        userRepository.save(user);
        return ResponseEntity.ok().build();
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

        User findedUser = user.orElseThrow();

        Address address = addressRepository.findByUserId(findedUser.getId())
                .orElseThrow(() -> new RuntimeException("Error on find address"));

        if(passwordEncoder.matches(loginDto.password(), findedUser.getPassword())){
            String token = this.tokenService.generateToken(findedUser);
            return ResponseEntity.ok().body(new ResponseUserDto(findedUser.getName(), findedUser.getId(), token));
        }

        return ResponseEntity.badRequest().build();
    }

}
