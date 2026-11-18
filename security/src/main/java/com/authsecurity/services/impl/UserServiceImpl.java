package com.authsecurity.services.impl;



import java.time.Instant;
import java.util.UUID;

import com.authsecurity.config.AppConstants;
import com.authsecurity.entities.Role;
import com.authsecurity.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.authsecurity.dtos.UserDto;
import com.authsecurity.entities.Provider;
import com.authsecurity.entities.User;
import com.authsecurity.exceptions.ResourceNotFoundException;
import com.authsecurity.helpers.UserHelper;
import com.authsecurity.repositories.UserRepository;
import com.authsecurity.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service 
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private  final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
@Override
 @Transactional 
public UserDto createUser(UserDto userDto) {
  if(userDto.getEmail()==null || userDto.getEmail().isBlank()){
    throw new IllegalArgumentException("Email is required");
  }
  if(userRepository.existsByEmail(userDto.getEmail())){
    throw new IllegalArgumentException("user with given email already exists");
  }
  User user=modelMapper.map(userDto,User.class);
  user.setEnable(userDto.getEnable() == null || userDto.getEnable());
  user.setProvider(userDto.getProvider()!=null? userDto.getProvider():Provider.LOCAL);
  //assign the default role
    Role role=roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE).orElse(null);
   user.getRoles().add(role);

  User savedUser=userRepository.save(user);

  return modelMapper.map(savedUser,UserDto.class);
   
}



@Override

public Iterable<UserDto> getAllUsers() {
    // TODO Auto-generated method stub
    return  userRepository.findAll().stream().map((User user) -> modelMapper.map(user, UserDto.class)).toList();
}
@Override
public UserDto getUserByEmail(String email) {
   User user=   userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user not foun with this email"));
    return modelMapper.map(user,UserDto.class);
}
@Override
public UserDto getUserById(String userId) {
    UUID uId=UserHelper.parseUUID(userId);
   User user=userRepository.findById(uId).orElseThrow(()->new ResourceNotFoundException("User not found with given Id"));
   return modelMapper.map(user, UserDto.class);
}
@Override
public UserDto updateUser(UserDto userDto, String userId) {
   UUID uId=UserHelper.parseUUID(userId);
   User existingUser=userRepository.findById(uId)
           .orElseThrow(()->new ResourceNotFoundException("User not found with given id"));
    if(userDto.getName()!=null){
        existingUser.setName(userDto.getName());
    }
    if(userDto.getEmail()!=null){
        existingUser.setEmail(userDto.getEmail());
    }
    if(userDto.getImage()!=null){
        existingUser.setImage(userDto.getImage());
    }
    if(userDto.getProvider()!=null){
        existingUser.setProvider(userDto.getProvider());
    }
    if(userDto.getPassword()!=null){
        existingUser.setPassword(userDto.getPassword());
    }
    if (userDto.getEnable() != null) {
        existingUser.setEnable(userDto.getEnable());
    }
    existingUser.setUpdatedAt(Instant.now());
    User updatedUser=userRepository.save(existingUser);
    return modelMapper.map(updatedUser, UserDto.class);
}
@Override
public void deleteUser(String userId) {
   UUID uId=UserHelper.parseUUID(userId);
   User user=userRepository.findById(uId).orElseThrow(()->new ResourceNotFoundException("User not found with given Id"));
   userRepository.delete(user); 
}
    
}
