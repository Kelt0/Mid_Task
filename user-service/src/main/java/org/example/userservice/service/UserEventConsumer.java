package org.example.userservice.service;

import org.example.userservice.entity.Role;
import org.example.userservice.entity.User;
import org.example.userservice.repository.RoleRepository;
import org.example.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserEventConsumer {

    private final UserRepository userRepository;
    private  final RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);

    public UserEventConsumer(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @KafkaListener(topics = "user.created", groupId = "user_service_group")
    public void handleUserCreated(UserCreatedEvent event) {

        logger.info("Received UserCreatedEvent for user: ", event.getUsername());

        User user = new User();
        user.setEmail(event.getEmail().toString());
        user.setUsername(event.getUsername().toString());

        Set<Role> roles = event.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName.toString()))
                        .collect(Collectors.toSet());
        user.setRoles(roles);
        userRepository.save(user);
    }
}
