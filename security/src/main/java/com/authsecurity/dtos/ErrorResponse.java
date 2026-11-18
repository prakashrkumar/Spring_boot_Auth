package com.authsecurity.dtos;

import org.springframework.http.HttpStatus;

public record ErrorResponse( 
    String meassge,
    HttpStatus status,
    int statusCode
){

}

