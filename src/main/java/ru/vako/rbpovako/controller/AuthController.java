package ru.vako.rbpovako.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.vako.rbpovako.model.UserAccount;
import ru.vako.rbpovako.model.UserRole;
import ru.vako.rbpovako.service.AuthTokenService;
import ru.vako.rbpovako.service.UserAccountService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserAccountService userAccountService;
    private final AuthTokenService authTokenService;

    public AuthController(UserAccountService userAccountService, AuthTokenService authTokenService) {
        this.userAccountService = userAccountService;
        this.authTokenService = authTokenService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegistrationRequest request) {
        UserAccount user = userAccountService.register(
                request.username(),
                request.email(),
                request.password(),
                UserRole.CANDIDATE
        );
        return UserResponse.from(user);
    }

    @PostMapping("/login")
    public TokenPairResponse login(@RequestBody LoginRequest request) {
        return TokenPairResponse.from(authTokenService.login(request.username(), request.password()));
    }

    @PostMapping("/refresh")
    public TokenPairResponse refresh(@RequestBody RefreshRequest request) {
        return TokenPairResponse.from(authTokenService.refresh(request.refreshToken()));
    }

    @GetMapping("/me")
    public CurrentUserResponse me(Authentication authentication) {
        return new CurrentUserResponse(authentication.getName());
    }

    public record RegistrationRequest(String username, String email, String password) {
    }

    public record LoginRequest(String username, String password) {
    }

    public record RefreshRequest(String refreshToken) {
    }

    public record UserResponse(Long id, String username, String email, UserRole role) {
        static UserResponse from(UserAccount user) {
            return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        }
    }

    public record TokenPairResponse(
            String accessToken,
            String refreshToken,
            java.time.Instant accessExpiresAt,
            java.time.Instant refreshExpiresAt
    ) {
        static TokenPairResponse from(AuthTokenService.TokenPair pair) {
            return new TokenPairResponse(
                    pair.accessToken(),
                    pair.refreshToken(),
                    pair.accessExpiresAt(),
                    pair.refreshExpiresAt()
            );
        }
    }

    public record CurrentUserResponse(String username) {
    }
}
