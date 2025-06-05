package com.papelariafrasato.api.services;

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

            if (userId.isEmpty()) return ResponseEntity.badRequest().body("User ID can't be empty!");

            if (foundedUser.isPresent()) {
                User user = foundedUser.get();
                return ResponseEntity.ok().body(user);
            }

            return ResponseEntity.notFound().build();
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
