package com.authsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration 
public class SecurityConfig {
    // this is not user
    // @Bean 
    // public  UserDetailsService users(){
    //     User.UserBuilder userBuilder=User.withDefaultPasswordEncoder();
    //     UserDetails user1=userBuilder.username("Bhushan").password("Prakashr@0234").roles("ADMIN").build();
    //     return new InMemoryUserDetailsManager(user1);
    // }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    } 
    @Bean 
   public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
http.csrf(AbstractHttpConfigurer::disable);
http.authorizeHttpRequests(authorizeHttpRequests->
    authorizeHttpRequests.requestMatchers("/api/v1/auth/register","/api/v1/auth/login").permitAll()
    .anyRequest().authenticated()
    
).httpBasic(Customizer.withDefaults());


    return http.build();

   }
    
}
