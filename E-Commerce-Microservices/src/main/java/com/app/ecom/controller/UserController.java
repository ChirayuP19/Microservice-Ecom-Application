package com.app.ecom.controller;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {


    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getAllUser(){
        List<UserResponse> users = userService.fetchAllUser();
        return  ResponseEntity.ok(users);
    }

    @PostMapping("")
    public ResponseEntity<String> createUser(@RequestBody UserRequest user){
        userService.createUser(user);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){

        return userService.fetchAllUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateData(@PathVariable Long id,@RequestBody UserRequest userRequest){
        boolean updated = userService.updateAllUser(id, userRequest);
        if(updated)
            return ResponseEntity.status(HttpStatus.OK).body("User Updated Successfully");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



}
