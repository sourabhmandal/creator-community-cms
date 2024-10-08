package com.nxtweb.supareel;

import com.nxtweb.supareel.role.RoleRepository;
import com.nxtweb.supareel.user.User;
import com.nxtweb.supareel.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.nxtweb.supareel.role.Role;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class SupareelApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupareelApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            String defaultAdmin = "admin@gmail.com";
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(
                    Role.builder()
                            .name("USER")
                            .createdBy(defaultAdmin)
                            .lastModifiedBy(defaultAdmin)
                        .build()
                );
            }
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                Role adminRole = roleRepository.saveAndFlush(
                        Role.builder()
                                .name("ADMIN")
                                .createdBy(defaultAdmin)
                                .lastModifiedBy(defaultAdmin)
                                .build()
                );

                if(userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                    userRepository.save(
                            User.builder()
                                    .email("admin@gmail.com")
                                    .accountLocked(false)
                                    .enabled(false)
                                    .firstName("default")
                                    .lastName("admin")
                                    .roles(new ArrayList<>(List.of(adminRole)))
                                    .build()
                    );
                }
            }

        };
    }
}