package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getUsers(){
        List<UserDTO> userDTOList=userService.listAllUsers();
        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved", userDTOList, HttpStatus.OK));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable("username") String name){
        UserDTO user=userService.findByUserName(name);
        return ResponseEntity.ok(new ResponseWrapper("User is retrieved", user,HttpStatus.OK ));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO userDTO){
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User is created", HttpStatus.CREATED));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO userDTO){
          userService.update(userDTO);
        return ResponseEntity.ok(new ResponseWrapper("User is updated", HttpStatus.OK));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<ResponseWrapper> deleteUser( @PathVariable("username") String userName){
         userService.delete(userName);
         return ResponseEntity.ok(new ResponseWrapper("User deleted", HttpStatus.OK));
    }

}
