package io.olatoye.githubsocial.service.authService;

import io.olatoye.githubsocial.domain.dto.auth.AuthenticationRequest;
import io.olatoye.githubsocial.domain.dto.auth.RegistrationRequest;

public interface AuthenticationService {
    String register(RegistrationRequest request);
    String authenticate(AuthenticationRequest response);
}
