package io.olatoye.githubsocial.service.authService;

import io.olatoye.githubsocial.domain.dto.ResponseSchema;
import io.olatoye.githubsocial.domain.dto.auth.AuthenticationRequest;
import io.olatoye.githubsocial.domain.dto.auth.RegistrationRequest;

public interface AuthenticationService {
    ResponseSchema register(RegistrationRequest request);
    ResponseSchema authenticate(AuthenticationRequest response);
}
