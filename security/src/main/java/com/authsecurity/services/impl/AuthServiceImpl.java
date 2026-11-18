package com.authsecurity.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authsecurity.dtos.UserDto;
import com.authsecurity.services.AuthService;
import com.authsecurity.services.UserService;

import lombok.AllArgsConstructor;
@Service 
@AllArgsConstructor 
public class AuthServiceImpl  implements AuthService{
    private final PasswordEncoder passwordEncoder;
    private  final UserService userService;
    
@Override
public UserDto registerUser(UserDto userDto) {
   
    // hashing password
    userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
 UserDto userDto1=userService.createUser(userDto);
 return userDto1;
    
}
    
}
