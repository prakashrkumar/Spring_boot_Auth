package com.authsecurity.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authsecurity.entities.User;
import com.authsecurity.exceptions.ResourceNotFoundException;
import com.authsecurity.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
@Service 
@RequiredArgsConstructor 
public class CustomUserDetailService implements UserDetailsService{
private  final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user= userRepository.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("Invalied email id"));
      return user;
    }

    
}
