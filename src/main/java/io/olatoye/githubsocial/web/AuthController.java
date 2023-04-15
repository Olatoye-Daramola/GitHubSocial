package io.olatoye.githubsocial.web;

import io.olatoye.githubsocial.domain.dto.ResponseSchema;
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
@RequestMapping("api/v1/github-social/auth")
public class AuthController {

    @Value("${login_page}")
    private String loginPageUrl;

    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseSchema> registerUser(@RequestBody RegistrationRequest request) {
        ResponseSchema response = authService.register(request);
        return ResponseEntity.created(URI.create(loginPageUrl)).body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseSchema> authenticateUser(AuthenticationRequest request) {
        ResponseSchema response = authService.authenticate(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
