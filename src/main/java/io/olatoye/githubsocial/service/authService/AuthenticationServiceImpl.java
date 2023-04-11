package io.olatoye.githubsocial.service.authService;

import io.olatoye.githubsocial.domain.AuthUser;
import io.olatoye.githubsocial.domain.dto.auth.AuthenticationRequest;
import io.olatoye.githubsocial.domain.dto.auth.RegistrationRequest;
import io.olatoye.githubsocial.exception.GithubSocialInternalServerException;
import io.olatoye.githubsocial.exception.GithubSocialNotFoundException;
import io.olatoye.githubsocial.repository.AuthUserRepository;
import io.olatoye.githubsocial.security.jwt.JwtService;
import io.olatoye.githubsocial.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(RegistrationRequest request) {

        boolean isValidToken = isValidGithubToken(request.getGithubToken(), request.getUsername());
        if (!isValidToken) throw new GithubSocialInternalServerException("GITHUB TOKEN NOT VALID");

        var user = new AuthUser();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getEmail());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var savedUser = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> userRepository.save(user));

        return jwtService.generateToken(savedUser);
    }

    @Override
    public String authenticate(AuthenticationRequest request) {
        Authentication authenticatedUser = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .stream().findFirst()
                .orElseThrow(() -> new GithubSocialNotFoundException("User not registered"));
        return jwtService.generateToken(user);
    }


    // -------------------------------- HELPER METHODS --------------------------------
    private boolean isValidGithubToken(String githubToken, String username) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + githubToken);
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            String baseUrl = Constants.getUserDetailsUrl(username);

            RestTemplate template = new RestTemplate();
            template.exchange(baseUrl, HttpMethod.GET, entity, String.class);
            return true;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError())
                return false;
            throw new GithubSocialInternalServerException(e.getMessage());
        } catch (Exception e) {
            throw new GithubSocialInternalServerException(e.getMessage());
        }
    }
}