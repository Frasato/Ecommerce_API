package com.papelariafrasato.api.services;

import com.papelariafrasato.api.dtos.RegisterAddressDto;
import com.papelariafrasato.api.exceptions.EmptyInformationException;
import com.papelariafrasato.api.exceptions.InternalServerException;
import com.papelariafrasato.api.exceptions.UserNotFoundException;
import com.papelariafrasato.api.models.Address;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> getSingleUser(String userId){
        try{
            Optional<User> foundedUser = userRepository.findById(userId);

            if (userId.isEmpty()) throw new EmptyInformationException();

            if (foundedUser.isPresent()) {
                User user = foundedUser.get();
                return ResponseEntity.ok().body(user);
            }

            throw new UserNotFoundException(userId);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity<?> getAllUsers(){
        try{
            List<User> foundedUsers = userRepository.findAll();

            if(foundedUsers.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok().body(foundedUsers);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity<?> registerAddress(RegisterAddressDto addressDto, String userId){
        try {
            Optional<User> foundUser = userRepository.findById(userId);
            if (foundUser.isEmpty()) throw new UserNotFoundException(userId);

            User user = foundUser.get();

            Address address = new Address();
            address.setStreet(addressDto.street());
            address.setCity(addressDto.city());
            address.setDistrict(addressDto.district());
            address.setCountryState(addressDto.countryState());
            address.setCEP(addressDto.CEP());
            address.setNumber(addressDto.number());

            address.setUser(user);
            user.setAddress(address);

            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch(Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

}
