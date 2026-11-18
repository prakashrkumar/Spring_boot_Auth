 

 package com.authsecurity.services;

import com.authsecurity.dtos.UserDto;

public interface UserService {

    // cretae user
    UserDto createUser(UserDto userDto);

    //get user by email
    UserDto getUserByEmail(String email);

    // update User
    UserDto updateUser(UserDto userDto,String userId);

    // delete user
    void deleteUser(String userId);

    //get user by id
    UserDto getUserById(String userId);

    // get all Users
    Iterable<UserDto>getAllUsers();

 

   
    
 }