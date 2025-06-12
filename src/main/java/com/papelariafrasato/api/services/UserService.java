package com.papelariafrasato.api.services;

import com.papelariafrasato.api.exceptions.EmptyInformationException;
import com.papelariafrasato.api.exceptions.UserNotFoundException;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}
