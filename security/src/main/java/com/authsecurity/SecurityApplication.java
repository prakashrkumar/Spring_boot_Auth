package com.authsecurity;

import com.authsecurity.config.AppConstants;
import com.authsecurity.entities.Role;
import com.authsecurity.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class SecurityApplication implements CommandLineRunner {
	@Autowired
	private RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
		System.out.println("full authorization and authenration");
	}

	@Override
	public void run(String... args) throws Exception {
		// we will create some default  user role
		// ADMIN AND GUST
		roleRepository.findByName("ROLE_"+AppConstants.ADMIN_ROLE).ifPresentOrElse((Role  role)->{
			System.out.println("ADMIN Role allready exists"+role.getName());
		},()->{
			Role role=new Role();
			role.setName("ROLE_"+AppConstants.ADMIN_ROLE);
			role.setId(UUID.randomUUID());
			roleRepository.save(role);
		});
		roleRepository.findByName("ROLE_"+AppConstants.GUEST_ROLE).ifPresentOrElse((Role role)->{
			System.out.println("GUEST Role allready exists"+role.getName());

		},()->{
			Role role=new Role();
			role.setName("ROLE_"+AppConstants.GUEST_ROLE);
			role.setId(UUID.randomUUID());
			roleRepository.save(role);
		});


	}


}
