package com.vuongdev.Storeclothes.configuration;

import com.vuongdev.Storeclothes.entity.Role;
import com.vuongdev.Storeclothes.entity.User;
import com.vuongdev.Storeclothes.enums.RolePlay;
import com.vuongdev.Storeclothes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Configuration
public class ApplicationInitConfig {

    private final UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ApplicationInitConfig(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Bean
    ApplicationRunner applicationRunner(){
        return args -> {
            if(userRepository.findByPhoneNumber("admin").isEmpty()){
                Role role = new Role();
                role.setId(2L);
                role.setName(RolePlay.ADMIN.name());
                User user = User.builder()
                        .phoneNumber("admin")
                        .password(passwordEncoder.encode("admin"))
                        .fullName("admin")
                        .email("admin@gmail.com")
                        .address("ssss")
                        .dateOfBirth(new Date())
                        .retypePassword("admin")
                        .role(role)
                        .active(true)
                        .build();
                userRepository.save(user);
            }
        };
    }
}
