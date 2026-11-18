package com.authsecurity.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
   info=@Info (
    title="Auth Apllication build by Prakash kumar",
    description="Generic auth app that can use with any application",
     contact=@Contact (
        name="Prakash kumar",
        url="http://Descriptive.ai",
        email="prakashkumar@descriptive.ai"
     ),
     version="1.0",
     summary="This app is very useful if you dont create auth app from Basic scratch"
   ) ,
   security = {
    @SecurityRequirement(
        name = "bearerAuth"
    )
   }  
)
@SecurityScheme(
    name = "bearerAuth",
    type=SecuritySchemeType.HTTP,
    scheme ="bearer",
    bearerFormat="JWT"
)
public class ApiDocConfig {
    
}
