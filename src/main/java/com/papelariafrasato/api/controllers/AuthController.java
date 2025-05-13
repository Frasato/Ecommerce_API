package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.LoginDto;
import com.papelariafrasato.api.dtos.RegisterDto;
import com.papelariafrasato.api.dtos.ResponseUserDto;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.UserRepository;
import com.papelariafrasato.api.services.TokenService;
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
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@RequestBody RegisterDto registerDto){
        User user = new User();
        user.setName(registerDto.name());
        user.setEmail(registerDto.email());
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginDto loginDto){
        Optional<User> user = userRepository.findByEmail(loginDto.email());

        if(user.isEmpty()){
            throw new RuntimeException("ERROR: Email or Password are wrong!");
        }

        User findedUser = user.orElseThrow();

        if(passwordEncoder.matches(loginDto.password(), findedUser.getPassword())){
            String token = this.tokenService.generateToken(findedUser);
            return ResponseEntity.ok().body(new ResponseUserDto(findedUser.getName(), findedUser.getAddress(), token));
        }

        return ResponseEntity.badRequest().build();
    }

}
