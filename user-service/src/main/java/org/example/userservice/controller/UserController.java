package org.example.userservice.controller;

import org.example.userservice.dto.UserUpdateRequest;
import org.example.userservice.entity.Role;
import org.example.userservice.entity.User;
import org.example.userservice.repository.RoleRepository;
import org.example.userservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody UserUpdateRequest request) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id " + id
                ));

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            if(userRepository.findByEmail(request.getEmail()).isPresent() &&
            !userRepository.findByEmail(request.getEmail()).get().getId().equals(id)){
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Email " + request.getEmail() + " is already taken."
                );
            }
            existingUser.setEmail(request.getEmail());
        }

        if (request.getRoleNames() != null && !request.getRoleNames().isEmpty()) {
            Set<Role> newRoles = request.getRoleNames().stream()
                    .flatMap(roleName -> {
                        Optional<Role> optionalRole = (Optional<Role>) roleRepository.findByName(roleName);
                        if (optionalRole.isEmpty()) {
                            throw new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "Role not found: " + roleName
                            );
                        }
                        return optionalRole.stream();
                    })
                    .collect(Collectors.toSet());

            existingUser.setRoles(newRoles);
        }

        User updatedUser = userRepository.save(existingUser);

        // TODO: ОТПРАВКА KAFKA-СОБЫТИЯ
        // Здесь должен быть вызов KafkaTemplate.send("user.updated", ...)
        // чтобы сообщить другим сервисам (если это потребуется по ТЗ)

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
