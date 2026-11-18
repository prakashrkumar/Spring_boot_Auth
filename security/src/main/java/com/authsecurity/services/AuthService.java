package com.authsecurity.services;

import com.authsecurity.dtos.UserDto;

public interface AuthService {
   UserDto registerUser(UserDto userDto) ;
}
