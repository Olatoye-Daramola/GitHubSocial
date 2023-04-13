package io.olatoye.githubsocial.web;

import io.olatoye.githubsocial.domain.dto.auth.AuthenticationRequest;
import io.olatoye.githubsocial.domain.dto.auth.RegistrationRequest;
import io.olatoye.githubsocial.service.authService.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("api/v1/github_social/auth")
public class AuthController {

    @Value("${login_page}")
    private String loginPageUrl;

    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest request) {
        String token = authService.register(request);
        if (!token.isEmpty() && !token.isBlank())
            return ResponseEntity
                .created(URI.create(loginPageUrl))
                .body(token);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("TOKEN NOT GENERATED");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(AuthenticationRequest request) {
        String token = authService.authenticate(request);
        if (!token.isEmpty() && !token.isBlank())
            return ResponseEntity.ok().body(token);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("TOKEN NOT GENERATED");
    }
}
