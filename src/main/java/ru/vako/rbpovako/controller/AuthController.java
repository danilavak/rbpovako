package ru.vako.rbpovako.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.vako.rbpovako.model.UserAccount;
import ru.vako.rbpovako.model.UserRole;
import ru.vako.rbpovako.service.UserAccountService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserAccountService service;

    public AuthController(UserAccountService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegistrationRequest request) {
        UserAccount user = service.register(
                request.username(),
                request.email(),
                request.password(),
                UserRole.CANDIDATE
        );
        return UserResponse.from(user);
    }

    @GetMapping("/me")
    public CurrentUserResponse me(Authentication authentication) {
        return new CurrentUserResponse(authentication.getName());
    }

    public record RegistrationRequest(String username, String email, String password) {
    }

    public record UserResponse(Long id, String username, String email, UserRole role) {
        static UserResponse from(UserAccount user) {
            return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        }
    }

    public record CurrentUserResponse(String username) {
    }
}
