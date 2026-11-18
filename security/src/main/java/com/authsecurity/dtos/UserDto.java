package com.authsecurity.dtos;



import java.util.Set;
import java.util.UUID;

import com.authsecurity.entities.Provider;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 

@Builder 
public class UserDto {
    
    private UUID id;
    private String email;
    private String name;
    private String image;
    private String password;
    private Boolean enable;
    private Provider provider;
    private Set<RoleDto> roles;
}

    
